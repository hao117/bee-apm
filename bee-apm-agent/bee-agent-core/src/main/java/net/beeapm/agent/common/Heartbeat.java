package net.beeapm.agent.common;

import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.reporter.ReporterFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Heartbeat {
    private static ScheduledExecutorService service = Executors.newScheduledThreadPool(1,new BeeThreadFactory("heartbeat"));

    public static void start(){
        int period = BeeConfig.me().getPeriod();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Span span = new Span(SpanType.HEARTBEAT);
                span.fillEnvInfo();
                span.setId(IdHepler.id());
                ReporterFactory.report(span);
            }
        },0,period, TimeUnit.SECONDS);
    }
}
