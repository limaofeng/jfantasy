package org.jfantasy.rpc.exception;

public class ServerException extends RpcException{

    private String traceId;

    public ServerException(String traceId, final Exception cause) {
        super(cause);
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }
}
