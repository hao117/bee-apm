package net.beeapm.agent.common;

import net.beeapm.agent.config.BeeConfig;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 采样率处理工具类
 *
 * @author yuan
 * @date 2018/09/26
 */
public class SamplingUtil {
    /**
     * 所有待采集总数
     */
    private static AtomicLong total = new AtomicLong(0);
    /**
     * 已被采集数量
     */
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

    /**
     * 采样判断,true采集
     * @return
     */
    public static boolean YES() {
        return isCollect();
    }

    /**
     * 采样判断,true不采集
     * @return
     */
    public static boolean NO() {
        return !isCollect();
    }

    private static boolean isCollect() {
        //采样率小于等于0,不采集,返回false
        if (BeeConfig.me().getRate() <= 0) {
            BeeTraceContext.setSampled(BeeConst.VAL_N);
            return false;
        }
        //采样率大于等于MAX_SAMPLING_RATE,采集,返回true
        if (BeeConfig.me().getRate() >= BeeConst.MAX_SAMPLING_RATE) {
            BeeTraceContext.setSampled(BeeConst.VAL_Y);
            return true;
        }
        String sampled = BeeTraceContext.getSampled();
        if (BeeConst.VAL_Y.equals(sampled)) {
            //采集,返回true
            return true;
        } else if (BeeConst.VAL_N.equals(sampled)) {
            return false;
        } else if (getCurrNum() == 0) {
            //第一条采集,返回true
            incrCurrNum();
            incrTotal();
            BeeTraceContext.setSampled(BeeConst.VAL_Y);
            return true;
        }
        //递增待采集总数
        long tmpTotal = incrTotal();
        //采样计算,假设当前数据会被采集,计算当前采样率
        long tmpCurrNum = getCurrNum() + 1;
        Double rate = tmpCurrNum * 1.0 / tmpTotal * BeeConst.MAX_SAMPLING_RATE;
        //采样大于预设采样率,则不被采集,返回false
        if (rate.intValue() > BeeConfig.me().getRate()) {
            BeeTraceContext.setSampled(BeeConst.VAL_N);
            return false;
        }
        //递增已被采集数量
        incrCurrNum();
        BeeTraceContext.setSampled(BeeConst.VAL_Y);
        return true;
    }
}
