//============================================================
//[描    述] static		Math帮助类
//============================================================
Fantasy.util.MathUtil = new (Fantasy.util.jClass((function(){

    return {
        jClass: 'Fantasy.util.MathUtil',
        
        initialize: function(){
        },
        
        chinese: {
            simple: {
                number: ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'],
                unit: ['','十', '百', '千', '万', '亿','','','']
            },
            money: {
				number : ["零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"],
				unit : ["圆", "拾", "佰", "仟", "万", "亿", "角", "分", "整" ]
            }
        },
        
        abs: function(number){
            return Math.abs(number);
        },
        
        toInt: function(source){
            if (!isNaN(source)) 
                return parseInt(source,10);
            else 
                return parseInt(0);
        },
        
        getRatio: function(a, b){
            return (Math.max(0, a) / b);
        },
        
        getMax: function(a, b){
            return Math.max(a, b);
        },
        
        getMin: function(a, b){
            return Math.min(a, b);
        },
        
        toChinese: function(number,unit,type){
			var chinese = new String();
			type = type?type:'simple';
			unit = unit == null ? 0 : unit;
			
			var numbers = Fantasy.util.MathUtil.chinese[type].number;
			var units = Fantasy.util.MathUtil.chinese[type].unit;
			
			chinese  = number.split('\.')[0];
        	number = number.split('\.').length > 1 ? number.split('\.')[1] : null;			
			 
        	var index = unit == 0 ? 0 : unit % 4 == 0 ? unit == 8 ? 5 : 4 : unit % 4;
        	chinese = chinese.length > 1 ? this.toChinese(chinese.substr(0,chinese.length - 1),unit + 1) + (numbers[parseInt(chinese.substr(chinese.length - 1))] + (chinese.substr(chinese.length - 1).equals("0") && unit % 4 != 0 ? "" : units[index])) : (numbers[parseInt(chinese)] + (chinese.substr(chinese.length - 1).equals("0") && unit % 4 != 0 ? "" : units[index]));
	        
			if (unit == 0) {
	            chinese = chinese.replace(numbers[0] + "{1,}$",units[0]).replace(numbers[0] + "{1,}",numbers[0]).replace(numbers[0] + units[5],units[5]).replace(numbers[0] + units[4],units[4]).replace(units[5] + units[4],units[5] + numbers[0]).replace(numbers[0] + units[0],units[0]).replace("^"+ numbers[1] + units[1],units[1]);
	            //chinese += "00".equals(number) ? units[8] : ((numbers[parseInt(number.substr(0,1))] + (number.substr(0,1).equals("0") ? "" : units[6])) + (number.substr(1).equals("0") ? "" : (numbers[parseInt(number.substr(1))] + units[7])));
	        	//console.log(number+'|'+chinese);
			}
	        return chinese;
        },
		
		getPageCount : function(total,size){
			return Math.ceil(total / size);
		},
		
		random : function(n){
			return Math.floor(Math.random()*n);
		},


        add : function(arg1, arg2){
            var r1, r2, m;
            try {
                r1 = arg1.toString().split(".")[1].length;
            } catch (e) {
                r1 = 0;
            }
            try {
                r2 = arg2.toString().split(".")[1].length;
            } catch (e) {
                r2 = 0;
            }
            m = Math.pow(10, Math.max(r1, r2));
            return (arg1 * m + arg2 * m) / m;
        },

        // 浮点数减法运算
        subtract : function (arg1, arg2) {
            var r1, r2, m, n;
            try {
                r1 = arg1.toString().split(".")[1].length;
            } catch (e) {
                r1 = 0;
            }
            try {
                r2 = arg2.toString().split(".")[1].length;
            } catch (e) {
                r2 = 0;
            }
            m = Math.pow(10, Math.max(r1, r2));
            // 动态控制精度长度
            n = (r1 >= r2) ? r1 : r2;
            return ((arg1 * m - arg2 * m) / m).toFixed(n);
        },

        // 浮点数乘法运算
        multiply : function(arg1, arg2) {
            var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
            try {
                m += s1.split(".")[1].length;
            } catch (e) {
            }
            try {
                m += s2.split(".")[1].length;
            } catch (e) {
            }
            return Number(s1.replace(".", "")) * Number(s2.replace(".", ""))/ Math.pow(10, m);
        },

        // 浮点数除法运算
        divide : function (arg1, arg2) {
            var t1 = 0, t2 = 0, r1, r2;
            try {
                t1 = arg1.toString().split(".")[1].length;
            } catch (e) {
            }
            try {
                t2 = arg2.toString().split(".")[1].length;
            } catch (e) {
            }
            with (Math) {
                r1 = Number(arg1.toString().replace(".", ""));
                r2 = Number(arg2.toString().replace(".", ""));
                return (r1 / r2) * pow(10, t2 - t1);
            }
        }

    };
    
})()))();
/**
 * {@link Fantasy.util.MathUtil#toChinese}的简写方式
 * @member Fantasy toChinese
 * @method */
Fantasy.toChinese = Fantasy.util.MathUtil.toChinese;
/**
 * {@link Fantasy.util.MathUtil#toInt}的简写方式
 * @member Fantasy toInt
 * @method */
Fantasy.toInt = Fantasy.util.MathUtil.toInt;

Fantasy.random = Fantasy.util.MathUtil.random;

Fantasy.getMax = Fantasy.util.MathUtil.getMax;

Fantasy.getMin = Fantasy.util.MathUtil.getMin;

Fantasy.add = Fantasy.util.MathUtil.add;

Fantasy.subtract = Fantasy.util.MathUtil.subtract;

Fantasy.multiply = Fantasy.util.MathUtil.multiply;

Fantasy.divide = Fantasy.util.MathUtil.divide;

 /*    public static String toChinese(String number) {
        return toChinese(number, 0);
    }
    
    private static String toChinese(String number, int unit) {
        String chinese = number.split("\\.")[0];
        number = number.split("\\.").length > 1 ? number.split("\\.")[1] : null;
        int index = unit == 0 ? 0 : unit % 4 == 0 ? unit == 8 ? 5 : 4 : unit % 4;
        chinese = chinese.length() > 1 ? toChinese(chinese.substring(0,
                                                                     chinese.length() - 1),
                                                   unit + 1)
                + (numberChinese[Integer.valueOf(chinese.substring(chinese.length() - 1))] + (chinese.substring(chinese.length() - 1)
                                                                                                     .equals("0")
                        && unit % 4 != 0 ? "" : unitChinese[index])) : (numberChinese[Integer.valueOf(chinese)] + (chinese.substring(chinese.length() - 1)
                                                                                                                          .equals("0")
                && unit % 4 != 0 ? "" : unitChinese[index]));
        if (number != null) {
            chinese = chinese.replaceAll(numberChinese[0] + "{1,}$",
                                         unitChinese[0])
                             .replaceAll(numberChinese[0] + "{1,}",
                                         numberChinese[0])
                             .replaceAll(numberChinese[0] + unitChinese[5],
                                         unitChinese[5])
                             .replaceAll(numberChinese[0] + unitChinese[4],
                                         unitChinese[4])
                             .replaceAll(unitChinese[5] + unitChinese[4],
                                         unitChinese[5] + numberChinese[0])
                             .replaceAll(numberChinese[0] + unitChinese[0],
                                         unitChinese[0])
                             .replaceAll("^"
                                                 + numberChinese[1]
                                                 + unitChinese[1],
                                         unitChinese[1]);
            chinese += "00".equals(number) ? unitChinese[8] : ((numberChinese[Integer.valueOf(number.substring(0,
                                                                                                               1))] + (number.substring(0,
                                                                                                                                        1)
                                                                                                                             .equals("0") ? "" : unitChinese[6])) + (number.substring(1)
                                                                                                                                                                           .equals("0") ? "" : (numberChinese[Integer.valueOf(number.substring(1))] + unitChinese[7])));
        };
        return chinese;
    }
    
    public static void main(String[] args) {
        System.out.println(toChinese("101001001.01", 0));
    }


function GetRandomNum(Min,Max)
 {
 var Range = Max - Min;
 var Rand = Math.random();
 return(Min + Math.round(Rand * Range));
 }

 var num = GetRandomNum(1,10);
 alert(num);

 var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];

 function generateMixed(n) {


 var res = "";


 for(var i = 0; i < n ; i ++) {


 var id = Math.ceil(Math.random()*35);


 res += chars[id];


 }


 return res;


 }


 1.Math.random(); 结果为0-1间的一个随机数(包括0,不包括1)


 2.Math.floor(num); 参数num为一个数值，函数结果为num的整数部分。


 3.Math.round(num); 参数num为一个数值，函数结果为num四舍五入后的整数。


 Math：数学对象，提供对数据的数学计算。


 Math.random(); 返回0和1间(包括0,不包括1)的一个随机数。


 Math.ceil(n); 返回大于等于n的最小整数。


 用Math.ceil(Math.random()*10);时，主要获取1到10的随机整数，取0的几率极小。


 Math.round(n); 返回n四舍五入后整数的值。


 用Math.round(Math.random());可均衡获取0到1的随机整数。


 用Math.round(Math.random()*10);时，可基本均衡获取0到10的随机整数，其中获取最小值0和最大值10的几率少一半。


 Math.floor(n); 返回小于等于n的最大整数。


 用Math.floor(Math.random()*10);时，可均衡获取0到9的随机整数。


 获取页面某一元素的绝对X,Y坐标，可以用offset()方法：


 var X = $('#DivID').offset().top;


 var Y = $('#DivID').offset().left;


 获取相对(父元素)位置:


 var X = $('#DivID').position().top;


 var Y = $('#DivID').position().left;


 */