package net.beeapm.agent.plugin.common.servlet;


/**
 * @author yuanlong.chen
 * @date 2021/04/28
 */
public class LocalCounter {
    private static final ThreadLocal<Integer> counter = new ThreadLocal<>();

    public static Integer incrementAndGet() {
        if (counter.get() == null) {
            counter.set(new Integer(1));
        } else {
            counter.set(counter.get() + 1);
        }
        return counter.get();
    }

    public static void remove() {
        counter.remove();
    }

}
