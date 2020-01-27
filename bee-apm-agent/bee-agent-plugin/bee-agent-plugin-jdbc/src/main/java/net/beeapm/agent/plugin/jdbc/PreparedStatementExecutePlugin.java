package net.beeapm.agent.plugin.jdbc;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.common.PluginOrder;
import net.beeapm.agent.plugin.AbstractPlugin;
import net.beeapm.agent.plugin.InterceptPoint;
import net.beeapm.agent.plugin.jdbc.interceptor.PreparedStatementExecuteAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.sql.PreparedStatement;

/**
 * @author yuan
 * @date 2018/08/14
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "jdbc-statement-execute")
public class PreparedStatementExecutePlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "jdbc-statement-execute";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.isSubTypeOf(PreparedStatement.class)
                                .and(ElementMatchers.not(ElementMatchers.isInterface()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>isAbstract()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith("com.sun")));

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
        return PreparedStatementExecuteAdvice.class;
    }

    @Override
    public int order() {
        return PluginOrder.PREPARED_STATEMENT_EXECUTE_PLUGIN;
    }

}
