package net.beeapm.ui.common;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Map;

public class BeeUtils {
    public static Long getBeginTime(Map<String,Object> param){
        String beginTime = (String) param.get("beginTime");
        try {
            return DateUtils.parseDate(beginTime+":00.000", "yyyy-MM-dd HH:mm:ss.SSS").getTime();
        }catch (Exception e){
        }
        return 0L;
    }
    public static Long getEndTime(Map<String,Object> param){
        String endTime = (String) param.get("endTime");
        try {
            return DateUtils.parseDate(endTime+":59.999", "yyyy-MM-dd HH:mm:ss.SSS").getTime();
        }catch (Exception e){
        }
        return 0L;
    }

    public static String[] getIndices(String index,Map<String,Object> param){
        String beginTime = (String) param.get("beginTime");
        beginTime = beginTime.substring(0,10).replace("-",".");
        String endTime = (String) param.get("endTime");
        endTime = endTime.substring(0,10).replace("-",".");
        if(beginTime.equals(endTime)){
            return new String[]{index+beginTime};
        }
        return new String[]{index+beginTime,index+endTime};
    }
}
