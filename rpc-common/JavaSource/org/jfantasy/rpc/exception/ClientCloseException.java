package org.jfantasy.rpc.exception;

public class ClientCloseException extends RpcException{

    private static final String MESSAGE = "Can't close this client, beacuse the client didn't connect a server.";

    public ClientCloseException() {
        super(MESSAGE);
    }
}
