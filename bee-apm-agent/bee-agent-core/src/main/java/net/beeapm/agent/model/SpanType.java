package net.beeapm.agent.model;

/**
 * Created by yuan on 2018/6/30.
 */
public class SpanType {
    public final static String PROCESS = "process";
    public final static String SQL = "sql";
    public final static String SQL_PARAM = "sqlp";
    public final static String PARAM = "param";
    public final static String REQUEST = "request";
    public final static String LOGGER = "log";
    public final static String SPRING_TX = "tx";
    public final static String ERROR = "err";
    public final static String REQUEST_PARAM = "reqparam";
}
