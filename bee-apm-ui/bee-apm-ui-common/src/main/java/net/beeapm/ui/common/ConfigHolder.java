package net.beeapm.ui.common;

import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ConfigHolder {
    private static Properties properties = new Properties();
    public static void  buildConfig(ConfigurableEnvironment environment){
        MutablePropertySources mps = environment.getPropertySources();
        Iterator<PropertySource<?>> iterator = mps.iterator();
        while (iterator.hasNext()){
            PropertySource ps = iterator.next();
            if(ps.getName().startsWith("applicationConfig")){
                Map<String, OriginTrackedValue> source = (Map<String, OriginTrackedValue>)ps.getSource();
                if(source != null) {
                    Iterator valIt = source.entrySet().iterator();
                    while (valIt.hasNext()) {
                        Map.Entry<String, OriginTrackedValue> entry = (Map.Entry<String, OriginTrackedValue>) valIt.next();
                        properties.put(entry.getKey(),entry.getValue().getValue());
                    }
                }
            }
        }
    }

    public static String getProperty(String key){
        Object val = properties.get(key);
        if(val == null){
            return null;
        }
        return val.toString();
    }

    public static String getProperty(String key,String defVal){
        String val = getProperty(key);
        if(val == null){
            return defVal;
        }
        return val;
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

    public static Properties getProperties(String prefix,boolean trim){
        Properties props = new Properties();
        Iterator iterator = properties.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,Object> entry = (Map.Entry<String,Object>)iterator.next();
            if(entry.getKey().startsWith(prefix)){
                if(trim) {
                    props.put(entry.getKey().substring(prefix.length()+1), entry.getValue());
                }else{
                    props.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return props;
    }

    public static Properties getAllProperties(){
        return  properties;
    }



}
