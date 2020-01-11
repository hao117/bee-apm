package net.beeapm.agent.common;

import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.reporter.ReporterFactory;

import java.util.concurrent.*;

public class Heartbeat {
    private static ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1, new BeeThreadFactory("heartbeat"));

    public static String id;

    public static void start() {
        int period = BeeConfig.me().getPeriod();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Span span = new Span(SpanType.HEARTBEAT);
                span.fillEnvInfo();
                span.setId(getId());
                ReporterFactory.report(span);
            }
        }, 0, period, TimeUnit.SECONDS);
    }

    private static String getId() {
        if (id != null) {
            return id;
        }
        id = BeeConfig.me().getEnv() + "_" + BeeConfig.me().getApp() + "_" + BeeConfig.me().getInst() + "_" + BeeConfig.me().getPort();
        return id;
    }
}
