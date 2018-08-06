package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeClassLoader;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuan on 2018/7/31.
 */
public class HandlerLoader {
    private static ConcurrentHashMap<String, IHandler> handlerMap = new ConcurrentHashMap<String, IHandler>();
    private static ReentrantLock INSTANCE_LOAD_LOCK = new ReentrantLock();
    private static BeeClassLoader beeClassLoader;

    private static BeeClassLoader getBeeClassLoader(ClassLoader parentClassLoader){
        if(beeClassLoader == null){
            synchronized (HandlerLoader.class){
                if(beeClassLoader == null){
                    beeClassLoader = new BeeClassLoader(parentClassLoader);
                }
            }
        }
        return beeClassLoader;
    }

    public static IHandler load(String className, ClassLoader targetClassLoader){
        try {
            BeeClassLoader classLoader = getBeeClassLoader(targetClassLoader);
            String instanceKey = className + "_OF_" + classLoader.getClass().getName() + "@" + Integer.toHexString(classLoader.hashCode());
            IHandler inst = handlerMap.get(instanceKey);
            if (inst == null) {
                INSTANCE_LOAD_LOCK.lock();
                try {
                    inst = (IHandler) Class.forName(className, true, classLoader).newInstance();
                } finally {
                    INSTANCE_LOAD_LOCK.unlock();
                }
                if (inst != null) {
                    handlerMap.put(instanceKey, inst);
                }
            }
            return inst;
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (Throwable t){
            t.printStackTrace();
        }
        return new EmptyHandler();
    }

}
