package net.beeapm.agent.common;

import net.beeapm.agent.config.BeeConfig;

import java.util.concurrent.atomic.AtomicLong;

public class CollectRate {
    private static AtomicLong total = new AtomicLong(0);
    private static AtomicLong currNum = new AtomicLong(0);

    public static long incrTotal(){
        return total.incrementAndGet();
    }

    public static long getTotal(){
        return total.get();
    }

    public static long incrCurrNum(){
        return currNum.incrementAndGet();
    }

    public static long getCurrNum(){
        return currNum.get();
    }

    public static boolean isCollect(){
        String ctag = BeeTraceContext.getCTag();
        if("Y".equals(ctag)){
            return true;
        }else if("N".equals(ctag)){
            return false;
        }else {
            long tmpTotal = incrTotal();
            long tmpCurrNum = getCurrNum() + 1;
            if(tmpCurrNum*10000/tmpTotal > BeeConfig.me().getRate()){
                BeeTraceContext.setCTag("N");
                return false;
            }
            incrCurrNum();
            BeeTraceContext.setCTag("Y");
            return true;
        }
    }
}
