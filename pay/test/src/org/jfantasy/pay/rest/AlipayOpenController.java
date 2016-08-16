package org.jfantasy.pay.rest;

import org.apache.commons.collections.map.HashedMap;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.sign.MD5;
import org.jfantasy.pay.product.sign.RSA;
import org.jfantasy.pay.product.sign.SignUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/alipay")
public class AlipayOpenController {

    @RequestMapping(value = "/auth",method = RequestMethod.GET)
    @ResponseBody
    public Object auth(HttpServletRequest request) throws IOException {
        Map<String,String> data = SignUtil.parseQuery(request.getQueryString(),true);
        System.out.println(data);

        String url = "https://openapi.alipay.com/gateway.do";
        Map<String,String> params = new TreeMap<>();
        params.put("app_id",data.get("app_id"));
        params.put("method","alipay.system.oauth.token");
        params.put("charset","utf-8");
        params.put("sign_type","RSA");
        params.put("timestamp", DateUtil.format("yyyy-MM-dd HH:mm:ss"));
        params.put("version","1.0");

        params.put("grant_type","authorization_code");//refresh_token
        params.put("code",data.get("auth_code"));


        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMqwpkaWk4p8gjAh\n" +
                "rQ6Vgq6qW/+9HWaQZGW/NhFIu+GTaPA/q3uOd+l627OstDqFAZjEmPzM3tF6eodL\n" +
                "h7zJBANHYZXj8+9jGqLV1uGxpjlFucr51A1c2/1bx4rlOaCZ1pqcw8FFkY6PeucD\n" +
                "5veT47OrvsKmX/sHqsdB9942vtk/AgMBAAECgYEAqxUBtDEipdDEPoYeQWIXJQDs\n" +
                "mGby6wBTjcIgi+Q9mYBIIglL4AV31135FaZflclweJbwnuj55gygYZRyJPny5KKZ\n" +
                "ixica0Fpe0iSPKQ9KwIn3rx6gC/wdYlo3n8V6GR1+iBhWZ7yNOWB06SzJW26KXMv\n" +
                "Gtdi4ts43xSNkvn9W4kCQQD/D58SoSmBcNN19+hjgur7DFPFjzCiyyi5iLc/JnDQ\n" +
                "5+UL8bR4Cx5EjR9uxs1vN6ILZBoaPlz1ZVM4dv4aekj1AkEAy2+r/ldEauUTdHLg\n" +
                "Ac+xYwevvvOK9HUN4ZNypOSZrGxNZmBHRR6SR/axNE6RVN0zwC6cNQuXoP3TiAM1\n" +
                "LpaI4wJASkqxicqZfVNwtG7GKJ4MdZ1MlUG05+YG8aupvGIlACRbadQ4PbL3WP5G\n" +
                "Bo0vb1KkB29bzwMVLoEZ8VtvfiTaNQJAFaLIzgIF+sBmM0pMXKT0Hq4gmNRaAOm6\n" +
                "EjWWSccuONJD4RF4QvefYxvveLqqZjYoXNYYMuQKukqEhsCglVXZNQJBAInO/KxS\n" +
                "u1i3dcbwtH4NYjac7wBsCmuTQ1c8ETsDbhYEyeomRZaRzC+X782xCqAomz7ZYQZ3\n" +
                "O6gK7gMBQZ+rQtQ=\n" +
                "-----END PRIVATE KEY-----";

        privateKey = privateKey.replaceAll("^-----BEGIN PRIVATE KEY-----","")
                .replaceAll("-----END PRIVATE KEY-----$","")
                .replaceAll("\n","");

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

        params.put("sign",sign(params,privateKey));

        Response response = HttpClientUtil.doPost(url,params);

        Map<String,?> result = JSON.deserialize(response.text(), HashedMap.class);

        System.out.println(result);

        data = (Map<String, String>)(result.containsKey("error_response") ? result.get("error_response") : result.get("alipay_system_oauth_token_response"));

        if(!verify(data ,result.get("sign").toString(),publicKey)){
            throw new RestException("签名错误");
        }

        return data;
    }

    public static boolean verify(Map<String, String> data,String sign, String key) {
        String signType = data.get("sign_type");
        if ("MD5".equals(signType)) {
            return MD5.verify(SignUtil.coverMapString(data, "sign", "sign_type"), sign, key, "utf-8");
        } else if ("RSA".equals(signType)) {
            return RSA.verify(SignUtil.coverMapString(data, "sign", "sign_type"), sign, key, "utf-8");
        } else if ("DSA".equals(signType)) {
            throw new PayException("DSA 签名方式还未实现");
        }
        throw new PayException("不支持的签名方式 => " + signType);
    }

    /**
     * 生成签名结果
     *
     * @param data 要签名的数组
     * @param key  sign_type = MD5 时为 安全校验码 如果为 sign_type = RSA 时为
     * @return 签名结果字符串
     */
    public static String sign(Map<String, String> data, String key) {
        if (!data.containsKey("sign_type")) {
            data.put("sign_type", "MD5");
        }
        String signType = data.get("sign_type");
        if ("MD5".equals(signType)) {
            return MD5.sign(SignUtil.coverMapString(data, "sign"), key, data.get("charset"));
        } else if ("RSA".equals(signType)) {
            return RSA.sign(SignUtil.coverMapString(data, "sign"), key, data.get("charset"));
        } else if ("DSA".equals(signType)) {
            throw new PayException("DSA 签名方式还未实现");
        }
        throw new PayException("不支持的签名方式 => " + signType);
    }

}
