package net.beeapm.agent.common;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.Version;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.config.ConfigUtils;
import net.beeapm.agent.log.LogUtil;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yuan
 * @date 2018/08/04
 */
public class IdHelper {
    private static String datePattern = "yyMMddHHmmss";
    private static SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

    public volatile static int nodeName = 0;
    public volatile static String timestamp = sdf.format(new Date());
    public volatile static String prevTimestamp = timestamp;
    public static AtomicLong id = new AtomicLong(1);
    private static final ILog log = LogFactory.getLog(IdHelper.class.getSimpleName());
    private static ScheduledExecutorService service;
    private static final String THREAD_NAME = "id";

    static {
        service = new ScheduledThreadPoolExecutor(1,new BeeThreadFactory(THREAD_NAME));
    }

    public static void shutdown() {
        BeeUtils.shutdown(service);
    }

    public static void init() {
        try {
            log.info("-----------------------init IdHelper------------------------------");
            String server = ConfigUtils.me().getStr("id.zk.url");
            log.info("zk address = {}", server);
            LogUtil.log("zk address = " + server);
            int sessionTimeout = ConfigUtils.me().getInt("id.zk.sessionTimeoutMs", 15 * 1000);
            int connectionTimeout = ConfigUtils.me().getInt("id.zk.connectionTimeoutMs", 15 * 1000);
            ZkUtils.init(server, sessionTimeout, connectionTimeout);
            ZkUtils.createBeeRootNode();
            ZkUtils.getZkClient().subscribeStateChanges(new IZkStateListener() {
                @Override
                public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {

                }

                @Override
                public void handleNewSession() throws Exception {
                    log.warn("ZK会话超时，ID标识重新生成");
                    //使用原有的nodeName创建节点，如果创建失败，则抢一个新的
                    if (!ZkUtils.createIdNode(nodeName, buildNodeText(nodeName))) {
                        log.info("原有ID标识被占用，重新生成");
                        createBeeIdNode();
                    } else {
                        log.info("使用原有的ID标识");
                    }
                }

                @Override
                public void handleSessionEstablishmentError(Throwable error) throws Exception {

                }
            });
            log.info("bee apm agent init node name");
            createBeeIdNode();
            log.info("bee apm init time tag");
            initTimestamp();
        } catch (Throwable e) {
            throw new RuntimeException("ID生成器初始化失败", e);
        }

    }

    private static void initTimestamp() {
        Calendar cal = Calendar.getInstance();
        timestamp = sdf.format(cal.getTime());
        prevTimestamp = timestamp;
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 1);
        cal.set(Calendar.MILLISECOND, 0);
        long delay = cal.getTimeInMillis() - System.currentTimeMillis();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                timestamp = sdf.format(new Date());
            }
        }, delay, 1000, TimeUnit.MILLISECONDS);

    }

    public static String id() {
        if (nodeName == 0) {
            return null;
        }
        if (!prevTimestamp.equals(timestamp)) {
            synchronized (IdHelper.class) {
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


    private static void createBeeIdNode() {
        String s_start = ConfigUtils.me().getStr("id.start", "1001");
        String s_end = ConfigUtils.me().getStr("id.end", "9999");
        int start = Integer.parseInt(s_start);
        int end = Integer.parseInt(s_end);
        for (int i = start; i <= end; i++) {
            if (ZkUtils.createIdNode(i, buildNodeText(i))) {
                nodeName = i;
                log.info("bee id nodeName = " + nodeName);
                break;
            }
        }
    }

}
