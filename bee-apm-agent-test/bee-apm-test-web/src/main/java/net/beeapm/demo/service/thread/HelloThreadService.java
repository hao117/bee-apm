package net.beeapm.demo.service.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.Reflection;

/**
 * @author yuan
 * @date 2020/05/29
 */
public class HelloThreadService {
    private Logger logger = LoggerFactory.getLogger(HelloThreadService.class);

    public String sayHelloThread3(String str) {
        logger.debug("日志输出测试: {}", "日志内容");
        (new Throwable()).getStackTrace();
        Reflection.getCallerClass();
        Throwable throwable = new Throwable();
        throwable.getStackTrace();
        Thread.currentThread().getStackTrace();
        System.out.println("sayHelloThread = " + str);
        return "<good " + str + ">";
    }
}
