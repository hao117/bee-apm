package net.beeapm.demo.common;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yuanlong.chen
 * @date 2020/01/12
 */
public class Config {
    public static List<String> getRemotePortList() {
        String portList = System.getProperty("remote.ports");
        List<String> list = new ArrayList<>(Arrays.asList(StringUtils.split(portList, "_")));
        list.remove(System.getProperty("server.port"));
        return list;
    }

    public static String getBaseUrl() {
        String ip = System.getProperty("server.ip", "127.0.0.1");
        List<String> ports = getRemotePortList();
        String port = ports.get(RandomUtils.nextInt(0, ports.size() - 1));
        return String.format("http://%s:%s", ip, port);
    }

    public static int maxCounter() {
        return Integer.parseInt(System.getProperty("max.counter"));
    }

    public static String appName() {
        return System.getProperty("bee.app");
    }

    public static String instName() {
        return System.getProperty("bee.inst");
    }

    public static String url() {
        StringBuilder sb = new StringBuilder();
        sb.append("/dog/blackDog");
        sb.append(",/dog/sayWangWang");
        sb.append(",/dog/sayHelloDog");
        sb.append(",/dog/sayGoodbyeDog");
        sb.append(",/duck/sayGaGa");
        sb.append(",/duck/twoWing");
        sb.append(",/duck/twoFoot");
        sb.append(",/duck/whiteDuck");
        sb.append(",/hello/sayGoodbye");
        sb.append(",/hello/sayHello");
        sb.append(",/hello/welcomeChina");
        sb.append(",/hello/welcomeXiaMen");
        List<String> methodList = Arrays.asList(StringUtils.split(sb.toString(), ","));
        String method = methodList.get(RandomUtils.nextInt(0, methodList.size() - 1));
        return getBaseUrl() + method;
    }
}
