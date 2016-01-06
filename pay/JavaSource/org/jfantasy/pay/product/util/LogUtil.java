package org.jfantasy.pay.product.util;

/**
 * 银联支付
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;

public class LogUtil {
    private static final Logger GATELOG = LoggerFactory.getLogger("ACP_SDK_LOG");
    private static final Logger GATELOG_ERROR = LoggerFactory.getLogger("SDK_ERR_LOG");
    private static final Logger GATELOG_MESSAGE = LoggerFactory.getLogger("SDK_MSG_LOG");
    static final String LOG_STRING_REQ_MSG_BEGIN = "============================== SDK REQ MSG BEGIN ==============================";
    static final String LOG_STRING_REQ_MSG_END = "==============================  SDK REQ MSG END  ==============================";
    static final String LOG_STRING_RSP_MSG_BEGIN = "============================== SDK RSP MSG BEGIN ==============================";
    static final String LOG_STRING_RSP_MSG_END = "==============================  SDK RSP MSG END  ==============================";

    public LogUtil() {
    }

    public static void writeLog(String cont) {
        GATELOG.info(cont);
    }

    public static void writeErrorLog(String cont) {
        GATELOG_ERROR.error(cont);
    }

    public static void writeErrorLog(String cont, Throwable ex) {
        GATELOG_ERROR.error(cont, ex);
    }

    public static void writeMessage(String msg) {
        GATELOG_MESSAGE.info(msg);
    }

    public static void printRequestLog(Map<String, String> reqParam) {
        writeMessage("============================== SDK REQ MSG BEGIN ==============================");

        for (Entry<String, String> en : reqParam.entrySet()) {
            writeMessage("[" + en.getKey() + "] = [" + en.getValue() + "]");
        }

        writeMessage("==============================  SDK REQ MSG END  ==============================");
    }

    public static void printResponseLog(String res) {
        writeMessage("============================== SDK RSP MSG BEGIN ==============================");
        writeMessage(res);
        writeMessage("==============================  SDK RSP MSG END  ==============================");
    }

    public static void debug(String cont) {
        if (GATELOG.isDebugEnabled()) {
            GATELOG.debug(cont);
        }

    }
}

