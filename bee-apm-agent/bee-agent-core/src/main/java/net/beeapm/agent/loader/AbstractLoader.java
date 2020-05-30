package net.beeapm.agent.loader;

import net.beeapm.agent.common.AgentClassLoader;
import net.beeapm.agent.common.BeeUtils;
import net.beeapm.agent.log.LogUtil;
import net.beeapm.agent.model.SpiDefine;
import net.beeapm.agent.reporter.ReporterLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * @author yuan
 * @date 2018/10/20
 */
public abstract class AbstractLoader {

    public static <T> Map<String, T> load(String[] jarFolder, String fileName) {
        Map<String, T> spiMap = new HashMap<String, T>(8);
        List<SpiDefine> spiDefList = new ArrayList<SpiDefine>(8);
        String rootPath = BeeUtils.getJarDirPath();
        AgentClassLoader classLoader = new AgentClassLoader(AbstractLoader.class.getClassLoader(), rootPath, jarFolder);
        List<URL> resources = getResources(classLoader, fileName);
        for (URL url : resources) {
            try {
                readSpiDefine(url.openStream(), spiDefList);
            } catch (Throwable t) {
                LogUtil.log("解析" + fileName + "文件异常", t);
            }
        }

        for (SpiDefine define : spiDefList) {
            try {
                T reporter = (T) Class.forName(define.clazz, true, classLoader).newInstance();
                spiMap.put(define.name, reporter);
            } catch (Throwable t) {
                LogUtil.log("类加载异常" + define.clazz, t);
            }
        }
        return spiMap;
    }

    private static List<URL> getResources(ClassLoader classLoader, String fileName) {
        List<URL> cfgUrlPaths = new ArrayList<URL>();
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources(fileName);
            while (urls.hasMoreElements()) {
                URL pluginUrl = urls.nextElement();
                cfgUrlPaths.add(pluginUrl);
            }
            return cfgUrlPaths;
        } catch (IOException e) {
            LogUtil.log("加载文件异常" + fileName, e);
        }
        return null;
    }

    private static void readSpiDefine(InputStream input, List<SpiDefine> spiDefList) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String define;
            while ((define = reader.readLine()) != null) {
                define = define.trim();
                try {
                    if (define.isEmpty() || define.startsWith("#")) {
                        continue;
                    }
                    define = define.trim();
                    String[] defs = define.split("=");
                    if (defs.length != 2) {
                        continue;
                    }
                    spiDefList.add(new SpiDefine(defs[0], defs[1]));
                } catch (Exception e) {
                    LogUtil.log("解析SPI定义异常" + define, e);
                }
            }
        } finally {
            input.close();
        }
    }


}
