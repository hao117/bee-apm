package net.beeapm.demo.controller;

import net.beeapm.demo.service.HelloServiceImpl;
import net.beeapm.demo.service.HttpClient4xService;
import net.beeapm.demo.service.IHelloService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by yuan on 2018/8/2.
 */
@WebServlet(urlPatterns = {"/http"})
public class HttpTestServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        String method = request.getParameter("method");
        System.out.println("     ----------HttpTestServlet-method:"+method);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            HttpClient4xService service = new HttpClient4xService();
            if("test".equals(method)){
                service.sendRequest();
                writer.append("Send OK!!!!");
            }else if("send".equals(method)){
                service.receiveMessage(request,response);
                writer.append("Receive OK!!!!");
            }
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
