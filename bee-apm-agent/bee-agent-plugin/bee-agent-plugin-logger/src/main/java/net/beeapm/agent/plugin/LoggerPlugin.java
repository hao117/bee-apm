package net.beeapm.agent.plugin;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.interceptor.LoggerAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;

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
                        ElementMatcher.Junction<TypeDescription> matcher = null;
                        List<String> loggerClass = LoggerConfig.me().getLoggerClass();
                        for (int i = 0; i < loggerClass.size(); i++) {
                            if (matcher == null) {
                                matcher = ElementMatchers.named(loggerClass.get(i));
                                continue;
                            }
                            matcher = matcher.or(ElementMatchers.<TypeDescription>named(loggerClass.get(i)));
                        }
                        return matcher;
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
