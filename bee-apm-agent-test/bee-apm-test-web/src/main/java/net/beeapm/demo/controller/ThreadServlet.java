package net.beeapm.demo.controller;

import net.beeapm.demo.service.thread.ThreadPoolService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuan on 2020/5/29.
 */
@WebServlet(urlPatterns = {"/thread"})
public class ThreadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {

            //ForkJoinTaskTestService testService = new ForkJoinTaskTestService();
            //testService.test();

            ThreadPoolService service = new ThreadPoolService();
            service.hello2("banana");
            //service.hello2("tom");

            StringBuilder out = new StringBuilder();

            out.append("<!DOCTYPE html>")
                    .append("<html><head></head><body>")
                    .append("<p>Hello World Thread Test !</p>")
                    .append("</body></html>");
            response.getOutputStream().println(out.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.doGet(request, response);
    }
}
