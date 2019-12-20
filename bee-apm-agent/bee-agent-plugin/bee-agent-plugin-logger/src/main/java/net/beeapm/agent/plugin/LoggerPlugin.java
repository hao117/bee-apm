package net.beeapm.agent.plugin;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.interceptor.LoggerAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
/**
 * @author yuan
 * @date 2018/08/19
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "logger")
public class LoggerPlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "logger";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.named("org.apache.log4j.Logger")
                                .or(ElementMatchers.<TypeDescription>named("org.apache.log4j.Category"))
                                .or(ElementMatchers.<TypeDescription>named("org.apache.log4j.spi.NOPLogger"))
                                .or(ElementMatchers.<TypeDescription>named("org.apache.logging.log4j.spi.AbstractLogger"))
                                .or(ElementMatchers.<TypeDescription>named("ch.qos.logback.classic.Logger"))
                                .or(ElementMatchers.<TypeDescription>named("org.slf4j.helpers.NOPLogger"));
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod().and(
                                ElementMatchers.<MethodDescription>named("trace")
                                        .or(ElementMatchers.<MethodDescription>named("debug"))
                                        .or(ElementMatchers.<MethodDescription>named("info"))
                                        .or(ElementMatchers.<MethodDescription>named("warn"))
                                        .or(ElementMatchers.<MethodDescription>named("error"))
                                        .or(ElementMatchers.<MethodDescription>named("fatal"))
                        );
                    }
                }
        };
    }

    @Override
    public Class interceptorAdviceClass() {
        return LoggerAdvice.class;
    }
}
