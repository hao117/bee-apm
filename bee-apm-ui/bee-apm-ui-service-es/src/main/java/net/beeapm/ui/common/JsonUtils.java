package net.beeapm.ui.common;

import com.alibaba.fastjson.JSON;

/**
 * @author yuanlong.chen
 * @date 2021/04/20
 */
public class JsonUtils {
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }
}
