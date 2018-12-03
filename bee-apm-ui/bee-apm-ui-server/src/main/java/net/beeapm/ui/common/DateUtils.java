package net.beeapm.ui.common;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DateUtils {

    private static Map<String, SimpleDateFormat> formats;
    public static final String DATE_DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 将date转换为yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, DATE_DEFAULT_PATTERN);
    }

    public static Date parseDate(String date, String pattern) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(date, pattern);
    }

    /**
     * 解析yyyy-MM-dd HH:mm:ss格式的日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(date, DATE_DEFAULT_PATTERN);
    }

    /**
     * 将输入pattern格式的日期换为取时间的毫秒数
     * @param str
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static long date2Long(String str, String pattern) throws ParseException {
        return parseDate(str, pattern).getTime();
    }



    /**
     * 在date日期上增加num天
     * @param date
     * @param num
     * @return
     */
    public static Date addDays(Date date, int num) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, num);
    }

    /**
     * 在date日期上增加num天,以pattern格式输出
     * @param date
     * @param num
     * @param pattern
     * @return
     */
    public static String addDays(Date date, int num, String pattern) {
        return format(addDays(date, num), pattern);
    }

    /**
     * 当前系统时间的下一天，明天
     * @return
     */
    public static Date nextDay() {
        return nextDay(new Date());
    }

    /**
     * 日期date的下一天
     * @return
     */
    public static Date nextDay(Date date) {
        return addDays(date, 1);
    }

    /**
     * 当前系统时间的下一天，明天,以pattern格式输出
     * @param pattern
     * @return
     */
    public static String nextDayStr(String pattern) {
        return nextDayStr(nextDay(), pattern);
    }

    /**
     * 日期date的下一天,以pattern格式输出
     * @param date
     * @param pattern
     * @return
     */
    public static String nextDayStr(Date date, String pattern) {
        return format(date, pattern);
    }

    /**
     * inPattern格式的输入日期inDate，增加num天，以outPattern格式输出
     * @param inDate
     * @param inPattern
     * @param outPattern
     * @param num
     * @return
     * @throws ParseException
     */
    public static String addDays(String inDate, String inPattern, String outPattern, int num) throws ParseException {
        Date date = parseDate(inDate, inPattern);
        return addDays(date, num, outPattern);
    }

    /**
     * 在date日期上增加num毫秒
     * @param date
     * @param num
     * @return
     */
    public static Date addMilliseconds(Date date, int num) {
        return org.apache.commons.lang3.time.DateUtils.addMilliseconds(date, num);
    }

    /**
     * 在date日期上增加num秒
     * @param date
     * @param num
     * @return
     */
    public static Date addSeconds(Date date, int num) {
        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, num);
    }

    /**
     * 在date日期上增加num分钟
     * @param date
     * @param num
     * @return
     */
    public static Date addMinute(Date date, int num) {
        return org.apache.commons.lang3.time.DateUtils.addMinutes(date, num);
    }

    /**
     * 在date日期上增加num小时
     * @param date
     * @param num
     * @return
     */
    public static Date addHours(Date date, int num) {
        return org.apache.commons.lang3.time.DateUtils.addHours(date, num);
    }

    /**
     * 在date日期上增加num周
     * @param date
     * @param num
     * @return
     */
    public static Date addWeeks(Date date, int num) {
        return org.apache.commons.lang3.time.DateUtils.addWeeks(date, num);
    }

    /**
     * 在date日期上增加num月
     * @param date
     * @param num
     * @return
     */
    public static Date addMonths(Date date, int num) {
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, num);
    }

    /**
     * 在date日期上增加num年
     * @param date
     * @param num
     * @return
     */
    public static Date addYears(Date date, int num) {
        return org.apache.commons.lang3.time.DateUtils.addYears(date, num);
    }

    /**
     * 日期date所在月的最后一天
     * @param date
     * @return
     */
    public static Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return cal.getTime();
    }

    /**
     * 日期date的下num个月的的最后一天
     * @param date
     * @param num
     * @return
     */
    public static Date lastDayOfNextMonth(Date date, int num) {
        Date d = addMonths(date, num);
        return lastDayOfMonth(d);
    }

    /**
     * 日期date的上num个月的最后一天
     * @param date
     * @param num
     * @return
     */
    public static Date lastDayOfPrevMonth(Date date, int num) {
        return lastDayOfNextMonth(date, num * -1);
    }

    /**
     * 当前月的最后一天，以pattern格式输出
     * @param pattern
     * @return
     */
    public static String lastDayOfMonth(String pattern) {
        Date d = lastDayOfMonth(new Date());
        return format(d, pattern);
    }

    /**
     * 输入inPattern格式的date日期的所在月最后一天，以outPattern格式输出
     * @param date
     * @param inPattern
     * @param outPattern
     * @return
     * @throws ParseException
     */
    public static String lastDayOfMonth(String date, String inPattern, String outPattern) throws ParseException {
        Date d = parseDate(date, inPattern);
        return format(d, outPattern);
    }

    /**
     * date所在年的最后一天
     * @param date
     * @return
     */
    public static Date lastDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        cal.set(Calendar.DAY_OF_YEAR, 0);
        return cal.getTime();
    }

    /**
     * date所在年的第一天
     * @param date
     * @return
     */
    public static Date firstDayOfYear(Date date) {
        return setDate(date,null,1,1);
    }

    /**
     * 重置日期的年月日，不需要的重置的参数传null<br/>
     * eg: 2017-05-23 改为2017-06-23 使用setDate(date,null,6,null)
     * @param date
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date setDate(Date date, Integer year, Integer month, Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (year != null) {
            cal.set(Calendar.YEAR, year);
        }
        if (month != null) {
            cal.set(Calendar.MONTH, month);
        }
        if (day != null) {
            cal.set(Calendar.DAY_OF_MONTH, day);
        }
        return cal.getTime();
    }

    /**
     * 重置日期的年月日，不需要的重置的参数传null<br/>
     * eg: 2017-05-23 11:25:17 改为2017-05-23 11:25:30使用setTime(date,null,null,30,null)
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @param millisecond
     * @return
     */
    public static Date setTime(Date date, Integer hour, Integer minute, Integer second, Integer millisecond) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if (hour != null) {
            cal.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != null) {
            cal.set(Calendar.MINUTE, minute);
        }
        if (second != null) {
            cal.set(Calendar.SECOND, second);
        }
        if (millisecond != null) {
            cal.set(Calendar.MILLISECOND, millisecond);
        }
        return cal.getTime();
    }

    /**
     * 修改日期的天
     * @param date
     * @param day
     * @return
     */
    public static Date setDay(Date date, int day) {
        return setDate(date, null, null, day);
    }

    /**
     * 修改日期的小时
     * @param date
     * @param hour
     * @return
     */
    public static Date setHour(Date date, int hour) {
        return setTime(date, hour, null, null, null);
    }

    /**
     * date日期的起始时间,比如2017-05-23 00:00:00.000
     * @param date
     * @return
     */
    public static Date beginDate(Date date) {
        return setTime(date, 0, 0, 0, 0);
    }

    /**
     * date日期的结束时间,比如2017-05-23 23:59:59.999
     * @param date
     * @return
     */
    public static Date endDate(Date date) {
        return setTime(date, 23, 59, 59, 999);
    }

    /**
     * date日期的起始时间,比如2017-05-23 00:00:00.000，以pattern格式输出
     * @param date
     * @param pattern
     * @return
     */
    public static String beginDateStr(Date date, String pattern) {
        return format(beginDate(date), pattern);
    }

    /**
     * date日期的起始时间,比如2017-05-23 00:00:00.000，以yyyy-MM-dd HH:mm:ss格式输出
     * @param date
     * @return
     */
    public static String beginDateStr(Date date) {
        return format(endDate(date));
    }

    /**
     * date日期的结束时间,比如2017-05-23 23:59:59.999,以pattern格式输出
     * @param date
     * @param pattern
     * @return
     */
    public static String endDateStr(Date date, String pattern) {
        return format(endDate(date), pattern);
    }

    /**
     * date日期的结束时间,比如2017-05-23 23:59:59.999，以yyyy-MM-dd HH:mm:ss格式输出
     * @param date
     * @return
     */
    public static String endDateStr(Date date) {
        return format(endDate(date));
    }

    /**
     * 判断闰年
     * <p>
     * 详细设计：
     * 1.被400整除是闰年，否则：
     * 2.不能被4整除则不是闰年
     * 3.能被4整除同时不能被100整除则是闰年
     * 4.能被4整除同时能被100整除则不是闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {

        if ((year % 400) == 0) {
            return true;
        } else if ((year % 4) == 0) {
            if ((year % 100) == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


      /**
     * 日期date所在月的第一天，以为pattern格式输出
     * @param date
     * @param pattern
     * @return
     */
    public static String firstDayOfMonth(Date date, String pattern) {
        Date d = setDate(date, null, null, 1);
        return format(d, pattern);
    }

    /**
     * 当前系统时间的所在月的第一天，以为pattern格式输出
     * @param pattern
     * @return
     */
    public static String firstDayOfMonth(String pattern) {
        Date d = setDate(new Date(), null, null, 1);
        return format(d, pattern);
    }

    /**
     * 判断两个时间大小(fistDate 在 secondDate 之前 返回true，否则返回false)
     *
     * @param format
     * @return
     * @throws ParseException
     */
    public static boolean compareDate(String fistDate, String secondDate, String format) throws ParseException {
        Date fist = parseDate(fistDate, format);
        Date second = parseDate(secondDate, format);
        return fist.before(second);
    }

    /**
     * 日期检查,判断是否是合法日期
     *
     * @param value    要验证的值
     * @param varValue xml 规则上的值,日期格式 yyyy-MM-dd HH:mm:s
     * @return
     */
    public static boolean isRightDate(String value, String varValue) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(varValue);
            format.setLenient(false);//若为true 2-31会自动转换为3-3
            format.parse(value);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param date
     * @param field
     * @return
     */
    public static int getField(Date date, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    /**
     * 获取date上的年
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        return getField(date, Calendar.YEAR);
    }

    /**
     * 获取date上的月
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        return getField(date, Calendar.MONTH);
    }

    /**
     * 获取date上的日
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        return getField(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取date上的小时
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        return getField(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取date上的分钟
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        return getField(date, Calendar.MINUTE);
    }

    /**
     * 获取date上的秒数
     * @param date
     * @return
     */
    public static int getSecond(Date date) {
        return getField(date, Calendar.SECOND);
    }


    /**
     * 比较是否同一天
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        return org.apache.commons.lang3.time.DateUtils.isSameDay(date1, date2);
    }

    /**
     * 两个日期相差几年
     * @param date1
     * @param date2
     * @return
     */
    public static int diffYear(Date date1, Date date2) {
        return Math.abs(getYear(date1) - getYear(date2));
    }

    /**
     * 两个日期相差几个月
     * @param date1
     * @param date2
     * @return
     */
    public static int diffMonth(Date date1, Date date2) {
        int month1 = getYear(date1) * 12 + getMonth(date1);
        int month2 = getYear(date2) * 12 + getMonth(date2);
        return Math.abs(month1 - month2);
    }

    /**
     * 两个日期相差几天
     * @param date1
     * @param date2
     * @return
     */
    public static int diffDay(Date date1, Date date2) {
        date1 = setTime(date1, 1, 0, 0, 0);
        date2 = setTime(date2, 1, 0, 0, 0);
        long diff = Math.abs(date1.getTime() - date2.getTime());
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    /**
     * 两个日期相差几个小时
     * @param date1
     * @param date2
     * @return
     */
    public static long diffHour(Date date1, Date date2) {
        date1 = setTime(date1, null, 0, 0, 0);
        date2 = setTime(date2, null, 0, 0, 0);
        long diff = Math.abs(date1.getTime() - date2.getTime());
        return diff / (60 * 60 * 1000);
    }

    /**
     * 两个日期相册几分钟
     * @param date1
     * @param date2
     * @return
     */
    public static long diffMinute(Date date1, Date date2) {
        date1 = setTime(date1, null, null, 0, 0);
        date2 = setTime(date2, null, null, 0, 0);
        long diff = Math.abs(date1.getTime() - date2.getTime());
        return diff / (60 * 1000);
    }

    /**
     * 两个日期相差几秒
     * @param date1
     * @param date2
     * @return
     */
    public static long diffSecond(Date date1, Date date2) {
        date1 = setTime(date1, null, null, null, 0);
        date2 = setTime(date2, null, null, null, 0);
        long diff = Math.abs(date1.getTime() - date2.getTime());
        return diff / 1000;
    }

}
