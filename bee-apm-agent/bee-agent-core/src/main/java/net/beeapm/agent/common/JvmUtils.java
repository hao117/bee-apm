package net.beeapm.agent.common;

import net.beeapm.agent.model.GcInfo;
import net.beeapm.agent.model.HeapInfo;
import net.beeapm.agent.model.MemorySpaceInfo;
import net.beeapm.agent.model.ThreadInfo;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuan
 * @date 2020/01/30
 */
public class JvmUtils {
    private static List<String> youngGenCollectorNames = new ArrayList<String>();
    private static List<String> oldGenCollectorNames = new ArrayList<String>();

    static {
        youngGenCollectorNames.add("Copy");
        youngGenCollectorNames.add("ParNew");
        youngGenCollectorNames.add("PS Scavenge");
        youngGenCollectorNames.add("G1 Young Generation");
        oldGenCollectorNames.add("MarkSweepCompact");
        oldGenCollectorNames.add("PS MarkSweep");
        oldGenCollectorNames.add("ConcurrentMarkSweep");
        oldGenCollectorNames.add("G1 Old Generation");
    }

    /**
     * gc次数和时间
     *
     * @return
     */
    public static GcInfo getGcInfo() {
        long oldCount = 0;
        long oldTime = 0;
        long youngCount = 0;
        long youngTime = 0;
        List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcs) {
            if (youngGenCollectorNames.contains(gc.getName())) {
                youngCount += gc.getCollectionCount();
                youngTime += gc.getCollectionTime();
            } else if (oldGenCollectorNames.contains(gc.getName())) {
                oldCount += gc.getCollectionCount();
                oldTime += gc.getCollectionTime();
            }
        }
        GcInfo report = new GcInfo();
        report.setYoungGcCount(youngCount);
        report.setOldGcCount(oldCount);
        report.setYoungGcTime(youngTime);
        report.setOldGcTime(oldTime);
        return report;
    }

    /**
     * 堆大小
     *
     * @return
     */
    public static HeapInfo getHeap() {
        MemoryMXBean memoryMxBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMxBean.getHeapMemoryUsage();
        HeapInfo heapInfo = new HeapInfo();
        heapInfo.setMax(memoryUsage.getMax());
        heapInfo.setUsed(memoryUsage.getUsed());
        return heapInfo;
    }

    /**
     * jvm内存空间大小
     * 包括年轻代、老年代、永久代（元空间）
     *
     * @return
     */
    public static MemorySpaceInfo getMemorySpaceInfo() {
        List<MemoryPoolMXBean> memoryPoolMxBeans = ManagementFactory.getMemoryPoolMXBeans();
        MemorySpaceInfo memorySpaceInfo = new MemorySpaceInfo();
        for (MemoryPoolMXBean bean : memoryPoolMxBeans) {
            String name = bean.getName();
            //jdk8及以后为Metaspace，jdk7及更早的为PermGen
            if (name.endsWith("Metaspace") || name.endsWith("Perm Gen")) {
                memorySpaceInfo.setPermGenSize(bean.getUsage().getUsed());
            } else if (name.endsWith("Survivor Space") || name.endsWith("Eden Space")) {
                memorySpaceInfo.setYoungSize(memorySpaceInfo.getYoungSize() + bean.getUsage().getUsed());
            } else if (name.endsWith("Old Gen")) {
                memorySpaceInfo.setOldSize(bean.getUsage().getUsed());
            }
        }
        return memorySpaceInfo;
    }

    /**
     * 线程数量（总数和守护线程数）
     *
     * @return
     */
    public static ThreadInfo getThreadInfo() {
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        ThreadInfo info = new ThreadInfo();
        info.setDaemonThreadCount(threadMxBean.getDaemonThreadCount());
        info.setThreadCount(threadMxBean.getThreadCount());
        return info;
    }

}
