package org.jfantasy.pay.product;

public abstract class AlipayPayProductSupport extends PayProductSupport {


//    /**
//     * 解析远程模拟提交后返回的信息，获得token
//     *
//     * @param text 要解析的字符串
//     * @return 解析结果
//     */
//    public static String getRequestToken(String text, String signType) throws Exception {
//        String requestToken = "";
//        //以“&”字符切割字符串
//        String[] strSplitText = text.split("&");
//        //把切割后的字符串数组变成变量与数值组合的字典数组
//        Map<String, String> paraText = new HashMap<String, String>();
//        for (String aStrSplitText : strSplitText) {
//
//            //获得第一个=字符的位置
//            int nPos = aStrSplitText.indexOf("=");
//            //获得字符串长度
//            int nLen = aStrSplitText.length();
//            //获得变量名
//            String strKey = aStrSplitText.substring(0, nPos);
//            //获得数值
//            String strValue = aStrSplitText.substring(nPos + 1, nLen);
//            //放入MAP类中
//            paraText.put(strKey, strValue);
//        }
//
//        if (paraText.get("res_data") != null) {
//            String resData = paraText.get("res_data");
//            //解析加密部分字符串（RSA与MD5区别仅此一句）
//            if ("0001".equals(signType)) {
//                resData = RSA.decrypt(resData, "", input_charset);
//            }
//
//            //token从res_data中解析出来（也就是说res_data中已经包含token的内容）
//            Document document = DocumentHelper.parseText(resData);
//            requestToken = document.selectSingleNode("//direct_trade_create_res/request_token").getText();
//        }
//        return requestToken;
//    }


}
