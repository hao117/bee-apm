package net.beeapm.agent.common;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.Version;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.config.ConfigUtils;
import net.beeapm.agent.log.BeeLogUtil;
import net.beeapm.agent.log.Log;
import net.beeapm.agent.log.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yuan
 * @date 2018/08/04
 */
public class IdHepler {
    private static boolean initFlag = false;
    private static char[] lock = new char[1];
    private static CuratorFramework client = null;
    public static String nodeName;
    private static String datePattern = "yyMMddHHmmss";
    private static SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
    public static AtomicLong id = new AtomicLong(1);
    public static String timestamp = sdf.format(new Date());
    public static String prevTimestamp = timestamp;
    public static AtomicInteger initTimes = new AtomicInteger(0);
    private static String rootDir = "/bee/ids/";
    private static final Log log = LogFactory.getLog(IdHepler.class.getSimpleName());
    private static ScheduledExecutorService service;
    private static final String THREAD_NAME = "id";

    static {
        service = new ScheduledThreadPoolExecutor(1, new BeeThreadFactory(THREAD_NAME));
    }

    public static void shutdown() {
        BeeUtils.shutdown(service);
    }

    public static void init() {
        synchronized (lock) {
            if (client == null) {
                try {
                    log.info("init zk 。。。。。。。。。。。。。。。。。。。。。。");
                    String server = ConfigUtils.me().getStr("id.zk.url");
                    log.info("zk address = {}", server);
                    BeeLogUtil.log("zk address = " + server);
                    int timeout = ConfigUtils.me().getInt("id.zk.timeout", 5000);
                    int times = ConfigUtils.me().getInt("id.zk.retryTimes", 2);
                    int sleep = ConfigUtils.me().getInt("id.zk.retrySleep", 5000);
                    System.out.println("zk address = " + server);
                    if (server == null) {
                        return;
                    }
                    client = CuratorFrameworkFactory.builder()
                            .connectString(server)
                            .sessionTimeoutMs(timeout)
                            .connectionTimeoutMs(timeout)
                            .retryPolicy(new RetryNTimes(times, sleep))
                            .build();
                    client.getConnectionStateListenable().addListener(clientListener);
                    client.start();
                    log.info("bee apm agent init node name");
                    genNodeName(initTimes.incrementAndGet());
                    log.info("bee apm init time tag");
                    initTimestamp();
                    initFlag = true;
                } catch (Throwable e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }

    private static void initTimestamp() {
        Calendar cal = Calendar.getInstance();
        timestamp = sdf.format(cal.getTime());
        prevTimestamp = timestamp;
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 1);
        cal.set(Calendar.MILLISECOND, 0);
        long delay = cal.getTimeInMillis() - System.currentTimeMillis();
        if (!initFlag) {
            service.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    timestamp = sdf.format(new Date());
                }
            }, delay, 1000, TimeUnit.MILLISECONDS);

        }
    }

    public static String id() {
        if (!initFlag) {
            init();
        }
        if (nodeName == null) {
            return null;
        }
        if (!prevTimestamp.equals(timestamp)) {
            synchronized (IdHepler.class) {
                if (!prevTimestamp.equals(timestamp)) {
                    prevTimestamp = timestamp;
                    id.set(1);
                }
            }
        }
        return nodeName + prevTimestamp + id.getAndIncrement();
    }


    private static String buildNodeText(int nodeName) {
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("time", sdf.format(new Date()));
        infoMap.put("ip", BeeConfig.me().getIp());
        infoMap.put("port", BeeConfig.me().getPort());
        infoMap.put("app", BeeConfig.me().getApp());
        infoMap.put("inst", BeeConfig.me().getInst());
        infoMap.put("nodeName", nodeName);
        infoMap.put("version", Version.VERSION);
        return JSON.toJSONString(infoMap);
    }

    private static int createNode(int nNodeName, int times) {
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(rootDir + nNodeName, buildNodeText(nNodeName).getBytes("utf-8"));
            return 1;
        } catch (KeeperException.NodeExistsException e) {
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void genNodeName(int times) {
        nodeName = null;
        String s_start = ConfigUtils.me().getStr("id.start", "1001");
        String s_end = ConfigUtils.me().getStr("id.end", "9999");
        int start = Integer.parseInt(s_start);
        int end = Integer.parseInt(s_end);
        for (int i = start; i <= end; i++) {
            int flag = createNode(i, times);
            if (flag == 1) {
                nodeName = i + "";
                log.info("bee id flag nodeName = " + nodeName);
                break;
            } else if (flag == 0) {
                continue;
            } else {
                break;
            }
        }
    }

    /**
     * 客户端的监听配置
     */
    static ConnectionStateListener clientListener = new ConnectionStateListener() {
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            if (newState == ConnectionState.CONNECTED) {
                log.error("zk connected established");
            } else if (newState == ConnectionState.LOST) {
                BeeUtils.close(client);
                try {
                    init();
                } catch (Exception e) {
                    log.error("re-init failed");
                }
            } else if (newState == ConnectionState.RECONNECTED) {
                BeeUtils.close(client);
                client = null;
                try {
                    init();
                } catch (Exception e) {
                    log.error("re-init failed");
                }
            }
        }
    };
}
