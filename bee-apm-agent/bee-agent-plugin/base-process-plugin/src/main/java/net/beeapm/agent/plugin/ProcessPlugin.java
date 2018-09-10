package net.beeapm.agent.plugin;

import net.beeapm.agent.common.MatchKit;
import net.beeapm.agent.config.ConfigUtils;
import net.beeapm.agent.plugin.interceptor.ProcessAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;
import java.util.Map;

/**
 * Created by yuan on 2018/7/31.
 */
public class ProcessPlugin implements IPlugin {

    @Override
    public String getName() {
        return "process";
    }

    public InterceptPoint[] buildInterceptPoint(){
        List<Map<String,Map<String,Map<String,String>>>> list = (List<Map<String,Map<String,Map<String,String>>>>)ConfigUtils.me().getVal("plugins.process.interceptPoints");
        if(list == null || list.isEmpty()) {
            return new InterceptPoint[]{
                    new InterceptPoint() {
                        @Override
                        public ElementMatcher<TypeDescription> buildTypesMatcher() {
                            return MatchKit.buildTypesMatcher(null,null)
                                    .and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"))));
                        }

                        @Override
                        public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                            return MatchKit.buildMethodsMatcher(null,null);
                        }
                    }
            };
        }
        InterceptPoint[] points = new InterceptPoint[list.size()];
        for(int i = 0; i < list.size(); i++){
            Map<String,Map<String,Map<String,String>>> item = list.get(i);
            if(item == null || item.isEmpty()){
                continue;
            }
            final Map<String,Map<String,String>> typeMatch = item.get("typeMatch");
            final Map<String,Map<String,String>> methodMatch = item.get("methodMatch");
            points[i] =  new InterceptPoint() {
                @Override
                public ElementMatcher<TypeDescription> buildTypesMatcher() {
                    if(typeMatch == null) {
                        return MatchKit.buildTypesMatcher(null, null)
                                .and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"))));
                    }
                    return MatchKit.buildTypesMatcher(typeMatch.get("include"), typeMatch.get("exclude"))
                            .and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"))));
                }

                @Override
                public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                    if(methodMatch == null) {
                        return MatchKit.buildMethodsMatcher(null, null);
                    }
                    return MatchKit.buildMethodsMatcher(methodMatch.get("include"), methodMatch.get("exclude"));
                }
            };
        }
        return points;
    }

    @Override
    public Class interceptorAdviceClass() {
        return ProcessAdvice.class;
    }

}
