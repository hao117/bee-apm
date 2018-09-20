package net.beeapm.agent.plugin;


import net.beeapm.agent.plugin.interceptor.SpringTxBeginAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class SpringTxEndPlugin implements IPlugin {
    @Override
    public String getName() {
        return "springTxEnd";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.named("org.springframework.jdbc.datasource.DataSourceTransactionManager");
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.<MethodDescription>named("doCleanupAfterCompletion")
                        );
                    }
                }
        };
    }

    @Override
    public Class interceptorAdviceClass() {
        return SpringTxBeginAdvice.class;
    }
}
