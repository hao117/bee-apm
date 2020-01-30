package net.beeapm.agent.model;

/**
 * 线程数量信息
 *
 * @author yuan
 * @date 2020/01/30
 */
public class ThreadInfo {
    /**
     * 线程总数
     */
    private int threadCount;
    /**
     * 守护线程数量
     */
    private int daemonThreadCount;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getDaemonThreadCount() {
        return daemonThreadCount;
    }

    public void setDaemonThreadCount(int daemonThreadCount) {
        this.daemonThreadCount = daemonThreadCount;
    }
}
