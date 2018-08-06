package net.beeapm.agent.model;

import java.util.Date;
import java.util.HashMap;


public class Span extends HashMap<String,Object> {
    public Span(String tc){
        put("tc",tc);
        setTime(new Date());
    }
    public String getId() {
        return (String) get("id");
    }
    public Span setId(String id) {
        put("id",id);
        return this;
    }

    public String getPid() {
        return (String) get("pid");
    }

    public Span setPid(String pid) {
        put("pid",pid);
        return this;
    }

    public String getGid() {
        return (String) get("gid");
    }

    public Span setGid(String gid) {
        put("gid",gid);
        return this;
    }

    public Long getSpend() {
        return (Long) get("spend");
    }

    public Span setSpend(Long spend) {
        put("spend",spend);
        return this;
    }

    public String getIp() {
        return (String) get("ip");
    }

    public Span setIp(String ip) {
        put("ip",ip);
        return this;
    }

    public String getServer() {
        return (String) get("server");
    }

    public Span setServer(String server) {
        put("server",server);
        return this;
    }

    public String getc() {
        return (String) get("tc");
    }

    /**
     * type code
     * @param tc
     * @return
     */
    public Span setTc(String tc) {
        put("tc",tc);
        return this;
    }
    public Date getTime() {
        return (Date) get("time");
    }

    public Span setTime(Date time) {
        put("time",time);
        return this;
    }

    public String getGroup() {
        return (String) get("group");
    }

    public Span setGroup(String group) {
        put("group",group);
        return this;
    }

    public Span setValue(String key,Object val){
        put(key,val);
        return this;
    }
}
