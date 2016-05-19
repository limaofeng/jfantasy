package org.jfantasy.rpc.exception;

public class RpcException extends RuntimeException{
    public RpcException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public RpcException(final String errorMessage, final Exception cause, final Object... args) {
        super(String.format(errorMessage, args), cause);
    }

    public RpcException(final Exception cause) {
        super(cause.getMessage(), cause);
    }
}
