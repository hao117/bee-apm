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
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
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
                        return ElementMatchers.not(ElementMatchers.isInterface())
                                .and(ElementMatchers.isSubTypeOf(ExecutorService.class)
                                        .or(ElementMatchers.isSubTypeOf(CompletionService.class)))
                                .and(ElementMatchers.not(ElementMatchers.nameStartsWith("org.apache.tomcat.")));
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.<MethodDescription>named("submit")
                                        .or(ElementMatchers.<MethodDescription>named("execute"))
                                        .or(ElementMatchers.<MethodDescription>named("schedule"))
                                        .or(ElementMatchers.<MethodDescription>named("scheduleAtFixedRate"))
                                        .or(ElementMatchers.<MethodDescription>named("scheduleWithFixedDelay"))
                                        .or(ElementMatchers.<MethodDescription>named("invoke")))
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
