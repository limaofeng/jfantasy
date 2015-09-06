package com.fantasy.framework.spring.mvc.http;

import com.fantasy.framework.spring.mvc.error.RestException;
import com.fantasy.framework.util.web.context.ActionContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorResponse errorResponse(Exception exception) {
        HttpServletResponse response = ActionContext.getContext().getHttpResponse();
        if (exception instanceof RestException) {
            response.setStatus(((RestException) exception).getStatusCode());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return new ErrorResponse(exception.getMessage());
    }

}

