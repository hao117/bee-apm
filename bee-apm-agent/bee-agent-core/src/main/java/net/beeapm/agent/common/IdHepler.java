package net.beeapm.agent.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yuan on 2018/8/4.
 */
public class IdHepler {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
    private static final AtomicLong idx = new AtomicLong(0);
    public static String id(){
        return dateFormat.format(new Date()) + String.format("%05d",idx.incrementAndGet());
    }
}
