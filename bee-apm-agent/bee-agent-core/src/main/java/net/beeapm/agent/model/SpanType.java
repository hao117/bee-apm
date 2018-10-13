package net.beeapm.agent.model;

/**
 * Created by yuan on 2018/6/30.
 */
public class SpanType {
    public final static String PROCESS = "proc";
    public final static String SQL = "sql";
    public final static String SQL_PARAM = "sqlp";
    public final static String PARAM = "para";
    public final static String REQUEST = "req";
    public final static String LOGGER = "log";
    public final static String SPRING_TX = "tx";
    public final static String ERROR = "err";
    public final static String REQUEST_PARAM = "rp";
    public final static String REQUEST_BODY = "rb";
}
