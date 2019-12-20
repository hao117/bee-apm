package net.beeapm.agent.plugin;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.interceptor.HttpClient4xAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.http.client.HttpClient;

/**
 * @author yuan
 * @date 2018/08/14
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "httpclient4x")
public class HttpClient4xPlugin extends AbstractPlugin {

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
