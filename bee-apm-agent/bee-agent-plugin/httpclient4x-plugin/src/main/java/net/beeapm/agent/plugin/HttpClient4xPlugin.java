package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.HttpClient4xAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.http.client.HttpClient;

/**
 * Created by yuan on 2018/8/14.
 */
public class HttpClient4xPlugin implements IPlugin {

    @Override
    public String getName() {
        return "httpclient4x";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.hasSuperType(ElementMatchers.named("org.apache.http.client.HttpClient"))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>isInterface()));

                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod().and(ElementMatchers.<MethodDescription>named("execute"));
                    }
                }
        };
    }


    @Override
    public Class interceptorAdviceClass() {
        return HttpClient4xAdvice.class;
    }
}
