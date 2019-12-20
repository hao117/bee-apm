package net.beeapm.agent.reporter;

import net.beeapm.agent.common.AgentClassLoader;
import net.beeapm.agent.model.ReporterDefine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
/**
 *
 * @author yuan
 * @date 2018/10/20
 */
public class ReporterLoader {
    public static Map<String, AbstractReporter> loadreporters() {
        Map<String, AbstractReporter> reporterMap = new HashMap<String, AbstractReporter>();
        List<ReporterDefine> reporterDefList = new ArrayList<ReporterDefine>();
        AgentClassLoader classLoader = new AgentClassLoader(ReporterLoader.class.getClassLoader(),new String[]{"reporter"});
        List<URL> resources = getResources(classLoader);
        for (URL url : resources) {
            try {
                readPluginDefine(url.openStream(),reporterDefList);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        for (ReporterDefine define : reporterDefList) {
            try {
                AbstractReporter reporter = (AbstractReporter)Class.forName(define.clazz,true,classLoader).newInstance();
                reporterMap.put(define.name,reporter);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return reporterMap;
    }

    private static List<URL> getResources(ClassLoader classLoader) {
        List<URL> cfgUrlPaths = new ArrayList<URL>();
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources("bee-reporter.def");
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

    private static void readPluginDefine(InputStream input,List<ReporterDefine> pluginDefList) throws IOException {
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
                    pluginDefList.add(new ReporterDefine(defs[0],defs[1]));
                } catch (Exception e) {
                }
            }
        } finally {
            input.close();
        }
    }


}
