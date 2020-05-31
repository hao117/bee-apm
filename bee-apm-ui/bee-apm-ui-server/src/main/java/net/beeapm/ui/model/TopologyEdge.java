package net.beeapm.ui.model;

/**
 * @author yuan
 * @date 2020/01/14
 */
public class TopologyEdge {
    private String from;
    private String to;
    private Integer times;

    public TopologyEdge(int times){
        this.times = times;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @Override
    public int hashCode() {
        return key().hashCode();
    }

    public String key() {
        return from + "_" + to;
    }
}
