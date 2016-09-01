package org.jfantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.regexp.RegexpUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;

public class DateUtil {

    private static final Log LOG = LogFactory.getLog(DateUtil.class);

    /**
     * 缓存的 SimpleDateFormat
     */
    private static final ConcurrentMap<String, DateFormatCache> DATE_FORMAT_CACHE = new ConcurrentHashMap<>();

    /**
     * 日期驱动接口 针对DateUtil.now()方法获取时间
     */
    private static DateDriver dateDriver = null;

    static {
        /**
         * 加载时获取日期驱动
         */
        String dateDriverClass = SimpleDateDriver.class.getName();
        dateDriverClass = PropertiesHelper.load("props/application.properties").getProperty("util.DateDriver", dateDriverClass);
        dateDriver = ClassUtil.newInstance(dateDriverClass);
    }

    public static String formatRfc822Date(Date expirationTime) {
        SimpleDateFormat rfc822DateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return rfc822DateFormat.format(expirationTime);
    }

    @Deprecated
    public enum DateType {
        DAY, HOUR, MINUTE, SECOND;

        public Date roundTo(DateType dateType, GregorianCalendar gc) {
            switch (dateType) {
                case DAY:
                    gc.set(Calendar.HOUR_OF_DAY, 0);
                    return gc.getTime();
                case HOUR:
                    gc.set(Calendar.MINUTE, 0);
                    gc.set(Calendar.SECOND, 0);
                    gc.set(Calendar.MILLISECOND, 0);
                    return gc.getTime();
                case MINUTE:
                    gc.set(Calendar.SECOND, 0);
                    gc.set(Calendar.MILLISECOND, 0);
                    return gc.getTime();
                case SECOND:
                    gc.set(Calendar.MILLISECOND, 0);
                    return gc.getTime();
                default:
                    return gc.getTime();
            }
        }
    }

    private static DateFormatCache getDateFormat(String format, Locale locale) {
        return getDateFormat(format, locale, null);
    }

    /**
     * 获取 SimpleDateFormat 对象
     *
     * @param format 格式
     * @return DateFormat
     */
    private static DateFormatCache getDateFormat(String format, Locale locale, TimeZone zone) {
        zone = ObjectUtil.defaultValue(zone, TimeZone.getDefault());
        locale = ObjectUtil.defaultValue(locale, Locale.getDefault());
        String key = zone.getID() + "->" + locale.toString() + "->" + format;
        if (!DATE_FORMAT_CACHE.containsKey(key)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("缓存日期格式:" + key);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
            dateFormat.setTimeZone(zone);
            DATE_FORMAT_CACHE.put(key, new DateFormatCache(new ReentrantLock(), dateFormat));
        }
        return DATE_FORMAT_CACHE.get(key);
    }

    public static String format(String format) {
        return format(now(), format);
    }

    public static String format(String format, Locale locale) {
        return format(now(), format, locale);
    }

    public static String format(String format, Locale locale, TimeZone zone) {
        return format(now(), format, locale, zone);
    }

    /**
     * 为ftl模板调用时提供的方法，以防时间对象为NULL时,找不到方法
     *
     * @param date   时间
     * @param format 格式
     * @return string
     */
    public static String format(Object date, String format) {
        if (date == null || !Date.class.isAssignableFrom(date.getClass())) {
            return "";
        }
        return format((Date) date, format);
    }

    public static String format(Date date, String format) {
        return format(date, format, Locale.getDefault());
    }

    public static String format(Date date, String format, Locale locale) {
        return format(date, format, locale, null);
    }

    /**
     * 格式化日期以字符串形式返回
     *
     * @param date   时间
     * @param format 格式
     * @return String
     */
    public static String format(Date date, String format, Locale locale, TimeZone zone) {
        if (date == null) {
            return "";
        }
        DateFormatCache cache = getDateFormat(format, locale, zone);
        cache.lock();
        try {
            return cache.format(date);
        } finally {
            cache.unlock();
        }
    }


    public static String toDay(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static boolean isSameDay(Date d1, Date d2) {
        return roundTo(DateType.DAY, d1).getTime() == roundTo(DateType.DAY, d2).getTime();
    }

    /**
     * 以指定格式格式化日期
     *
     * @param s      时间字符串
     * @param format 格式
     * @return data
     */
    public static Date parse(String s, String format) {
        return parse(s, format, null);
    }

    public static Date parse(String s, String format, Locale locale) {
        return parse(s, format, locale, null);
    }

    public static Date parse(String s, String format, Locale locale, TimeZone zone) {
        if (s == null) {
            return null;
        }
        DateFormatCache cache = getDateFormat(format, locale, zone);
        cache.lock();
        try {
            return cache.parse(s);
        } catch (ParseException e) {
            throw new IgnoreException(e.getMessage());
        } finally {
            cache.unlock();
        }
    }

    /**
     * 将字符串转为日期
     *
     * @param s 时间字符串
     * @return date
     */
    public static Date parse(String s) {
        if (s == null) {
            return null;
        }
        return parse(s, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 相隔的天数
     *
     * @param big   大的时间
     * @param small 小时间
     * @return double
     */
    public static long dayInterval(Date big, Date small) {
        return interval(big, small, Calendar.DATE);
    }

    public static boolean isWorkDay(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int week = gc.get(7);
        return (week == 7) || (week == 1);
    }

    public static Date roundTo(DateType dateType, Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return dateType.roundTo(dateType, gc);
    }

    /**
     * 获取当前日期的下一天
     *
     * @param date 时间
     * @return date
     */
    public static Date nextDay(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, 1);
        return roundTo(DateType.DAY, gc.getTime());
    }

    public static Date nextThreeDate(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, 3);
        return roundTo(DateType.DAY, gc.getTime());
    }

    public static Date nextHour(Date date) {
        return roundTo(DateType.HOUR, add(date, 10, 1));
    }

    /**
     * 添加时间
     *
     * @param date  原始时间
     * @param field 字段项
     * @param value 字段项值
     * @return date
     */
    public static Date add(Date date, int field, int value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(field, value);
        return gc.getTime();
    }

    /**
     * 设置时间的某个字段
     *
     * @param date  原始时间
     * @param field 字段项
     * @param value 字段项值
     * @return date
     */
    public static Date set(Date date, int field, int value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(field, value);
        return gc.getTime();
    }

    /**
     * 获取时间的最大上限
     *
     * @param date   原始日期
     * @param fields 设置的时间格式字段
     * @return date
     */
    public static Date getActualMaximumTime(Date date, int... fields) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int field : fields) {
            calendar.set(field, calendar.getActualMaximum(field));
        }
        return calendar.getTime();
    }

    /**
     * 获取时间的最小下限
     *
     * @param date   原始日期
     * @param fields 设置的时间格式字段
     * @return date
     */
    public static Date getActualMinimumTime(Date date, int... fields) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int field : fields) {
            calendar.set(field, calendar.getActualMinimum(field));
        }
        return calendar.getTime();
    }

    /**
     * 获取日期间隔
     *
     * @param big_   大的日期
     * @param small_ 小的日期
     * @param field  比较日期字段
     * @return date
     */
    public static long interval(Date big_, Date small_, int field) {
        Date big = big_;
        Date small = small_;
        boolean positive = big.after(small);
        if (!positive) {
            Date temp = big;
            big = small;
            small = temp;
        }
        long elapsed = 0;

        GregorianCalendar smallCalendar = new GregorianCalendar();
        smallCalendar.setTime(small);
        GregorianCalendar bigCalendar = new GregorianCalendar();
        bigCalendar.setTime(big);
        switch (field) {
            case Calendar.YEAR:
                smallCalendar.clear(Calendar.MONTH);
                bigCalendar.clear(Calendar.MONTH);
            case Calendar.MONTH:
                smallCalendar.clear(Calendar.DATE);
                bigCalendar.clear(Calendar.DATE);
            case Calendar.DATE://NOSONAR
                smallCalendar.clear(Calendar.HOUR);
                smallCalendar.clear(Calendar.HOUR_OF_DAY);
                bigCalendar.clear(Calendar.HOUR);
                bigCalendar.clear(Calendar.HOUR_OF_DAY);
            case Calendar.HOUR_OF_DAY:
                smallCalendar.clear(Calendar.MINUTE);
                bigCalendar.clear(Calendar.MINUTE);
            case Calendar.MINUTE:
                smallCalendar.clear(Calendar.SECOND);
                bigCalendar.clear(Calendar.SECOND);
            default:
                smallCalendar.clear(Calendar.MILLISECOND);
                bigCalendar.clear(Calendar.MILLISECOND);
        }
        if (smallCalendar.equals(bigCalendar)) {
            return elapsed;
        }
        while (smallCalendar.before(bigCalendar)) {//如果循环过多是否有潜在的性能问题
            smallCalendar.add(field, 1);
            elapsed++;
        }
        return positive ? elapsed : -elapsed;
    }

    /**
     * 最高到天
     * dd天HH小时mm分钟
     *
     * @param field   比较字段
     * @param between 间隔
     * @param format  格式
     * @return {String}
     */
    public static String intervalFormat(int field, long between, String format) {
        return intervalFormat(field, between, format, "0[^\\d]{1,}", "");
    }

    public static String intervalFormat(int field, long between, String format, String zeroFormat, String repStr) {
        between = Math.abs(between);
        long day = Calendar.DATE == field ? field : 0, hour = (field == Calendar.HOUR_OF_DAY || field == Calendar.HOUR) ? between : 0, minute = (field == Calendar.MINUTE) ? between : 0, second = (field == Calendar.SECOND) ? between : 0;
        switch (field) {
            case Calendar.SECOND:
                minute = second / 60;
                second = second % 60;
                break;
            case Calendar.MINUTE:
                hour = minute / 60;
                minute = minute % 60;
                break;
            case Calendar.HOUR_OF_DAY:
                break;
            case Calendar.HOUR:
                day = hour / 24;
                hour = hour % 24;
                break;
            default://Calendar.DATE or Calendar.MONTH or Calendar.YEAR
                throw new IgnoreException("不支持的时间转换格式:" + field);
        }
        final String days = String.valueOf(day), hours = String.valueOf(hour), minutes = String.valueOf(minute), seconds = String.valueOf(second);
        String retVal = RegexpUtil.replace(format, "dd|HH|mm|ss", new RegexpUtil.AbstractReplaceCallBack() {
            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                if ("dd".equals(text)) {
                    return days;
                }
                if ("HH".equals(text)) {
                    return hours;
                }
                if ("mm".equals(text)) {
                    return minutes;
                }
                if ("ss".equals(text)) {
                    return seconds;
                }
                return text;
            }

        });
        return StringUtil.isBlank(zeroFormat) ? retVal : RegexpUtil.replace(retVal, zeroFormat, repStr);
    }

    public static long interval(Date big, Date small, int field, int... ignore) {
        if (ignore.length == 0) {
            return interval(big, small, field);
        }
        boolean positive = big.after(small);
        if (!positive) {
            Date temp = big;
            big = small;
            small = temp;
        }
        long num = 0;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(small);


        do {
            num++;
            calendar.add(field, 1);
        } while (calendar.getTimeInMillis() < big.getTime());
        return positive ? num - 1 : -(num - 1);

    }

    public static boolean compare(Date big, Date small, int field) {
        return interval(big, small, field) > -1;
    }

    /**
     * 对日期添加天数
     *
     * @param date  原始日期
     * @param value 添加的天数
     * @return {Date}
     */
    public static Date addDay(Date date, int value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, value);
        return gc.getTime();
    }

    /**
     * 获取字段项对应的时第一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
    public static Date first(Date date, int field) {
        return getActualMinimumTime(date, field);
    }

    /**
     * 获取字段项对应的最后时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return ${Date}
     */
    public static Date last(Date date, int field) {
        return getActualMaximumTime(date, field);
    }

    /**
     * 获取字段项对应的下一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
    public static Date next(Date date, int field) {
        return add(date, field, 1);
    }

    /**
     * 获取字段项对应的上一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
    public static Date prev(Date date, int field) {
        return add(date, field, -1);
    }

    public static Date getLastDayOfWeek(Date date) {
        while (getTimeField(date, Calendar.DAY_OF_WEEK) != 1) {
            date = nextDay(date);
        }
        return date;
    }

    public static int getTimeField(Date date, int field) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(field);
    }

    public static int getWeekOfYear(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(Calendar.WEEK_OF_YEAR);
    }

    public static Date setTimeField(Date date, int field, int timeNum) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(field, timeNum);
        return gc.getTime();
    }

    /**
     * 比较时间返回最小值
     *
     * @param dates 比较的时间数组
     * @return date
     */
    public static Date min(Date... dates) {
        Date min = null;
        for (Date date : dates) {
            if (min == null) {
                min = date;
            } else {
                min = date.getTime() < min.getTime() ? date : min;
            }
        }
        return min;
    }

    /**
     * 比较时间返回最大值
     *
     * @param dates 比较的时间数组
     * @return date
     */
    public static Date max(Date... dates) {
        Date max = null;
        for (Date date : dates) {
            if (max == null) {
                max = date;
            } else {
                max = date.getTime() > max.getTime() ? date : max;
            }
        }
        return max;
    }

    /**
     * 获取当前时间
     *
     * @return date
     */
    public static Date now() {
        return dateDriver.getTime();
    }

    public static int age(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("出生时间大于当前时间!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;//注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    /**
     * 为DateUtil.now提供的时间启动接口
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2013-3-27 上午10:07:48
     */
    public static interface DateDriver {
        Date getTime();
    }

    /**
     * DateUtil.now 时间启动接口的默认实现
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2013-3-27 上午10:08:07
     */
    public static class SimpleDateDriver implements DateDriver {

        public Date getTime() {
            return new Date();
        }

    }

    private static class DateFormatCache {

        private SimpleDateFormat format;

        private Lock lock;

        public DateFormatCache(Lock lock, SimpleDateFormat format) {
            this.lock = lock;
            this.format = format;
        }

        public void lock() {
            this.lock.lock();
        }

        public String format(Date date) {
            return this.format.format(date);
        }

        public void unlock() {
            this.lock.unlock();
        }

        public Date parse(String s) throws ParseException {
            return this.format.parse(s);
        }
    }

    public static Date set(Date date, FieldValue... details) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        for (FieldValue d : details) {
            gc.set(d.getField(), d.getValue());
        }
        return gc.getTime();
    }

    public static FieldValue fieldValue(int field, int value) {
        return new FieldValue(field, value);
    }

    public static class FieldValue {
        private int field;
        private int value;

        public FieldValue(int field, int value) {
            this.field = field;
            this.value = value;
        }

        public int getField() {
            return field;
        }

        public int getValue() {
            return value;
        }
    }
}