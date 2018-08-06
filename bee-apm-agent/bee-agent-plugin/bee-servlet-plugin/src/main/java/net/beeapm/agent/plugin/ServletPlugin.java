package net.beeapm.agent.plugin;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * Created by yuan on 2018/8/5.
 */
public class ServletPlugin implements IPlugin {
    @Override
    public ElementMatcher<TypeDescription> buildTypesMatcher() {
        return null;
    }

    @Override
    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
        return null;
    }

    @Override
    public Class adviceClass() {
        return null;
    }
}
