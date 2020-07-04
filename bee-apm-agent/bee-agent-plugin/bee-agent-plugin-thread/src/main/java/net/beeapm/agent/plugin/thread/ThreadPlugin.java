package net.beeapm.agent.plugin.thread;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.AbstractPlugin;
import net.beeapm.agent.plugin.InterceptPoint;
import net.beeapm.agent.plugin.thread.interceptor.ThreadAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinTask;


/**
 * @author yuan
 * @date 2020/05/28
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "thread")
public class ThreadPlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "thread";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.any()
                                .and(ElementMatchers.hasSuperType(ElementMatchers.named("java.util.concurrent.ExecutorService"))
                                        .or(ElementMatchers.hasSuperType(ElementMatchers.named("java.util.concurrent.CompletionService"))))
                                .and(ElementMatchers.not(ElementMatchers.isInterface()))
                                .and(ElementMatchers.not(ElementMatchers.isAbstract()))
                                .and(ElementMatchers.not(ElementMatchers.nameStartsWith("org.apache.tomcat.")));
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.named("submit")
                                        .or(ElementMatchers.named("execute"))
                                        .or(ElementMatchers.named("schedule"))
                                        .or(ElementMatchers.named("scheduleAtFixedRate"))
                                        .or(ElementMatchers.named("scheduleWithFixedDelay"))
                                        .or(ElementMatchers.named("invoke")))
                                .and(ElementMatchers.takesArgument(0, Runnable.class)
                                        .or(ElementMatchers.takesArgument(0, Callable.class))
                                        .or(ElementMatchers.takesArgument(0, ForkJoinTask.class))
                                );
                    }
                }
        };
    }

    @Override
    public Class interceptorAdviceClass() {
        return ThreadAdvice.class;
    }
}
