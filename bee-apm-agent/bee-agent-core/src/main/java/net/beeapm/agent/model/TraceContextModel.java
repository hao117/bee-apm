package net.beeapm.agent.model;

/**
 * @author yuan
 * @date 2020/05/13
 */
public class TraceContextModel {
    private String parentId;
    private String traceId;
    private String sampled;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSampled() {
        return sampled;
    }

    public void setSampled(String sampled) {
        this.sampled = sampled;
    }

    public TraceContextModel copy() {
        TraceContextModel model = new TraceContextModel();
        model.setSampled(this.sampled);
        model.setTraceId(this.traceId);
        model.setParentId(this.parentId);
        return model;
    }
}
