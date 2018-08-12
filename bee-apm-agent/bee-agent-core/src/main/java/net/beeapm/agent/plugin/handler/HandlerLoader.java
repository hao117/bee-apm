package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.AgentClassLoader;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuan on 2018/7/31.
 */
public class HandlerLoader {
    private static final LogImpl log = LogManager.getLog(HandlerLoader.class.getSimpleName());
    private static ConcurrentHashMap<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();
    private static ReentrantLock INSTANCE_LOAD_LOCK = new ReentrantLock();
    private static AgentClassLoader beeClassLoader;

    private static AgentClassLoader getBeeClassLoader(ClassLoader parentClassLoader){
        if(beeClassLoader == null){
            synchronized (HandlerLoader.class){
                if(beeClassLoader == null){
                    beeClassLoader = new AgentClassLoader(parentClassLoader,new String[]{"plugins"});
                }
            }
        }
        return beeClassLoader;
    }

    public static Object load(String className){
        try {
            ClassLoader contextClassLoader =  Thread.currentThread().getContextClassLoader();
            ClassLoader classLoader = getBeeClassLoader(contextClassLoader);
            String instanceKey = className + "_OF_" + classLoader.getClass().getName() + "@" + Integer.toHexString(classLoader.hashCode());
            Object inst = handlerMap.get(instanceKey);
            if (inst == null) {
                INSTANCE_LOAD_LOCK.lock();
                try {
                    inst = Class.forName(className, true, classLoader).newInstance();
                } finally {
                    INSTANCE_LOAD_LOCK.unlock();
                }
                if (inst != null) {
                    handlerMap.put(instanceKey, inst);
                }
            }
            return inst;
        }catch (IllegalAccessException e){
            log.warn("",e);
        }catch (InstantiationException e){
            log.warn("",e);
        }catch (ClassNotFoundException e){
            log.warn("",e);
        }catch (Throwable t){
            log.warn("",t);
        }
        return new EmptyHandler();
    }
}
