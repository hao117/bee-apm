package net.beeapm.agent.plugin.common;

import net.beeapm.agent.model.Span;

/**
 * @date 2018/8/22
 * @author kaddddd
 */
public class SpringTxContext {
    private static final ThreadLocal<Span> localTxSpan = new ThreadLocal<Span>();
    public static void remove(){
        localTxSpan.remove();
    }

    public static Span getTxSpan(){
        return localTxSpan.get();
    }

    public static void setTxSpan(Span span){
        localTxSpan.set(span);
    }
}
