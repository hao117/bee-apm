package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.MethodSpendAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Created by yuan on 2018/7/31.
 */
public class MethodSpendPlugin implements IPlugin {

    @Override
    public String getName() {
        return "method";
    }

    public InterceptPoint[] buildInterceptPoint(){
        return new InterceptPoint[]{
              new InterceptPoint() {
                  @Override
                  public ElementMatcher<TypeDescription> buildTypesMatcher() {
                      return ElementMatchers.nameStartsWith("net.beeapm.demo")
                              .and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"))));
                  }

                  @Override
                  public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                      return ElementMatchers.isMethod();
                  }
              },
        };
    }

    @Override
    public Class interceptorAdviceClass() {
        return MethodSpendAdvice.class;
    }

}
