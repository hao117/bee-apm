package net.beeapm.agent.model;

/**
 * @author yuan
 * @date 2020/05/13
 */
public class TraceContextModel {
    private String pid;
    private String gid;
    private String cTag;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getCTag() {
        return cTag;
    }

    public void setCTag(String cTag) {
        this.cTag = cTag;
    }

    public TraceContextModel copy() {
        TraceContextModel model = new TraceContextModel();
        model.setCTag(this.cTag);
        model.setGid(this.gid);
        model.setPid(this.pid);
        return model;
    }
}
