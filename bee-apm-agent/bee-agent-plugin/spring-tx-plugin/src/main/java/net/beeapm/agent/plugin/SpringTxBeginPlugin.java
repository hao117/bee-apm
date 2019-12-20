package net.beeapm.agent.plugin;


import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.interceptor.SpringTxBeginAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author kaddddd
 * @date 2018/08/22
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "springTxBegin")
public class SpringTxBeginPlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "springTxBegin";
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
                                .and(ElementMatchers.<MethodDescription>named("doBegin")
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
