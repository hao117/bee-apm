package net.beeapm.agent.model;

/**
 * gc信息
 *
 * @author yuan
 * @date 2020/01/30
 */
public class GcInfo {
    private long oldGcCount = 0;
    private long oldGcTime = 0;
    private long youngGcCount = 0;
    private long youngGcTime = 0;

    public long getOldGcCount() {
        return oldGcCount;
    }

    public void setOldGcCount(long oldGcCount) {
        this.oldGcCount = oldGcCount;
    }

    public long getOldGcTime() {
        return oldGcTime;
    }

    public void setOldGcTime(long oldGcTime) {
        this.oldGcTime = oldGcTime;
    }

    public long getYoungGcCount() {
        return youngGcCount;
    }

    public void setYoungGcCount(long youngGcCount) {
        this.youngGcCount = youngGcCount;
    }

    public long getYoungGcTime() {
        return youngGcTime;
    }

    public void setYoungGcTime(long youngGcTime) {
        this.youngGcTime = youngGcTime;
    }

}
