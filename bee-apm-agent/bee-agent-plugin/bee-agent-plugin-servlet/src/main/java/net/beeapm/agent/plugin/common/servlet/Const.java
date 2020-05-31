package net.beeapm.agent.plugin.common.servlet;

import net.beeapm.agent.plugin.common.BeeHttpServletRequestWrapper;
import net.beeapm.agent.plugin.common.BeeHttpServletResponseWrapper;

/**
 * @author yuan
 * @date 2019/12/27
 */
public class Const {
    public static final String KEY_RESP_WRAPPER = "_respWrapper";
    public static final String KEY_REQ_WRAPPER = "_reqWrapper";
    public static final String CLASS_BEE_HTTP_SERVLET_REQUEST_RAPPER = BeeHttpServletRequestWrapper.class.getSimpleName();
    public static final String CLASS_BEE_HTTP_SERVLET_RESPONSE_WRAPPER = BeeHttpServletResponseWrapper.class.getSimpleName();
}
