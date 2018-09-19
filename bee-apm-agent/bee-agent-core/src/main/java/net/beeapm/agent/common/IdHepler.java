package net.beeapm.agent.common;

import net.beeapm.agent.config.ConfigUtils;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yuan on 2018/8/4.
 */
public class IdHepler {
    private static boolean initFlag = false;
    private static char[] lock = new char[1];
    private static CuratorFramework client = null;
    public static String nodeName;
    private static String datePattern = "yyMMddHHmmss";
    private static SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
    public static AtomicLong id = new AtomicLong(1);
    public static String timeTag = sdf.format(new Date());
    public static String prevTimeTag = timeTag;
    public static AtomicInteger initTimes = new AtomicInteger(0);
    private static String rootDir = "/bee/ids/";

    public static void init() {
        synchronized (lock) {
            if(client == null) {
                try {
                    System.out.println("init zk 。。。。。。。。。。。。。。。。。。。。。。");
                    String server = ConfigUtils.me().getStr("id.zk.url");
                    int timeout = ConfigUtils.me().getInt("id.zk.timeout", 5000);//秒
                    int times = ConfigUtils.me().getInt("id.zk.retryTimes", 2);
                    int sleep = ConfigUtils.me().getInt("id.zk.retrySleep", 5000);//毫秒
                    System.out.println("zk address = " + server);
                    if (server == null) {
                        return;
                    }
                    client = CuratorFrameworkFactory.builder()
                            .connectString(server)
                            .sessionTimeoutMs(1000 * 10)
                            .connectionTimeoutMs(timeout)
                            .retryPolicy(new RetryNTimes(times, sleep))
                            .build();
                    client.getConnectionStateListenable().addListener(clientListener);
                    client.start();
                    System.out.println("hlog-agent init node name");
                    genNodeName(initTimes.incrementAndGet());
                    System.out.println("hlog-agent init time tag");
                    initTag();
                    initFlag = true;
                }catch (Throwable e){
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }

    private static void initTag(){
        Calendar cal = Calendar.getInstance();
        timeTag = sdf.format(cal.getTime());
        prevTimeTag = timeTag;
        cal.set(Calendar.SECOND,cal.get(Calendar.SECOND)+1);
        cal.set(Calendar.MILLISECOND,0);
        long delay = cal.getTimeInMillis() - System.currentTimeMillis();
        if(!initFlag){
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    timeTag = sdf.format(new Date());
                }
            },delay,1000);
        }
    }

    public static String id(){
        if(!initFlag) {
            init();
        }
        if(nodeName == null){
            return null;
        }
        if(!prevTimeTag.equals(timeTag)){
            synchronized(IdHepler.class){
                if(!prevTimeTag.equals(timeTag)) {
                    prevTimeTag = timeTag;
                    id.set(1);
                }
            }
        }
        return nodeName+prevTimeTag+id.getAndIncrement();
    }


    private static String buildNodeText(int nodeName,int times){
        StringBuilder sb = new StringBuilder();
        String buildDate = "bee ";
//        try{
//            buildDate = ManifestUtils.getManifest(IdHepler.class).get("Build-Date");
//        }catch (Exception e){
//        }
        sb.append("startTime=").append(sdf.format(new Date()));
//                .append(",times=").append(times)
//                .append(",version=").append(HLogConfig.VERSION)
//                .append(",verNum=").append(HLogConfig.VER_NUM)
//                .append(",buildDate=").append(buildDate)
//                .append(",serverGrp=").append(System.getProperty(Constants.SYS_KEY_HLOG_SERVER_GROUP))
//                .append(",serverAlias=").append(System.getProperty(Constants.SYS_KEY_HLOG_SERVER_ALIAS))
//                .append(",ip=").append(System.getProperty(Constants.SYS_KEY_HLOG_AGENT_IP))
//                .append(",port=").append(System.getProperty("server.port"));
        return sb.toString();
    }

    private static int createNode(int nNodeName,int times){
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(rootDir+nNodeName,buildNodeText(nNodeName,times).getBytes("utf-8"));
            //System.out.println("======> hlog createNode = " + str);
            return 1;
        }catch (KeeperException.NodeExistsException e){
            //System.out.println("NodeExistsException nodeName="+nNodeName);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    private static void genNodeName(int times){
        nodeName = null;
        String s_start = ConfigUtils.me().getStr("id.start","1001");
        String s_end = ConfigUtils.me().getStr("id.end","9999");
        int start = Integer.parseInt(s_start);
        int end = Integer.parseInt(s_end);
        for(int i = start; i <= end; i++){
            int flag = createNode(i,times);
            if(flag == 1){
                nodeName = i + "";
                System.out.println("bee id flag nodeName = " + nodeName);
                break;
            }else if(flag == 0){
                continue;
            }else{
                break;
            }
        }
    }

    // 客户端的监听配置
    static ConnectionStateListener clientListener = new ConnectionStateListener() {
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            if (newState == ConnectionState.CONNECTED) {
                //countDownLatch.countDown();
                System.out.println("zk connected established");
            } else if (newState == ConnectionState.LOST) {
                try {
                    client.close();
                }catch (Exception e){
                }
                client = null;
                try {
                    init();
                } catch (Exception e) {
                    System.err.println("re-init failed");
                }
            }else if(newState == ConnectionState.RECONNECTED){
                try {
                    client.close();
                }catch (Exception e){
                }
                client = null;
                try {
                    init();
                } catch (Exception e) {
                    System.err.println("re-init failed");
                }
            }
        }
    };
}
