package net.beeapm.agent.plugin;

/**
 * Created by yuan on 2018/3/28.
 */
public interface IPlugin {
    String getName();

    InterceptPoint[] buildInterceptPoint();

    /**
     * 拦截器类名称
     * @return
     */
    Class interceptorAdviceClass();
}
