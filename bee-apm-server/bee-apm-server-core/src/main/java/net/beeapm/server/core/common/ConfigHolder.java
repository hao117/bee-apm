package net.beeapm.server.core.common;

import org.springframework.core.env.Environment;

public class ConfigHolder {
    static Environment env;
    public static void  setEnv(Environment env){
        env = env;
    }

    public static Environment getEnv(){
        return env;
    }

    public static String getProperty(String key){
        return env.getProperty(key);
    }

    public static String getProperty(String key,String defVal){
        return env.getProperty(key,defVal);
    }

    public static int getPropInt(String key,int defVal){
        return Integer.parseInt(getProperty(key,defVal+""));
    }

    public static String getProperty(String prefix,String key,String defVal){
        return getProperty(prefix+key,defVal);
    }
    public static int getPropInt(String prefix,String key,int defVal){
        return getPropInt(prefix+key,defVal);
    }



}
