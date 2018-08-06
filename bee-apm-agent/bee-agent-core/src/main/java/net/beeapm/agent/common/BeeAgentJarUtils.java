package net.beeapm.agent.common;

import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yuan on 2018/8/6.
 */
public class BeeAgentJarUtils {
    private static final LogImpl logger = LogManager.getLog(BeeAgentJarUtils.class);

    private static File agentJarFile;

    public static File getAgentJarFile() {
        if (agentJarFile == null) {
            agentJarFile = findPath();
        }
        return agentJarFile;
    }


    private static File findPath() {
        String classPath = BeeAgentJarUtils.class.getName().replaceAll("\\.", "/") + ".class";

        URL resource = BeeAgentJarUtils.class.getClassLoader().getSystemClassLoader().getResource(classPath);
        if (resource != null) {
            String urlString = resource.toString();
            int insidePathIndex = urlString.indexOf('!');
            boolean isInJar = insidePathIndex > -1;
            System.out.println(urlString);
        }

        logger.error("Can not locate agent jar file.");
        return null;
    }
}
