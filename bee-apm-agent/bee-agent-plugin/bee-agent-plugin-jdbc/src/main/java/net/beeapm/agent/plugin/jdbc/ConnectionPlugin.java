package net.beeapm.agent.plugin.jdbc;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.common.PluginOrder;
import net.beeapm.agent.plugin.AbstractPlugin;
import net.beeapm.agent.plugin.InterceptPoint;
import net.beeapm.agent.plugin.jdbc.interceptor.ConnectionAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author yuan
 * @date 2018/08/14
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "jdbc-connection")
public class ConnectionPlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "jdbc-connection";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.isSubTypeOf (java.sql.Connection.class)
                                .and(ElementMatchers.not(ElementMatchers.isInterface()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>isAbstract()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith("com.sun")));
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.<MethodDescription>named("prepareStatement")
                                        .or(ElementMatchers.<MethodDescription>named("prepareCall"))
                        );
                    }
                }
        };
    }

    @Override
    public Class interceptorAdviceClass() {
        return ConnectionAdvice.class;
    }

    @Override
    public int order(){
        return PluginOrder.CONNECTION_PLUGIN;
    }
}
