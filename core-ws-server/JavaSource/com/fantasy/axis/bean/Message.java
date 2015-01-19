package com.fantasy.axis.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Axis 报文日志表
 */
@Entity(name = "axis.Message")
@Table(name = "AXIS_MESSAGE")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Message extends BaseBusEntity {

    /**
     * 消息类型
     */
    public static enum Type {
        /**
         * 客户端调用
         */
        client("客户端调用"),
        /**
         * 服务端响应
         */
        server("服务端响应");

        private String value;

        private Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

    }

    /**
     * 返回结果类型
     */
    public static enum ResultType {
        /**
         * 正常
         */
        normal("正常"),
        /**
         * 调用出错
         */
        fault("调用出错");
        private String value;

        private ResultType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    private String id;
    /**
     * 调用接口的远程地址
     */
    @Column(name = "REMOTE_ADDR", length = 20)
    private String remoteAddr;
    /**
     * 数据类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20)
    private Type type;
    /**
     * 请求内容
     */
    @Lob
    @Column(name = "IN_Message")
    private String in;
    /**
     * 返回结果
     */
    @Lob
    @Column(name = "OUT_Message")
    private String out;
    /**
     * 处理结果
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "RESULT", length = 20)
    private ResultType result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public ResultType getResult() {
        return result;
    }

    public void setResult(ResultType result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", in='" + in + '\'' +
                ", out='" + out + '\'' +
                ", result=" + result +
                '}';
    }
}
