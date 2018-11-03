package net.beeapm.agent.transmit.kafka1x;

public class KfkConst {
    /** 序列化方式 */
    public static final String DEF_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    /** 重试次数 */
    public static final String DEF_RETRIES = "3";
    /** 重试等待时间，毫秒 */
    public static final String DEF_RETRY_BACKOFF_MS = "100";
    /** 请求超时时间，毫秒 */
    public static final String DEF_REQUEST_TIMEOUT_MS = "600";
    public static final String DEF_PARTITIONER_CLASS = "org.apache.kafka.clients.producer.internals.DefaultPartitioner";
}
