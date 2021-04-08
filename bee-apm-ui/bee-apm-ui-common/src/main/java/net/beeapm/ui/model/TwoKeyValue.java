package net.beeapm.ui.model;

import java.util.HashMap;

public class TwoKeyValue extends HashMap {
    public TwoKeyValue(String k1,Object v1,String k2,Object v2){
        this.put(k1,v1);
        this.put(k2,v2);
    }
}
