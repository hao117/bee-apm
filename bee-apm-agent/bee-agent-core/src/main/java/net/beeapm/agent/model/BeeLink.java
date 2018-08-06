package net.beeapm.agent.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan on 2018/7/29.
 */
public class BeeLink {
    private Map<String,Span> spanMap = new HashMap<String,Span>();
    private String id;
    private String pId;
    private String gId;
    private String cTag;

    public String getId() {
        return id;
    }

    public BeeLink setId(String id) {
        this.id = id;
        return this;
    }

    public String getPId() {
        return pId;
    }

    public BeeLink setPId(String pId) {
        this.pId = pId;
        return this;
    }

    public String getGId() {
        return gId;
    }

    public BeeLink setGId(String gId) {
        this.gId = gId;
        return this;
    }

    public String getCTag() {
        return cTag;
    }

    public BeeLink setCTag(String cTag) {
        this.cTag = cTag;
        return this;
    }

    public BeeLink addSpan(String key,Span span) {
        spanMap.put(key, span);
        return this;
    }
    public Span getSpan(String key) {
        return spanMap.get(key);
    }

}
