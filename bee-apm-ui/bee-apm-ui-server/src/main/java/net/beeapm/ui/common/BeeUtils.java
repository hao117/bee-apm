package net.beeapm.ui.common;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BeeUtils {
    private static String DATE_FORMAT_DAY = "yyyy-MM-dd";
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

    public static String[] getIndices(String indexPrefix,Map<String,Object> param){
        String beginTime = (String) param.get("beginTime");
        beginTime = beginTime.substring(0,10).replace("-",".");
        String endTime = (String) param.get("endTime");
        endTime = endTime.substring(0,10).replace("-",".");
        if(beginTime.equals(endTime)){
            return new String[]{indexPrefix+beginTime};
        }
        try {
            Date begin = DateUtils.parseDate(beginTime, DATE_FORMAT_DAY);
            Date end = DateUtils.parseDate(endTime, DATE_FORMAT_DAY);
            if(begin.getTime() > end.getTime()){
                throw new RuntimeException("开始时间不能大于结束时间,开始时间为" + begin + "结束时间为" + end);
            }
            List<String> indexList = new ArrayList<>();
            indexList.add(indexPrefix+ DateFormatUtils.format(begin,DATE_FORMAT_DAY));
            for(;;){
                begin = DateUtils.addDays(begin,1);
                if(begin.getTime() <= end.getTime()){
                    indexList.add(indexPrefix+ DateFormatUtils.format(begin,DATE_FORMAT_DAY));
                }else {
                    break;
                }
            }
            return indexList.toArray(new String[indexList.size()]);
        }catch (Exception e){
            throw new RuntimeException("日期格式不正确",e);
        }
    }
}
