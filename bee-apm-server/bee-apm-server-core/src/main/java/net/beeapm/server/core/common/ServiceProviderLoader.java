package net.beeapm.server.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class ServiceProviderLoader {
    private final static Logger logger = LoggerFactory.getLogger(ServiceProviderLoader.class);
    private Map<String,Object> serviceMap = new HashMap<>();

    private Map<String,String> defineMap = new HashMap<>();
    public ServiceProviderLoader(String defineFileName){
        List<URL> resources = getResources(ServiceProviderLoader.class.getClassLoader(),defineFileName);
        for (URL url : resources) {
            try {
                readDefine(url.openStream());
            } catch (Throwable t) {
                logger.error("",t);
            }
        }
    }

    public <T> T load(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object service = serviceMap.get(name);
        if(service != null){
            return (T)service;
        }
        String clazz = defineMap.get(name);
        if(clazz == null){
            return null;
        }
        service = Class.forName(clazz).newInstance();
        return (T)service;
    }



    private List<URL> getResources(ClassLoader classLoader,String defineFileName) {
        List<URL> cfgUrlPaths = new ArrayList<URL>();
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources(defineFileName);
            while (urls.hasMoreElements()) {
                URL pluginUrl = urls.nextElement();
                cfgUrlPaths.add(pluginUrl);
            }
            return cfgUrlPaths;
        } catch (IOException e) {
            logger.error("",e);
        }
        return null;
    }

    private void readDefine(InputStream input) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String define;
            while ((define = reader.readLine()) != null) {
                try {
                    if (define == null || define.trim().length() == 0 || define.startsWith("#")) {
                        continue;
                    }
                    define = define.trim();
                    String[] defs = define.split("=");
                    if (defs.length != 2) {
                        continue;
                    }
                    defineMap.put(defs[0],defs[1]);
                } catch (Exception e) {
                    logger.error("",e);
                }
            }
            reader.close();
        } finally {
            input.close();
        }
    }
}
