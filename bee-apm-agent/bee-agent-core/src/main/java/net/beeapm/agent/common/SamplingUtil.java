package net.beeapm.agent.common;

import net.beeapm.agent.config.BeeConfig;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 采样率处理工具类
 *
 * @author yuanlong.chen
 * @date 2018/09/26
 */
public class SamplingUtil {
    private static AtomicLong total = new AtomicLong(0);
    private static AtomicLong currNum = new AtomicLong(0);

    public static long incrTotal() {
        return total.incrementAndGet();
    }

    public static long getTotal() {
        return total.get();
    }

    public static long incrCurrNum() {
        return currNum.incrementAndGet();
    }

    public static long getCurrNum() {
        return currNum.get();
    }

    public static boolean YES() {
        return isCollect();
    }

    public static boolean NO() {
        return !isCollect();
    }

    private static boolean isCollect() {
        if (BeeConfig.me().getRate() <= 0) {
            BeeTraceContext.setCTag("N");
            return false;
        }
        if (BeeConfig.me().getRate() >= 10000) {
            BeeTraceContext.setCTag("Y");
            return true;
        }
        String ctag = BeeTraceContext.getCTag();
        if ("Y".equals(ctag)) {
            return true;
        } else if ("N".equals(ctag)) {
            return false;
        } else if (getCurrNum() == 0) {//第一条采集
            incrCurrNum();
            incrTotal();
            BeeTraceContext.setCTag("Y");
            return true;
        }
        long tmpTotal = incrTotal();
        long tmpCurrNum = getCurrNum() + 1;
        Double rate = tmpCurrNum * 1.0 / tmpTotal * 10000;
        if (rate.intValue() > BeeConfig.me().getRate()) {
            BeeTraceContext.setCTag("N");
            return false;
        }
        incrCurrNum();
        BeeTraceContext.setCTag("Y");
        return true;
    }
}
