package net.beeapm.agent.plugin.jdbc.common;

import net.beeapm.agent.model.Span;

public class JdbcContext {
    private static final ThreadLocal<Span> localJdbcSpan = new ThreadLocal<Span>();
    public static void remove(){
        localJdbcSpan.remove();
    }

    public static Span getJdbcSpan(){
        return localJdbcSpan.get();
    }

    public static void setJdbcSpan(Span span){
        localJdbcSpan.set(span);
    }
}
