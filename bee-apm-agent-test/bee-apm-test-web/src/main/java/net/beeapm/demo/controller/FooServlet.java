package net.beeapm.demo.controller;

import net.beeapm.demo.service.HelloServiceImpl;
import net.beeapm.demo.service.IHelloService;
import net.beeapm.demo.service.test.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by yuan on 2018/8/2.
 */
@WebServlet(urlPatterns = {"/foo"})
public class FooServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            FooAaa a = new FooAaa();
            FooBbb b = new FooBbb();
            FooCcc c = new FooCcc();
            FooDdd d = new FooDdd();
            FooDddAaa da = new FooDddAaa();
            FooEee e = new FooEee();

            a.say();
            a.speak();
            a.talk();
            a.tell();

            b.say();
            b.speak();
            b.talk();
            b.tell();

            c.say();
            c.speak();
            c.talk();
            c.tell();

            d.say();
            d.speak();
            d.talk();
            d.tell();

            da.say();
            da.speak();
            da.talk();
            da.tell();

            e.say();
            e.speak();
            e.talk();
            e.tell();

            PrintWriter writer = response.getWriter();
            writer.append("<!DOCTYPE html>")
                    .append("<html><head></head><body>")
                    .append("<p>Hello World !</p>")
                    .append("</body></html>");
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
