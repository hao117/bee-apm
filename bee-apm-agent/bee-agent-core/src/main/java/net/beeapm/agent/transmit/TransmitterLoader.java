package net.beeapm.agent.transmit;

import net.beeapm.agent.common.AgentClassLoader;
import net.beeapm.agent.model.TransmitterDefine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class TransmitterLoader {
    public static Map<String,AbstractTransmitter> loadTransmitters() {
        Map<String,AbstractTransmitter> transmitterList = new HashMap<String,AbstractTransmitter>();
        List<TransmitterDefine> transmitterDefList = new ArrayList<TransmitterDefine>();
        AgentClassLoader classLoader = new AgentClassLoader(TransmitterLoader.class.getClassLoader(),new String[]{"transmit"});
        List<URL> resources = getResources(classLoader);
        for (URL url : resources) {
            try {
                readPluginDefine(url.openStream(),transmitterDefList);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        for (TransmitterDefine define : transmitterDefList) {
            try {
                AbstractTransmitter transmitter = (AbstractTransmitter)Class.forName(define.clazz,true,classLoader).newInstance();
                transmitterList.put(define.name,transmitter);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return transmitterList;
    }

    private static List<URL> getResources(ClassLoader classLoader) {
        List<URL> cfgUrlPaths = new ArrayList<URL>();
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources("bee-transmit.def");
            while (urls.hasMoreElements()) {
                URL pluginUrl = urls.nextElement();
                cfgUrlPaths.add(pluginUrl);
            }
            return cfgUrlPaths;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void readPluginDefine(InputStream input,List<TransmitterDefine> pluginDefList) throws IOException {
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
                    pluginDefList.add(new TransmitterDefine(defs[0],defs[1]));
                } catch (Exception e) {
                }
            }
        } finally {
            input.close();
        }
    }


}
