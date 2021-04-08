package net.beeapm.agent.model;

import java.util.HashMap;

/**
 * @author yuan
 * @date 2018-08-06
 */
public class Attributes extends HashMap<String,Object> {
    public Attributes addAttribute(String key, Object val){
        this.put(key,val);
        return this;
    }
}
