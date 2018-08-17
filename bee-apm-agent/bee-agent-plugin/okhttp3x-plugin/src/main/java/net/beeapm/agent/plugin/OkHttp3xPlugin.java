package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.OkHttp3xAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Created by yuan on 2018/8/14.
 */
public class OkHttp3xPlugin implements IPlugin {

    @Override
    public String getName() {
        return "okhttp3x";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.named("okhttp3.Request");

                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isConstructor();
                    }
                }
        };
    }


    @Override
    public Class interceptorAdviceClass() {
        return OkHttp3xAdvice.class;
    }
}
