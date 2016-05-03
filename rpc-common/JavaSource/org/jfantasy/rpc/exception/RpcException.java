package org.jfantasy.rpc.exception;

public class RpcException extends RuntimeException{
    protected RpcException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    protected RpcException(final String errorMessage, final Exception cause, final Object... args) {
        super(String.format(errorMessage, args), cause);
    }

    protected RpcException(final Exception cause) {
        super(cause.getMessage(), cause);
    }
}
