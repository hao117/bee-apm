package net.beeapm.agent.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuan
 * @date 2018-08-06
 */
public class Span {
    private String id;
    /**
     * span名称
     */
    private String name;
    /**
     * span类型
     */
    private String kind;
    /**
     * 实例标识ID
     */
    private String instanceId;
    /**
     * 开始时间,单位纳秒
     */
    private Long startTime;
    /**
     * 结束时间,单位纳秒
     */
    private Long endTime;
    /**
     * 持续时间,单位纳秒
     */
    private Long duration;
    /**
     * span 属性
     */
    private Attributes attributes;
    /**
     * 父 span id
     */
    private String parentId;
    /**
     * 链路ID
     */
    private String traceId;
    /**
     * 暂存数据,不上报
     */
    private Map<String, Object> cacheMap;

    /**
     * 是否异常,0正常,1异常
     */
    private Integer exception = 0;

    /**
     * 子span数量
     */
    private AtomicInteger totalChild = new AtomicInteger();

    public Span(String kind) {
        this.kind = kind;
        this.startTime = System.currentTimeMillis();
    }


    public String getId() {
        return id;
    }

    public Span setId(String id) {
        this.id = id;
        return this;
    }

    public Span setName(String name) {
        this.name = name;
        return this;
    }

    public Span setKind(String kind) {
        this.kind = kind;
        return this;
    }

    public Span setInstanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public Span setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Span setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public Span setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public Span setAttributes(Attributes attributes) {
        this.attributes = attributes;
        return this;
    }

    public Span addAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new Attributes();
        }
        this.attributes.addAttribute(key, value);
        return this;
    }

    public Object getAttribute(String key) {
        if (this.attributes != null) {
            return this.attributes.get(key);
        }
        return null;
    }

    public Span removeAttribute(String key) {
        if (this.attributes != null) {
            this.attributes.remove(key);
        }
        return this;
    }

    public Span setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public Span setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public String getParentId() {
        return parentId;
    }

    public String getTraceId() {
        return traceId;
    }

    public Integer getException() {
        return exception;
    }

    public Span setException(Integer exception) {
        this.exception = exception;
        return this;
    }

    public Span addCache(String key, Object value) {
        if (this.cacheMap == null) {
            this.cacheMap = new HashMap<>(16);
        }
        this.cacheMap.put(key, value);
        return this;
    }

    public Object getCache(String key) {
        if (this.cacheMap != null) {
            this.cacheMap.get(key);
        }
        return null;
    }

    public Span removeCache(String key) {
        if (this.cacheMap != null) {
            this.cacheMap.remove(key);
        }
        return this;
    }

    public Span clearCache() {
        this.cacheMap = null;
        return this;
    }

    public Integer getTotalChild() {
        return totalChild.intValue();
    }

    public Span addTotalChild(Integer delta) {
        this.totalChild.addAndGet(delta);
        return this;
    }

    public Span incrementTotalChild() {
        this.totalChild.incrementAndGet();
        return this;
    }
}
