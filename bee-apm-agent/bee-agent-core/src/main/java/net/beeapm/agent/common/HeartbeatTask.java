package net.beeapm.agent.common;

import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.reporter.ReporterFactory;

/**
 * 心跳数据
 * @author yuan
 * @date 2018-11-07
 */
import java.util.concurrent.*;

public class HeartbeatTask {
    private static ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1, new BeeThreadFactory("heartbeat"));

    public static String id;

    public static void start() {
        int period = BeeConfig.me().getHeartbeatPeriod();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Span span = new Span(SpanType.HEARTBEAT);
                span.setId(getId());
                span.fillEnvInfo();
                ReporterFactory.report(span);
            }
        }, 0, period, TimeUnit.SECONDS);
    }



    public static void shutdown(){
        BeeUtils.shutdown(service);
    }

    private static String getId() {
        if (id != null) {
            return id;
        }
        id = BeeConfig.me().getEnv() + "_" + BeeConfig.me().getApp() + "_" + BeeConfig.me().getInst() + "_" + BeeConfig.me().getPort();
        return id;
    }
}
