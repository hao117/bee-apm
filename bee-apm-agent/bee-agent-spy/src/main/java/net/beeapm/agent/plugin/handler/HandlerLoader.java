package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.AgentClassLoader;
import net.beeapm.agent.common.HandlerContext;
import net.beeapm.agent.log.LogUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 插件handler加载
 *
 * @author yuan
 * @date 2018/7/31
 */
public class HandlerLoader {
    private static ConcurrentHashMap<String, IHandler> handlerMap = new ConcurrentHashMap<String, IHandler>();
    private static ReentrantLock INSTANCE_LOAD_LOCK = new ReentrantLock();
    private static final EmptyHandler EMPTY_HANDLER = new EmptyHandler();
    private static String rootPath;

    public static void init(String path) {
        rootPath = path;
    }


    public static IHandler load(String className) {
        try {
            String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
            ClassLoader targetClassLoader = HandlerContext.get();
            if (targetClassLoader == null) {
                targetClassLoader = Thread.currentThread().getContextClassLoader();
            }
            String instanceKey = className + "_OF_" + targetClassLoader.getClass().getName() + "@" + Integer.toHexString(targetClassLoader.hashCode());
            IHandler inst = handlerMap.get(instanceKey);
            if (inst == null) {
                synchronized (className.intern()) {
                    ClassLoader classLoader = new AgentClassLoader(targetClassLoader, rootPath, new String[]{"plugins"});
                    inst = (IHandler) Class.forName(className, true, classLoader).newInstance();
                }
                if (inst != null) {
                    handlerMap.put(instanceKey, inst);
                }
            }
            return inst;
        } catch (IllegalAccessException e) {
            LogUtil.log("plugin handler load error-IllegalAccessException", e);
        } catch (InstantiationException e) {
            LogUtil.log("plugin handler load error-InstantiationException", e);
        } catch (ClassNotFoundException e) {
            LogUtil.log("plugin handler load error-ClassNotFoundException", e);
        } catch (Throwable t) {
            LogUtil.log("plugin handler load error-Throwable", t);
        }
        return EMPTY_HANDLER;
    }
}
