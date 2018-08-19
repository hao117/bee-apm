package net.beeapm.demo.controller;


import ch.qos.logback.classic.LoggerContext;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.slf4j.impl.StaticLoggerBinder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by yuan on 2018/8/2.
 */
@WebServlet(urlPatterns = {"/logger"})
public class LoggerTestServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        String context = request.getParameter("context");
        System.out.println("=================================>DbTestServlet");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            testLog4j();
            testLog4j2();
            testLogback();
            PrintWriter writer = response.getWriter();
            writer.append("Good!");
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        this.doGet(request, response);
    }

    private void testLog4j(){
        BasicConfigurator.configure();
        Logger logger = LogManager.getLogger(LoggerTestServlet.class);
        logger.setLevel(Level.ALL);
        Throwable t1 = new Exception("test1----------------Exception");
        Throwable t2 = new Exception("test2----------------Exception");
        logger.trace("Log4j==================>trace");
        logger.debug("Log4j==================>debug");
        logger.info("Log4j==================>info");
        logger.warn("Log4j==================>warn");
        logger.error("Log4j==================>error",t1);
        logger.fatal("Log4j==================>fatal",t2);
    }

    private void testLog4j2(){
        org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(LoggerTestServlet.class);
        Throwable t1 = new Exception("test1----------------Exception");
        Throwable t2 = new Exception("test2----------------Exception");
        logger.trace("Log4j2==================>trace");
        logger.debug("Log4j2==================>debug");
        logger.info("Log4j2==================>info");
        logger.warn("Log4j2==================>warn");
        logger.error("Log4j2==================>error",t1);
        logger.fatal("Log4j2==================>fatal",t2);
    }

    private void testLogback(){
        LoggerContext loggerFactory = (LoggerContext)StaticLoggerBinder.getSingleton().getLoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerFactory.getLogger(LoggerTestServlet.class);
        Throwable t = new Exception("test----------------Exception");
        logger.setLevel(ch.qos.logback.classic.Level.ALL);
        logger.trace("Logback==================>trace");
        logger.debug("Logback==================>debug");
        logger.info("Logback==================>info");
        logger.warn("Logback==================>warn");
        logger.error("Logback==================>error",t);
    }
}
