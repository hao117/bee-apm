package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.ServletAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuan on 2018/8/5.
 */
public class ServletPlugin implements IPlugin {
    @Override
    public ElementMatcher<TypeDescription> buildTypesMatcher() {
        return ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"));
    }

    @Override
    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
        return ElementMatchers.isMethod().and(ElementMatchers.takesArgument(0, HttpServletRequest.class))
                .and(ElementMatchers.takesArgument(1, HttpServletResponse.class));
    }

    @Override
    public Class adviceClass() {
        return ServletAdvice.class;
    }
}
