package net.beeapm.agent.plugin;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * Created by yuan on 2018/3/28.
 */
public interface IPlugin {
    /**
     * 类匹配规则
     * @return
     */
    ElementMatcher<TypeDescription> buildTypesMatcher();
    /**
     * 方法匹配规则
     * @return
     */
    ElementMatcher<MethodDescription> buildMethodsMatcher();

    /**
     * 拦截器类名称
     * @return
     */
    Class adviceClass();
}
