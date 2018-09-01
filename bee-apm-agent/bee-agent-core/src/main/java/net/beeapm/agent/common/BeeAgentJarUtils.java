package net.beeapm.agent.common;

import java.io.File;
import java.net.URL;

/**
 * Created by yuan on 2018/8/6.
 */
public class BeeAgentJarUtils {

    private static String agentJarPath;
    private static String agentJarDirPath;

    public static String getAgentJarPath() {
        try {
            if (agentJarPath == null) {
                agentJarPath = findPath();
            }
            return agentJarPath;
        }catch (Exception e){
            e.printStackTrace();
            //logger.error("",e);
        }
        return null;
    }

    public static String getAgentJarDirPath() {
        try {
            if (agentJarDirPath == null) {
                File jarFile = new File(getAgentJarPath());
                if (jarFile.exists()) {
                    agentJarDirPath = jarFile.getParentFile().getCanonicalPath();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return agentJarDirPath;
    }


    private static String findPath() throws Exception{
        String classPath = BeeAgentJarUtils.class.getName().replaceAll("\\.", "/") + ".class";
        URL resource = BeeAgentJarUtils.class.getClassLoader().getSystemClassLoader().getResource(classPath);
        if (resource != null) {
            String path = resource.toString();
            int jarIndex = path.indexOf('!');
            if(jarIndex > 0){
                path = path.substring(path.indexOf("file:")+5, jarIndex);
            }else{
                return null;
            }
            File f = new File(path);
            if(f.exists()){
                path = f.getCanonicalPath();
            }
            System.out.println("agent jar path = " + path);
            return path;
        }
        return null;
    }
}
