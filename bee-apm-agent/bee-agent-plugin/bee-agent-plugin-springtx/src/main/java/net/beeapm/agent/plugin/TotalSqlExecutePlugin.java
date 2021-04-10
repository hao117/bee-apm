package net.beeapm.agent.plugin;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.interceptor.TotalSqlExecuteAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.sql.PreparedStatement;

/**
 * 事务中，sql执行次数
 *
 * @author yuan
 * @date 2018/08/14
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "totalSqlExecute")
public class TotalSqlExecutePlugin extends AbstractPlugin {

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.isSubTypeOf(PreparedStatement.class)
                                .and(ElementMatchers.not(ElementMatchers.isInterface()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>isAbstract()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith("com.sun.proxy.")));

                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.<MethodDescription>nameStartsWith("execute"))
                                .and(ElementMatchers.<MethodDescription>isPublic())
                                .and(ElementMatchers.not(ElementMatchers.<MethodDescription>named("executeInternal")));
                    }
                }
        };
    }

    @Override
    public Class interceptorAdviceClass() {
        return TotalSqlExecuteAdvice.class;
    }
}
