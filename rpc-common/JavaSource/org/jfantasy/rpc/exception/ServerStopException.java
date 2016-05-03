package org.jfantasy.rpc.exception;


public class ServerStopException extends RpcException{

    private static final String MESSAGE = "Can't stop this server, because the server didn't start yet.";

    public ServerStopException() {
        super(MESSAGE);
    }
}
