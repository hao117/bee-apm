package net.beeapm.agent.plugin;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.interceptor.HttpClient3xAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author yuan
 * @date 2018/08/16
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "httpclient3x")
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
