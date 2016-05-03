package org.jfantasy.rpc.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RpcResponse {

    private String traceId;

    private Throwable error;

    //不采用java体系的话，依赖status及msg
//    private int status;

//    private String msg;

    private Object result;

    public RpcResponse() {
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isError() {
        return error != null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
