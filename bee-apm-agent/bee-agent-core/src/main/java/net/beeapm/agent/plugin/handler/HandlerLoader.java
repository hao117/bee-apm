package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeClassLoader;

import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by yuan on 2018/7/31.
 */
public class HandlerLoader {
    private static ConcurrentHashMap<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();
    private static ReentrantLock INSTANCE_LOAD_LOCK = new ReentrantLock();
    private static BeeClassLoader beeClassLoader;

    private static BeeClassLoader getBeeClassLoader(URLClassLoader parentClassLoader){
        if(beeClassLoader == null){
            synchronized (HandlerLoader.class){
                if(beeClassLoader == null){
                    beeClassLoader = new BeeClassLoader(parentClassLoader);
                }
            }
        }
        return beeClassLoader;
    }

    public static Object load(String className, ClassLoader targetClassLoader){
        try {
            BeeClassLoader classLoader = getBeeClassLoader((URLClassLoader) targetClassLoader);
            String instanceKey = className + "_OF_" + classLoader.getClass().getName() + "@" + Integer.toHexString(classLoader.hashCode());
            Object inst = handlerMap.get(instanceKey);
            if (inst == null) {
                INSTANCE_LOAD_LOCK.lock();
                try {
                    inst = Class.forName(className, true, classLoader).newInstance();
                    //inst = (IHandler)obj;
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
