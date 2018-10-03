package net.beeapm.agent.model;

import net.beeapm.agent.config.BeeConfig;

import java.util.Date;

public class Span{
    private Tags tags = new Tags();
    private String type;
    private Date time;
    private String server;
    private String cluster;
    private String pid;
    private String gid;
    private String id;
    private Long spend;
    private String port;
    private String ip;



    public Span(String spanType){
        setType(spanType);
        setTime(new Date());
        setIp(BeeConfig.me().getIp());
        setPort(BeeConfig.me().getPort());
        setServer(BeeConfig.me().getServer());
        setCluster(BeeConfig.me().getCluster());
    }

    public Tags getTags() {
        return tags;
    }

    public Span addTag(String key,Object val) {
        this.tags.put(key,val);
        return this;
    }

    public Span removeTag(String key){
        this.tags.remove(key);
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

    public String getServer() {
        return server;
    }

    public Span setServer(String server) {
        this.server = server;
        return this;
    }

    public String getCluster() {
        return cluster;
    }

    public Span setCluster(String cluster) {
        this.cluster = cluster;
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
}
