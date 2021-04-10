package net.beeapm.demo.controller;

import net.beeapm.demo.service.HelloServiceImpl;
import net.beeapm.demo.service.IHelloService;
import net.beeapm.demo.service.test.FooAaa;
import net.beeapm.demo.service.thread.BeeFutureTask;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yuan on 2018/8/2.
 */
@WebServlet(urlPatterns = {"/hello"})
public class HelloServlet extends HttpServlet{
    private static ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
        private final AtomicInteger threadCounter = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("bee-thread-demo" + threadCounter.incrementAndGet());
            t.setDaemon(true);
            return t;
        }
    });
    public void testTask(){
        System.out.println("---");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        String context = request.getParameter("context");
        System.out.println("     ----------HelloServlet-context:"+context);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            BeeFutureTask task = new BeeFutureTask(new Callable(){
                @Override
                public Object call() {
                    System.out.println("--------------BeeFutureTask------------");
                    FooAaa f = new FooAaa();
                    f.speak();
                    return null;
                }
            });
            executorService.submit(task);
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
//            System.out.println(response.getOutputStream().toString());
////
//            response.getOutputStream();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            String body = baos.toString();
//            System.out.println("====================="+body);
//            body = "<!DOCTYPE html><html><head></head><body><p>Hello World !</p><p>11111111111</p></body></html>";
//            response.getOutputStream().write(body.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        this.doGet(request, response);
    }
}
