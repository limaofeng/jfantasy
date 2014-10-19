Fantasy.apply(Date, {

    /**
     * 名称：format		属于:Date
     * 描述：根据指定的格式将对象格式化。
     * @param formatter
     * @return
     */
    format: function(formatter){
        if (!formatter || formatter == "") {
            formatter = "yyyy-MM-dd";
        }
        return Date.format(this, formatter);
    },
    
    clone: function(){
        return new Date(this.getTime());
    },
    
    getFirstDayOfMonth : function() {
        var day = (this.getDay() - (this.getDate() - 1)) % 7;
        return (day < 0) ? (day + 7) : day;
    },
    
    getLastDayOfMonth : function() {
        return this.getLastDateOfMonth().getDay();
    },
    
    getFirstDateOfMonth : function() {
        return new Date(this.getFullYear(), this.getMonth(), 1);
    },
    
    getLastDateOfMonth : function() {
        return new Date(this.getFullYear(), this.getMonth(), Date.getDaysInMonth(this));
    },
    
    isLeapYear : function() {
        var year = this.getFullYear();
        return !!((year & 3) == 0 && (year % 100 || (year % 400 == 0 && year)));
    },
    
    /**
     * 一个便利的日期运算的方法. 该方法摘抄自Ext的 Date.js
     * @param {Object} interval
     * @param {Object} value
     */
    add: function(interval, value){
        var d = this.clone();
        if (!interval || value === 0) 
            return d;
        switch (interval.toLowerCase()) {
            case Date.MILLI:
                d.setMilliseconds(this.getMilliseconds() + value);
                break;
            case Date.SECOND:
                d.setSeconds(this.getSeconds() + value);
                break;
            case Date.MINUTE:
                d.setMinutes(this.getMinutes() + value);
                break;
            case Date.HOUR:
                d.setHours(this.getHours() + value);
                break;
            case Date.DAY:
                d.setDate(this.getDate() + value);
                break;
            case Date.MONTH:
                var day = this.getDate();
                if (day > 28) {
                    day = Math.min(day, this.getFirstDateOfMonth().add(Date.MONTH, value).getLastDateOfMonth().getDate());
                }
                d.setDate(day);
                d.setMonth(this.getMonth() + value);
                break;
            case Date.YEAR:
                d.setFullYear(this.getFullYear() + value);
                break;
        }
        return d;
    }
}, (function(){

    /**
     * 提取日期的年月日时分秒
     * @param {Object} d
     * @param {Object} isZero
     */
    var splitDate = function(d, isZero){
        var yyyy, MM, dd, hh, HH, mm, ss, ms;
        if (isZero) {
            yyyy = d.getFullYear();
            MM = Fantasy.addZeroLeft(String(d.getMonth() + 1), 2);
            dd = Fantasy.addZeroLeft(String(d.getDate()), 2);
            HH = Fantasy.addZeroLeft(String(d.getHours()), 2);
            hh = parseInt(HH) > 12 ? Fantasy.addZeroLeft(String(parseInt(HH)-12), 2) : HH;
            mm = Fantasy.addZeroLeft(String(d.getMinutes()), 2);
            ss = Fantasy.addZeroLeft(String(d.getSeconds()), 2);
            ms = Fantasy.addZeroLeft(String(d.getMilliseconds()), 3);
        } else {
            yyyy = d.getFullYear();
            MM = d.getMonth() + 1;
            dd = d.getDate();
            HH = d.getHours();
            hh = HH > 12 ? HH-12 : HH;
            mm = d.getMinutes();
            ss = d.getSeconds();
            ms = d.getMilliseconds();
        }
        return {
            'yyyy': yyyy,
            'MM': MM,
            'dd': dd,
            'HH': HH,
            'hh': hh,
            'mm': mm,
            'ss': ss,
            'ms': ms,
            'cw': chineseWeekNames[d.getDay()]
        };
    };
    
    var chineseWeekNames = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];
    
    return {
        YEAR: 'y',
        MONTH: 'mo',
        DAY: 'd',
        HOUR: 'h',
        MINUTE: 'mi',
        SECOND: 's',
        MILLI: 'ms',
        
        Months: {
            Jan: 0,
            Feb: 1,
            Mar: 2,
            Apr: 3,
            May: 4,
            Jun: 5,
            Jul: 6,
            Aug: 7,
            Sep: 8,
            Oct: 9,
            Nov: 10,
            Dec: 11
        },
        
        Weeks: {
            Mon: 1,
            Tue: 2,
            Wed: 3,
            Thu: 4,
            Fri: 5,
            Sat: 6,
            Sun: 0
        },
        
        getDaysInMonth: (function() {
            var daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

            return function(date) { // return a closure for efficiency
                var m = date.getMonth();

                return m == 1 && date.isLeapYear() ? 29 : daysInMonth[m];
            };
        }()),
        
        /**
         * 以指定格式格式化日期 默认格式格式化日期格式为 yyyy-MM-dd HH:mm:ss
         * @param s
         *            日期
         * @param format
         *            格式
         */
        parse: function(source, formatter){
            try {
            	if(!source){
            		return new Date("Invalid Date");
            	}
                if (!/^\d[\S\s]+\d$/.test(source)) {
                    return new Date(source);
                }
                if (!formatter || formatter == "") {
                    formatter = "yyyy-MM-dd HH:mm:ss";
                }
                var data = new Array();
                var formatters = new Array('yyyy', 'MM', 'dd', 'HH', 'mm', 'ss');
                formatters.each(function(index){
                    var of = formatter.indexOf(this);
                    data[index] = (of > -1) ? source.substring(of, of + this.length) : '0'.addZeroLeft(this.length);
                });
                data[1] = Number(data[1]) > 0 ? Number(data[1]) - 1 : 0;
                return new Date(data[0], data[1], data[2], data[3], data[4], data[5]);
            }catch (e) {
                throw new Error(source + ',转换日期格式失败!(' + formatter ? formatter : 'Sun Mar 06 08:41:59 GMT 2011' + ')');
            }
        },
        
        /**
         * 格式化日期以字符串形式返回
         *
         * @param date
         *            日期
         * @param format
         *            格式
         */
        format: function(date, formatter){
            if (!formatter) {
                formatter = 'yyyy-MM-dd HH:mm:ss';
            }
            if (date == "Invalid Date" || !date || (jQuery.browser.msie && date == 'NaN')){
            	return null;
            }
            date = new Date(date);
            if (isNaN(date)) {
                throw new Error('时间参数非法\n正确的时间示例:\nThu Nov 9 20:30:37 UTC+0800 2006\n或\n2006/10/17');
            }
            var dateObject = splitDate(date, true);
            var formatters = new Array('yyyy', 'MM', 'dd', 'HH', 'mm', 'ss', 'ms', 'cw');
            formatters.each(function(index){
                formatter = formatter.replaceAll(this, dateObject[this]);
            });
            return formatter.indexOf('yy') > -1 ? formatter.replaceAll('yy', dateObject['yyyy'].toString().substr(2, 2)) : formatter;
        },
        
        getChineseWeekName : function(){
        	return chineseWeekNames[this.getDay()];
        }
    };
    
})());
