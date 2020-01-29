package net.beeapm.ui.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryForever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuan
 * @date 2020/01/28
 */
@Service
public class ZkClient {
    private static CuratorFramework client = null;
    private static char[] lock = new char[1];
    private static Logger log = LoggerFactory.getLogger(ZkClient.class);

    /**
     * 客户端的监听配置
     */
    private ConnectionStateListener clientListener = new ConnectionStateListener() {
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            if (newState == ConnectionState.CONNECTED) {
                log.error("zk connected established");
            } else if (newState == ConnectionState.LOST) {
                log.warn("ZK ConnectionState LOST");
                try {
                    client.close();
                } catch (Exception e) {
                }
                client = null;
                try {
                    init();
                } catch (Exception e) {
                    log.error("LOST re-init failed", e);
                }
            } else if (newState == ConnectionState.RECONNECTED) {
                log.warn("ZK ConnectionState RECONNECTED");
                try {
                    client.close();
                } catch (Exception e) {
                }
                client = null;
                try {
                    init();
                } catch (Exception e) {
                    log.error("RECONNECTED re-init failed", e);
                }
            }
        }
    };

    @PostConstruct
    public void init() {
        synchronized (lock) {
            if (client == null) {
                try {
                    String server = ConfigHolder.getProperty("zk.url");
                    log.info("zk address = {}", server);
                    int sessionTimeoutMs = ConfigHolder.getPropInt("zk.sessionTimeoutMs", 5 * 1000);
                    int connectionTimeoutMs = ConfigHolder.getPropInt("zk.connectionTimeoutMs", 5 * 1000);
                    int retryIntervalMs = ConfigHolder.getPropInt("zk.retryIntervalMs", 5 * 1000);
                    log.info("zk url={},sessionTimeoutMs={},connectionTimeoutMs={},retryIntervalMs={}", server, sessionTimeoutMs, connectionTimeoutMs, retryIntervalMs);
                    if (server == null) {
                        return;
                    }
                    client = CuratorFrameworkFactory.builder()
                            .connectString(server)
                            .sessionTimeoutMs(sessionTimeoutMs)
                            .connectionTimeoutMs(connectionTimeoutMs)
                            .retryPolicy(new RetryForever(retryIntervalMs))
                            .build();
                    client.getConnectionStateListenable().addListener(clientListener);
                    client.start();
                } catch (Throwable e) {
                    log.error("zk初始化失败", e);
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (client != null) {
                client.close();
            }
            client = null;
        } catch (Exception e) {
            log.error("close zk error", e);
        }
    }

    public CuratorFramework getClient() {
        return client;
    }

    public List<String> getChildren(String path) throws Exception {
        List list = getClient().getChildren().forPath(path);
        if (list == null) {
            list = new ArrayList(1);
        }
        return list;
    }

    public List<JSONObject> getChildrenValueList(String path) throws Exception {
        List<String> nodeList = getChildren(path);
        List<JSONObject> valueList = new ArrayList<>(nodeList.size());
        for (int i = 0; i < nodeList.size(); i++) {
            String childPath = path + "/" + nodeList.get(i);
            String value = new String(client.getData().forPath(childPath), "utf-8");
            valueList.add(JSON.parseObject(value));
        }
        return valueList;
    }
}
