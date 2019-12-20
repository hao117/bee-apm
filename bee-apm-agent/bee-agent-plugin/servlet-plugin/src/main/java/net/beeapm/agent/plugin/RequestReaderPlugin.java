package net.beeapm.agent.plugin;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.interceptor.RequestReaderAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author yuan
 * @date 2018/10/09
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "requestReader")
public class RequestReaderPlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "requestReader";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named("javax.servlet.http.HttpServletRequest"))
                                .and(ElementMatchers.not(ElementMatchers.isInterface()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>isAbstract()));


                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.<MethodDescription>named("getReader");

                    }
                }
        };
    }


    @Override
    public Class interceptorAdviceClass() {
        return RequestReaderAdvice.class;
    }

}
