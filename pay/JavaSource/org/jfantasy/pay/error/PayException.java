package org.jfantasy.pay.error;

import org.jfantasy.framework.spring.mvc.error.RestException;

public class PayException extends RestException{

    public PayException(String message){
        super(message);
    }

}
