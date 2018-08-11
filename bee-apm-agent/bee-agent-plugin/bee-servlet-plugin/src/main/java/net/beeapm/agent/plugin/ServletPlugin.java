package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.ServletAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Created by yuan on 2018/8/5.
 */
public class ServletPlugin implements IPlugin {

    @Override
    public String getName() {
        return "servlet";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"));
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.takesArgument(0, ElementMatchers.named("javax.servlet.http.HttpServletRequest")))
                                .and(ElementMatchers.takesArgument(1,ElementMatchers.named("javax.servlet.http.HttpServletResponse")));
                    }
                }
        };
    }


    @Override
    public Class interceptorAdviceClass() {
        return ServletAdvice.class;
    }
}
