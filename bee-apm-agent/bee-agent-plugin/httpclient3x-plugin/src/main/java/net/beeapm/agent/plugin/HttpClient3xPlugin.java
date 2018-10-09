package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.HttpClient3xAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Created by yuan on 2018/8/16.
 */
public class HttpClient3xPlugin extends AbstractPlugin {

    @Override
    public String getName() {
        return "httpclient3x";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.hasSuperType(ElementMatchers.named("org.apache.commons.httpclient.HttpClient"));

                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod().and(ElementMatchers.<MethodDescription>named("executeMethod"));
                    }
                }
        };
    }


    @Override
    public Class interceptorAdviceClass() {
        return HttpClient3xAdvice.class;
    }
}
