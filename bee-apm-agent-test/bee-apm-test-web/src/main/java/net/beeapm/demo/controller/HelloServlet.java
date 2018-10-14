package net.beeapm.demo.controller;

import net.beeapm.demo.service.HelloServiceImpl;
import net.beeapm.demo.service.IHelloService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by yuan on 2018/8/2.
 */
@WebServlet(urlPatterns = {"/hello"})
public class HelloServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        String context = request.getParameter("context");
        System.out.println("     ----------HelloServlet-context:"+context);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            System.out.println("----------------------------------->getParameterMap : "+request.getParameterMap());
            System.out.println("----------------------------------->getParameterMap : "+request.getParameterMap());
            request.getParameterMap();
            System.out.println("----------------------------------->getInputStream");
            request.getInputStream();
            System.out.println("----------------------------------->getInputStream");
            request.getInputStream();
            System.out.println("----------------------------------->getReader");
            request.getReader();
            IHelloService helloService = new HelloServiceImpl();
            helloService.sayHello(context);
            PrintWriter writer = response.getWriter();
            writer.append("<!DOCTYPE html>")
                    .append("<html><head></head><body>")
                    .append("<p>Hello World !</p><p>")
                    .append(context)
                    .append("</p></body></html>");
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
}
