package net.beeapm.agent.model;

/**
 * 各个内存分区大小
 *
 * @author yuan
 * @date 2020/01/30
 */
public class MemorySpaceInfo {
    /**
     * 年轻代大小 Eden Space + Survivor Spaces
     */
    private long youngSize = 0;

    /**
     * 老年代大小 Old Gen
     */
    private long oldSize = 0;

    /**
     * 永久代（元空间）大小
     */
    private long permGenSize = 0;

    public long getYoungSize() {
        return youngSize;
    }

    public void setYoungSize(long youngSize) {
        this.youngSize = youngSize;
    }

    public long getOldSize() {
        return oldSize;
    }

    public void setOldSize(long oldSize) {
        this.oldSize = oldSize;
    }

    public long getPermGenSize() {
        return permGenSize;
    }

    public void setPermGenSize(long permGenSize) {
        this.permGenSize = permGenSize;
    }
}
