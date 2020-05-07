package net.beeapm.agent.common;


import net.beeapm.agent.log.Log;
import net.beeapm.agent.log.LogFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * Created by yuan on 2018/4/11.
 */
public class BeeClassLoader extends URLClassLoader{
    private static BeeClassLoader DEFAULT_LOADER;
    private static final Log log = LogFactory.getLog(BeeClassLoader.class.getSimpleName());
    private JarFile agentJar;
    private ClassLoader parent;

    public BeeClassLoader(ClassLoader parent) {
//        super(new URL[]{BeeClassLoader.class.getClassLoader().getResource("")},parent);
//        this.parent = parent;
        super(new URL[]{url()},parent);
        loadAgentJar();
    }
//
//    public BeeClassLoader(ClassLoader parent) {
//        this(parent);
//    }

    private static URL url(){
        try{
            return new URL("file:"+BeeAgentJarUtils.getAgentJarPath());
        }catch (Exception e){

        }
        return null;
    }

    private void loadAgentJar(){
        try {
            agentJar = new JarFile(new File(BeeAgentJarUtils.getAgentJarPath()));
        }catch (Exception e){

        }
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
                c = findClass(name);
            }catch (Throwable t){
                try {
                    c = super.loadClass(name,resolve);
                }catch (Exception e){

                }
            }
        }
        return c;
    }


}
