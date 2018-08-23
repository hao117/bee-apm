package net.beeapm.agent.plugin;


import net.beeapm.agent.plugin.interceptor.TxAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class TxPlugin implements IPlugin {
    //@Override
    public String getName() {
        return "tx";
    }

    //@Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    //@Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.isSubTypeOf (org.springframework.transaction.PlatformTransactionManager.class)
                                .and(ElementMatchers.not(ElementMatchers.isInterface()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>isAbstract()));
                    }

                    //@Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.<MethodDescription>named("doBegin")
                                        .or(ElementMatchers.<MethodDescription>named("doCleanupAfterCompletion"))
                        );
                    }
                }
        };
    }

    //@Override
    public Class interceptorAdviceClass() {
        return TxAdvice.class;
    }
}
