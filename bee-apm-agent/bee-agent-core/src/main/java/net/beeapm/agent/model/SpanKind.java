package net.beeapm.agent.model;

/**
 * @date 2018/6/30.
 * @author  yuan
 */
public class SpanKind {
    public final static String METHOD = "method";
    public final static String SQL = "sql";
    public final static String SQL_PARAM = "sql_param";
    public final static String PARAM = "param";
    public final static String SERVER = "server";
    public final static String CLIENT = "client";
    public final static String LOGGER = "logger";
    public final static String SPRING_TX = "tx";
    public final static String ERROR = "error";
    public final static String REQUEST_PARAM = "req_param";
    public final static String REQUEST_BODY = "req_body";
    public final static String REQUEST_HEADERS ="req_headers";
    public final static String RESPONSE_BODY = "resp_body";
    public final static String HEARTBEAT = "heartbeat";
    public final static String TOPOLOGY = "topo";
    public final static String JVM = "jvm";
    public final static String THREAD = "thread";

}
