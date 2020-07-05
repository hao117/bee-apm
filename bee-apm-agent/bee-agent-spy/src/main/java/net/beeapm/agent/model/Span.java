package net.beeapm.agent.model;


import java.util.Date;

/**
 * @author yuan
 * @date 2018-08-06
 */
public class Span{
    private Tags tags;
    private String type;
    private Date time;
    private String inst;
    private String app;
    private String env;
    private String pid;
    private String gid;
    private String id;
    private Long spend;
    private String port;
    private String ip;

    public Span(String spanType){
        setType(spanType);
        setTime(new Date());
    }

    public Object getTag(String key){
        if(tags == null){
            return null;
        }
        return tags.get(key);
    }

    public Span addTag(String key,Object val) {
        if(tags == null){
            tags = new Tags();
        }
        this.tags.put(key,val);
        return this;
    }

    public Span removeTag(String key){
        if(tags != null){
            this.tags.remove(key);
        }
        return this;
    }

    public String getType() {
        return type;
    }

    public Span setType(String type) {
        this.type = type;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Span setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getInst() {
        return inst;
    }

    public Span setInst(String inst) {
        this.inst = inst;
        return this;
    }

    public String getApp() {
        return app;
    }

    public Span setApp(String app) {
        this.app = app;
        return this;
    }

    public String getPid() {
        return pid;
    }

    public Span setPid(String pid) {
        this.pid = pid;
        return this;
    }

    public String getGid() {
        return gid;
    }

    public Span setGid(String gid) {
        this.gid = gid;
        return this;
    }

    public String getId() {
        return id;
    }

    public Span setId(String id) {
        this.id = id;
        return this;
    }

    public Long getSpend() {
        return spend;
    }

    public void setSpend(Long spend) {
        this.spend = spend;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Tags getTags() {
        return tags;
    }

}
