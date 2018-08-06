package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.model.Span;
import java.lang.reflect.Method;

/**
 * Created by yuan on 2018/8/5.
 */
public class ServletHandler extends AbstractHandler {
    @Override
    public Span before(Method method, Object[] allArguments) {
        return null;
    }

    @Override
    public Object after(Span span, Method method, Object[] allArguments, Object result, Throwable t) {
        return null;
    }
}
