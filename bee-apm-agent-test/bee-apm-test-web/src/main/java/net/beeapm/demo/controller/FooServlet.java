package net.beeapm.demo.controller;


import net.beeapm.demo.service.test.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by yuan on 2018/8/2.
 */
@WebServlet(urlPatterns = {"/foo"})
public class FooServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {

            StringBuilder sb = new StringBuilder();
            InputStream is = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            System.out.println("=========================================" + sb.toString());


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

            c.say();

            d.say();

            da.say();

            e.say();


            StringBuilder out = new StringBuilder();

            out.append("<!DOCTYPE html>")
                    .append("<html><head></head><body>")
                    .append("<p>Hello World 1111 !</p>")
                    .append("</body></html>");
            response.getOutputStream().println(out.toString());
//            PrintWriter writer = response.getWriter();
//            writer.append(out.toString());
//            writer.flush();
//            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.doGet(request, response);
    }
}
