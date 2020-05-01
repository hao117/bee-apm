package net.beeapm.agent.plugin;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * 拦截点接口
 * @author yuan
 */
public interface InterceptPoint {
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
}
