package net.beeapm.agent.reporter;

import net.beeapm.agent.config.ConfigUtils;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author yuan
 * @date 2018/08/06
 */
public class ReporterFactory {
    private static final LogImpl log = LogManager.getLog(ReporterFactory.class.getSimpleName());
    public static AbstractReporter reporter;
    public static Map<String, AbstractReporter> reporterMap;
    private static BlockingQueue<Span> queue ;
    private static ScheduledExecutorService scheduledExecutorService;
    private static String reporterName;
    private static int idleSleep = ConfigUtils.me().getInt("reporter.idleSleep",100);
    private static int batchSize = ConfigUtils.me().getInt("reporter.batchSize",100);
    private static final String THREAD_NAME_PREFIX = "bee-reporter-";
    public synchronized static int init(){
        System.out.println("===========================>ReporterFactory::init");
        if(reporterMap == null) {
            reporterName = ConfigUtils.me().getStr("reporter.name");
            reporterMap = ReporterLoader.loadreporters();
            reporter = reporterMap.get(reporterName);
            if(reporter == null){
                System.out.println("===========================>reporter：" + reporterName + "不存在");
                throw new RuntimeException("reporter：" + reporterName + "不存在");
            }
            reporter.init();
            initQueue();
            initTask();
        }
        return 0;
    }

    private static void initQueue(){
        int queueSize = ConfigUtils.me().getInt("reporter.queueSize",1000);
        queue = new LinkedBlockingQueue<Span>(queueSize);
    }

    private static void initTask(){
        int threadNum = ConfigUtils.me().getInt("reporter.threadNum",Runtime.getRuntime().availableProcessors());
        scheduledExecutorService = Executors.newScheduledThreadPool(threadNum);
        for(int i = 0; i < threadNum; i++){
            final int idx = i;
            scheduledExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName(THREAD_NAME_PREFIX + idx);
                    while (true) {
                        doReport();
                    }
                }
            });
        }
    }

    public static void doReport(){
        try{
            if(queue.isEmpty()){
                Thread.sleep(idleSleep);
                return;
            }
            List<Span> list = new ArrayList<Span>(batchSize);
            queue.drainTo(list,batchSize);
            reporter.report(list);
        }catch (Exception e){
            log.error("",e);
        }
    }

    public static void report(Span span){
        //如果队列已满，则返回false
        queue.offer(span);
    }
}
