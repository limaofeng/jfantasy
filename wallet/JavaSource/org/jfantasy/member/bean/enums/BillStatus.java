package org.jfantasy.member.bean.enums;

/**
 * 账单类型
 */
public enum BillStatus {
    /**
     * 处理中
     */
    processing,
    /**
     * 成功
     */
    success,
    /**
     * 关闭
     */
    close;

    public static BillStatus getStatusByTradeStatus(String status) {
        switch (status) {
            case "unprocessed":
            case "processing":
                return processing;
            case "success":
                return success;
            case "close":
                return close;
        }
        return null;
    }
}
