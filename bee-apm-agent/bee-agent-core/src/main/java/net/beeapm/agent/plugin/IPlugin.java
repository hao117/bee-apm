package net.beeapm.agent.plugin;

/**
 * Created by yuan on 2018/3/28.
 */
public interface IPlugin {
    /**
     * 获取插件名称
     *
     * @return
     */
    String getName();

    /**
     * 设置插件名称
     *
     * @return
     */
    void setName(String name);

    /**
     * 构建拦截位置规则
     * @return
     */
    InterceptPoint[] buildInterceptPoint();

    /**
     * 拦截器类名称
     *
     * @return
     */
    Class interceptorAdviceClass();
}
