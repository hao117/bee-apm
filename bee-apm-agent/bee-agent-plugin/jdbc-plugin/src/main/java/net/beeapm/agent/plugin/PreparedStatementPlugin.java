package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.PreparedStatementAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.sql.PreparedStatement;

public class PreparedStatementPlugin implements IPlugin {
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
                        return ElementMatchers.isSubTypeOf(PreparedStatement.class)
                                .and(ElementMatchers.not(ElementMatchers.isInterface()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>isAbstract()));
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.<MethodDescription>nameStartsWith("set").and(ElementMatchers.takesArgument(0,int.class)))
                                .or(ElementMatchers.isMethod().and(ElementMatchers.<MethodDescription>nameStartsWith("execute")));
                    }
                }
        };
    }

    @Override
    public Class interceptorAdviceClass() {
        return PreparedStatementAdvice.class;
    }
}
