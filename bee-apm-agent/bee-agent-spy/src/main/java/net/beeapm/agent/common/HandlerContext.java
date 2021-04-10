package net.beeapm.agent.common;

/**
 * @author yuanlong.chen
 * @date 2021/04/09
 */
public class HandlerContext {
    private static ThreadLocal<ClassLoader> local = new ThreadLocal<>();

    public static void set(ClassLoader classLoader) {
        local.set(classLoader);
    }

    public static ClassLoader get() {
        return local.get();
    }

    public static void remove() {
        local.remove();
    }
}
