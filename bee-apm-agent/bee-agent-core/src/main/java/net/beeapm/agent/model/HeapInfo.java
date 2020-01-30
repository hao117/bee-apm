package net.beeapm.agent.model;

/**
 * 堆信息
 *
 * @author yuan
 * @date 2020/01/30
 */
public class HeapInfo {
    private long used;
    private long max;

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

}
