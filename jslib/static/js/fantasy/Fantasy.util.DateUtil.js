//============================================================
//[描    述] static	Date帮助类	主要功能：日期格式转换,日期比较。
//============================================================
Fantasy.util.DateUtil = new (Fantasy.util.jClass({

    jClass : 'Fantasy.util.DateUtil',
    
    initialize: function(){
    
    },
    
    /**
     * 格式化日期 返回 如(2000-01-10 12:00:20)
     *
     * @param date
     *            日期
     */
    toSeconds: function(date){
        return Date.format(date, 'yyyy-MM-dd hh:mm:ss');
    },
    
    /**
     * 格式化日期 返回 如(2000-01-10 12:00)
     *
     * @param date
     *            日期
     */
    toMinute: function(date){
        return Date.format(date, 'yyyy-MM-dd hh:mm');
    },
    
    /**
     * 格式化日期 返回 如(05/18)
     *
     * @param date
     *            日期
     */
    toMonthAndDay: function(date){
        return Date.format(date, 'MM/dd');
    },
    
    /**
     * 格式化日期 返回 如(05/18 16:10)
     *
     * @param date
     *            日期
     */
    toDaySeconds: function(date){
        return Date.format(date, 'MM/dd hh:mm');
    },
    
    /**
     * 格式化日期 返回 如(2000-01-10)
     *
     * @param date
     *            日期
     */
    toDay: function(date){
        return Date.format(date, 'yyyy-MM-dd');
    },
    
    /**
     * 格式化日期 返回 如(2000.01)
     * @param {Date} date
     */
    toYearMonth: function(date){
        return Date.format(date, 'yyyy.MM');
    },
    
    /**
     * 格式化日期 返回 如(12月22日 14:58)
     * @param {Date} date
     */
    toMonthDate: function(date){
        return Date.format(date, 'MM月dd日 hh:mm');
    },
    
    /**
     * 格式化日期 返回 如(20000110)
     *
     * @param date	日期
     */
    toShortDay: function(date){
        return Date.format(date, 'yyyyMMdd');
    },
    
    /**
     * 格式化日期 返回月份
     *
     * @param date
     *            日期
     */
    toMonth: function(date){
        return Date.format(date, 'MM');
    },
    
    /**
     * 格式化日期 返回日期
     *
     * @param date
     *            日期
     */
    toShortdd: function(date){
        return Date.format(date, 'dd');
    },
    
    /**
     * 格式化日期 返回时分秒
     *
     * @param date
     *            日期
     */
    toShortSeconds: function(date){
        return Date.format(date, 'hh:mm:ss');
    },
    
    /**
     * 格式化日期 返回年份
     *
     * @param date
     *            日期
     */
    toShortYear: function(date){
        return Date.format(date, 'yyyy');
    },
    
    /**
     * 以指定的格式返回日期
     *
     * @param s
     *            日期
     * @param format
     *            格式
     */
    valueof: function(source, format){
        return null;
    },
    
    /**
     * 以默认的格式返回日期 'yyyy-MM-dd'
     *
     * @param source
     */
    valueOfStandard: function(source){
        try {
            return null;
        } 
        catch (e) {
            alert('将' + source + '转换为时间的时候出现错误！');
        }
    },
    
    /**
     * 判断日期是否相同
     *
     * @param d1
     *            日期
     * @param d2
     *            日期
     */
    sSameDay: function(d1, d2){
        return true;
    },
    
    /**
     * 比较日期大小返回布尔型
     *
     * @param d1
     *            日期
     * @param d2
     *            日期
     * @return d1 > d2 true
     */
    compareDay: function(d1, d2){
        return true;
    },
    
    /**
     * 日期相减 返回天数
     *
     * @param big
     *            大的日期
     * @param small
     *            小的日期
     */
    dayInterval: function(big, small){
        var difference = Date.UTC(big.getFullYear(), big.getMonth(), big.getDate(), 0, 0, 0) - Date.UTC(small.getFullYear(), small.getMonth(), small.getDate(), 0, 0, 0);
        return difference / 1000 / 60 / 60 / 24;
    },
    
    /**
     * 日期相减 返回分钟
     *
     * @param big
     *            大的日期
     * @param small
     *            小的日期
     */
    minuteInterval: function(big, small){
        throw new Error('方法未实现');
    },
    
    /**
     * 日期相减 返回小时
     *
     * @param big
     *            大的日期
     * @param small
     *            小的日期
     */
    newMinuteInterval: function(big, small){
        throw new Error('方法未实现');
    },
    
    /**
     * 日期相减 返回秒数
     *
     * @param big
     *            大的日期
     * @param small
     *            小的日期
     */
    secondInterval: function(big, small){
        throw new Error('方法未实现');
    },
    
    /**
     * 日期相减 以几个星期的形式返回
     *
     * @param big
     *            大的日期
     * @param small
     *            小的日期
     */
    workDayInterval: function(big, small){
        throw new Error('方法未实现');
    },
    
    /**
     * 判断日期是否为工作日
     *
     * @param date日期
     */
    isWorkDay: function(date){
        throw new Error('方法未实现');
    },
    
    /**
     * @param date
     * @return
     */
    roundToDay: function(date){
        throw new Error('方法未实现');
    },
    
    roundToHour: function(date){
        throw new Error('方法未实现');
    },
    
    roundToMinute: function(date){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回日期的下一天
     * @param date
     *            日期
     */
    nextDate: function(date){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回日期的下三天
     *
     * @param date
     *            日期
     */
    nextThreeDate: function(date){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回日期的下一个小时
     *
     * @param date日期
     */
    nextHour: function(date){
        throw new Error('方法未实现');
    },
    
    add: function(date, field, amount){
        throw new Error('方法未实现');
    },
    
    addDay: function(date, amount){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回日期的上一天
     *
     * @param date日期
     */
    lastDate: function(date){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回日期的上一天当前小时
     *
     * @param date
     *            日期
     */
    newlastDate: function(date){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回当前日期的前或后i天
     * @param date
     *            当前日期
     * @param i
     *            前或后的天数前为负
     */
    lastDate: function(date, i){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回当前日期的前或后i天当前小时 liug
     * @param date
     *            当前日期
     * @param i
     *            前或后的天数前为负
     */
    lastDateToH: function(date, i){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回当前日期的前或后i天的当前时间
     * @param date
     *            当前日期
     * @param i
     *            前或后的天数前为负
     */
    lastDateToM: function(date, i){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回日期的上一个月
     * @param date
     *            日期
     */
    lastMonth: function(date){
        throw new Error('方法未实现');
    },
    
    /**
     * 返回日期的上三天
     *
     * @param date
     *            日期
     * @return
     */
    lastThreeDate: function(date){
        throw new Error('方法未实现');
    },
    
    roundToMonth: function(date){
        throw new Error('方法未实现');
    },
    
    roundToTenMinute: function(date){
        throw new Error('方法未实现');
    },
    
    getFirstDayOfMonth: function(date){
        throw new Error('方法未实现');
    },
    
    getFirstDayOfWeek: function(date){
        throw new Error('方法未实现');
    },
    
    getLastDayOfMonth: function(date){
        throw new Error('方法未实现');
    },
    
    getLastDayOfWeek: function(date){
        throw new Error('方法未实现');
    },
    
    /**
     * 判断日期是 pm还是 am
     *
     * @param date
     *            日期
     */
    ampm: function(date){
        return 'P';
    },
    
    getChineseWeekName: function(date){
        var w = 1;
        var cw = '';
        switch (w) {
            case Calendar.SUNDAY:
                cw = '星期日';
                break;
            case Calendar.MONDAY:
                cw = '星期一';
                break;
            case Calendar.TUESDAY:
                cw = '星期二';
                break;
            case Calendar.WEDNESDAY:
                cw = '星期三';
                break;
            case Calendar.THURSDAY:
                cw = '星期四';
                break;
            case Calendar.FRIDAY:
                cw = '星期五';
                break;
            case Calendar.SATURDAY:
                cw = '星期六';
                break;
            default:
                break;
        }
        return cw;
    },
    
    getChineseMonthName: function(date){
        var w = 1;
        var cw = '';
        switch (w) {
            case Calendar.JANUARY:
                cw = '一月';
                break;
            case Calendar.FEBRUARY:
                cw = '二月';
                break;
            case Calendar.MARCH:
                cw = '三月';
                break;
            case Calendar.APRIL:
                cw = '四月';
                break;
            case Calendar.MAY:
                cw = '五月';
                break;
            case Calendar.JUNE:
                cw = '六月';
                break;
            case Calendar.JULY:
                cw = '七月';
                break;
            case Calendar.AUGUST:
                cw = '八月';
                break;
            case Calendar.SEPTEMBER:
                cw = '九月';
                break;
            case Calendar.OCTOBER:
                cw = '十月';
                break;
            case Calendar.NOVEMBER:
                cw = '十一月';
                break;
            case Calendar.DECEMBER:
                cw = '十二月';
                break;
            default:
                break;
        }
        return cw;
    },
    
    nextSevenDate: function(date){
        throw new Error('方法未实现');
    },
    
    previousSevenDate: function(date){
        throw new Error('方法未实现');
    },
    
    previousYear: function(date, num){
        throw new Error('方法未实现');
    },
    
    /**
     * 取日期的最小值
     *
     * @param d1
     *            日期
     * @param d2
     *            日期
     */
    min: function(d1, d2){
        throw new Error('方法未实现');
    },
    
    /**
     * 取日期的最大值
     *
     * @param d1
     *            日期
     * @param d2
     *            日期
     */
    max: function(d1, d2){
        throw new Error('方法未实现');
    },
    
    /**
     * 比较时间 如date1小于date2返回1 date1大于date2返回-1否则返回0
     *
     * @param date1
     *            日期
     * @param date2
     *            日期
     */
    compareTime: function(date1, date2){
        throw new Error('方法未实现');
    },
    
    /**
     * 显示开始月份和结束月份之间的所有月份
     *
     * @param startMonth
     *            开始月份
     * @param endMonth
     *            结束月份
     */
    ArrayMonthOption: function(startMonth, endMonth){
       throw new Error('方法未实现');
    }
}))();

