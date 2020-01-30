package net.beeapm.agent.reporter;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.BeeUtils;
import net.beeapm.agent.config.ConfigUtils;
import net.beeapm.agent.log.BeeLog;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yuan
 * @date 2018/08/06
 */
public class ReporterFactory {
    private static final LogImpl log = LogManager.getLog(ReporterFactory.class.getSimpleName());
    public static AbstractReporter reporter;
    public static Map<String, AbstractReporter> reporterMap;
    private static BlockingQueue<Span> queue;
    private static ScheduledExecutorService scheduledExecutorService;
    private static String reporterName;
    private static int idleSleep = ConfigUtils.me().getInt("reporter.idleSleep", 100);
    private static int batchSize = ConfigUtils.me().getInt("reporter.batchSize", 100);
    private static final String THREAD_NAME_PREFIX = "bee-reporter-";

    static {

    }

    public static void shutdonw(){
        BeeUtils.shutdown(scheduledExecutorService);
    }

    public synchronized static int init() {
        BeeLog.log("===========================>ReporterFactory::init");
        int threadNum = ConfigUtils.me().getInt("reporter.threadNum", Runtime.getRuntime().availableProcessors());
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private final AtomicLong mThreadNum = new AtomicLong(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, THREAD_NAME_PREFIX + mThreadNum.getAndIncrement());
            }
        });
        if (reporterMap == null) {
            reporterName = ConfigUtils.me().getStr("reporter.name");
            reporterMap = ReporterLoader.loadreporters();
            reporter = reporterMap.get(reporterName);
            if (reporter == null) {
                BeeLog.log("===========================>reporter：" + reporterName + "不存在");
                throw new RuntimeException("reporter：" + reporterName + "不存在");
            }
            reporter.init();
            initQueue();
            initTask(threadNum);
        }
        return 0;
    }

    private static void initQueue() {
        int queueSize = ConfigUtils.me().getInt("reporter.queueSize", 1000);
        queue = new LinkedBlockingQueue<Span>(queueSize);
    }

    private static void initTask(int threadNum) {
        for (int i = 0; i < threadNum; i++) {
            scheduledExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        doReport();
                    }
                }
            });
        }
    }

    public static void doReport() {
        try {
            if (queue.isEmpty()) {
                Thread.sleep(idleSleep);
                return;
            }
            List<Span> list = new ArrayList<Span>(batchSize);
            queue.drainTo(list, batchSize);
            reporter.report(list);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void report(Span span) {
        if (ConfigUtils.me().getBoolean("reporter.log", false)) {
            log.debug(JSON.toJSONString(span));
        }
        //如果队列已满，则返回false
        if (!queue.offer(span)) {
            log.debug("report queue is full.");
        }
    }
}
