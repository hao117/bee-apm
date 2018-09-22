package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.SqlExecuteCountAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.sql.PreparedStatement;

/**
 * 事务中，sql执行次数
 */
public class SqlExecuteCountPlugin implements IPlugin {
    @Override
    public String getName() {
        return "springTxSqlCount";
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
                                .and(ElementMatchers.<MethodDescription>nameStartsWith("execute"))
                                .and(ElementMatchers.<MethodDescription>isPublic())
                                .and(ElementMatchers.not(ElementMatchers.<MethodDescription>named("executeInternal")));
                    }
                }
        };
    }

    @Override
    public Class interceptorAdviceClass() {
        return SqlExecuteCountAdvice.class;
    }
}
