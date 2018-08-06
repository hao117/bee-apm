package net.beeapm.agent.common;


import net.beeapm.agent.plugin.handler.MethodSpendHandler;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by yuan on 2018/4/11.
 */
public class BeeClassLoader extends URLClassLoader{
    private static BeeClassLoader DEFAULT_LOADER;
    private static final LogImpl log = LogManager.getLog(MethodSpendHandler.class.getSimpleName());

    public BeeClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public BeeClassLoader(ClassLoader parent) {
        this(new URL[]{BeeClassLoader.class.getClassLoader().getResource("")}, parent);
    }

    protected synchronized Class loadClass(String name,boolean resolve) throws ClassNotFoundException{

        if(name.startsWith("net.beeapm.")){
            return myLoadClass(name,resolve);
        }else{
            return super.loadClass(name,resolve);
        }
    }

    protected synchronized Class myLoadClass(String name,boolean resolve) throws ClassNotFoundException{
        Class c = findLoadedClass(name);
        if(c == null){
            try {
                return findClass(name);
            }catch (Throwable t){
                //第一次加载会报java.lang.NullPointerException at sun.net.util.URLUtil.urlNoFragString
                //其它会是ClassNotFoundException
                return super.loadClass(name,resolve);
            }
        }
        return c;
    }
}
