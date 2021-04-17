package net.beeapm.agent.common;

import net.beeapm.agent.common.ids.IdGenerator;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.model.GcInfo;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanKind;
import net.beeapm.agent.reporter.ReporterFactory;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * JVM信息采集
 *
 * @author yuan
 * @date 2020-01-030
 */

public class JvmInfoTask {
    private static final String THREAD_NAME = "jvm";
    private static ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1, new BeeThreadFactory(THREAD_NAME));

    private static GcInfo preGcInfo;

    public static void start() {
        int period = BeeConfig.me().getJvmPeriod();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Span span = new Span(SpanKind.JVM);
                span.setId(IdGenerator.generator().id());
                span.setStartTime(System.currentTimeMillis());
                buildJvmInfo(span);
                ReporterFactory.report(span);
            }
        }, 0, period, TimeUnit.SECONDS);
    }

    public static void shutdown() {
        if (service != null) {
            service.shutdown();
        }
    }

    private static void buildJvmInfo(Span span) {
        GcInfo currGcInfo = JvmUtils.getGcInfo();
        GcInfo gc = new GcInfo();
        if (preGcInfo == null) {
            gc.setOldGcCount(0);
            gc.setOldGcTime(0);
            gc.setYoungGcCount(0);
            gc.setYoungGcTime(0);
        } else {
            gc.setYoungGcCount(currGcInfo.getYoungGcCount() - preGcInfo.getYoungGcCount());
            gc.setYoungGcTime(currGcInfo.getYoungGcTime() - preGcInfo.getYoungGcTime());
            gc.setOldGcCount(currGcInfo.getOldGcCount() - preGcInfo.getOldGcCount());
            gc.setOldGcTime(currGcInfo.getOldGcTime() - preGcInfo.getOldGcTime());
        }
        preGcInfo = currGcInfo;
        span.addAttribute("gc", gc);
        span.addAttribute("heap", JvmUtils.getHeap());
        span.addAttribute("memory", JvmUtils.getMemorySpaceInfo());
        span.addAttribute("thread", JvmUtils.getThreadInfo());
    }
}
