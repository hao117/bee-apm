package net.beeapm.demo.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by yuan on 2018/8/7.
 */
public class MyFilter  implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("==========doFilter=============");
    }

    @Override
    public void destroy() {

    }
}
