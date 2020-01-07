package net.beeapm.ui.common;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BeeUtils {
    private static String DATE_FORMAT_DAY = "yyyy.MM.dd";
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

    /**
     * 返回值：间隔单位，日期格式，时区格式，间隔数
     * <br/>按分钟：1m YYYY-MM-dd HH:mm
     * <br/>按小时：1h YYYY-MM-dd HH
     * <br/>按天：1d YYYY-MM-dd
     * <br/>按月：1M YYYY-MM
     * <br/>按年：1Y YYYY
     * @param beginDate
     * @param endDate
     * @return
     */
    public static String[] parseDateInterval(Date beginDate,Date endDate){
        long MAX_DIFF = 30;//最大范围30分钟合0.5小时
        String timeZone = "+08:00";
        long diff = DateUtils.diffMinute(beginDate,endDate);
        if(diff < MAX_DIFF){
            return new String[]{"1m","MM-dd HH:mm",timeZone,diff+""};
        }
        if(diff < MAX_DIFF*2){
            return new String[]{"2m","MM-dd HH:mm",timeZone,Math.ceil(diff/2.0)+""};
        }
        if(diff < (MAX_DIFF * 5)){
            return new String[]{"5m","MM-dd HH:mm",timeZone,Math.ceil(diff/5.0)+""};
        }
        if(diff < (MAX_DIFF * 10)){
            return new String[]{"10m","MM-dd HH:mm",timeZone,Math.ceil(diff/10.0)+""};
        }
        if(diff < (MAX_DIFF * 30)){
            return new String[]{"30m","MM-dd HH:mm",timeZone,Math.ceil(diff/30.0)+""};
        }
        diff = DateUtils.diffHour(beginDate,endDate);
        MAX_DIFF = 36;//最大范围36小时合1.5天
        if(diff < MAX_DIFF){
            return new String[]{"1h","MM-dd HH:mm",timeZone,diff+""};
        }
        if(diff < (MAX_DIFF * 2)){
            return new String[]{"2h","MM-dd HH:mm",timeZone,Math.ceil(diff/2.0)+""};
        }
        if(diff < (MAX_DIFF * 4)){
            return new String[]{"4h","MM-dd HH:mm",timeZone,Math.ceil(diff/4.0)+""};
        }
        if(diff < (MAX_DIFF * 6)){
            return new String[]{"6h","MM-dd HH:mm",timeZone,Math.ceil(diff/6.0)+""};
        }
        if(diff < (MAX_DIFF * 8)){
            return new String[]{"8h","MM-dd HH:mm",timeZone,Math.ceil(diff/8.0)+""};
        }
        if(diff < (MAX_DIFF * 12)){
            return new String[]{"12h","MM-dd HH:mm",timeZone,Math.ceil(diff/12.0)+""};
        }
        diff = DateUtils.diffDay(beginDate,endDate);
        MAX_DIFF = 30;//最大范围30天合1个月
        if(diff < MAX_DIFF){
            return new String[]{"1d","YYYY-MM-dd",timeZone,diff+""};
        }
        if(diff < (MAX_DIFF * 2)){
            return new String[]{"2d","YYYY-MM-dd",timeZone,Math.ceil(diff/2.0)+""};
        }
        if(diff < (MAX_DIFF * 4)){
            return new String[]{"4d","YYYY-MM-dd",timeZone,Math.ceil(diff/4.0)+""};
        }
        if(diff < (MAX_DIFF * 7)){
            return new String[]{"7d","YYYY-MM-dd",timeZone,Math.ceil(diff/7.0)+""};
        }
        if(diff < (MAX_DIFF * 10)){
            return new String[]{"10d","YYYY-MM-dd",timeZone,Math.ceil(diff/10.0)+""};
        }
        if(diff < (MAX_DIFF * 15)){
            return new String[]{"15d","YYYY-MM-dd",timeZone,Math.ceil(diff/15.0)+""};
        }
        diff = DateUtils.diffMonth(beginDate,endDate);
        MAX_DIFF = 36;//最大范围36个月合3年
        if(diff < MAX_DIFF){
            return new String[]{"1M","YYYY-MM",timeZone,diff+""};
        }
        if(diff < (MAX_DIFF * 2)){
            return new String[]{"2M","YYYY-MM",timeZone,Math.ceil(diff/2.0)+""};
        }
        if(diff < (MAX_DIFF * 3)){
            return new String[]{"3M","YYYY-MM",timeZone,Math.ceil(diff/3.0)+""};
        }
        if(diff < (MAX_DIFF * 6)){
            return new String[]{"6M","YYYY-MM",timeZone,Math.ceil(diff/6.0)+""};
        }
        diff = DateUtils.diffYear(beginDate,endDate);
        return new String[]{"1Y","YYYY",timeZone,diff+""};
    }

    public static Date nextDateTime(Date beginDate,String interval){
        int num = Integer.parseInt(interval.substring(0,interval.length()-1));
        if(interval.endsWith("m")){
            beginDate = DateUtils.addMinute(beginDate,num);
        }else if(interval.endsWith("h")){
            beginDate = DateUtils.addHours(beginDate,num);
        }else if(interval.endsWith("d")){
            beginDate = DateUtils.setDay(beginDate,num);
        }else if(interval.endsWith("M")){
            beginDate = DateUtils.addMonths(beginDate,num);
        }else if(interval.endsWith("Y")){
            beginDate = DateUtils.addYears(beginDate,num);
        }
        return beginDate;
    }
}
