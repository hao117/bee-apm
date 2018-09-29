package net.beeapm.ui.model;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.HashMap;

public class SevenKey extends HashMap {
    public SevenKey(int i,String k1,String k2,String k3,String k4,String k5,String k6,String k7){
        this.put(k1,DateFormatUtils.format(new Date(new Date().getTime() + (60*1000*i)),"HH:mm"));
        this.put(k2, RandomUtils.nextInt(300,500));
        this.put(k3, RandomUtils.nextInt(800,1000));
        this.put(k4, RandomUtils.nextInt(900,1500));
        this.put(k5, RandomUtils.nextInt(500,800));
        this.put(k6, RandomUtils.nextInt(400,800));
        this.put(k7, RandomUtils.nextInt(200,500));
        //this.put(k1,DateFormatUtils.format(new Date(new Date().getTime() + (60*1000*i)),"HH:mm") + "(" + (getKeyInt(k2)+getKeyInt(k3)+getKeyInt(k4)+getKeyInt(k5)+getKeyInt(k6)+getKeyInt(k7)) + ")");
    }
    public Integer getKeyInt(String key){
        return (Integer)get(key);
    }
}
