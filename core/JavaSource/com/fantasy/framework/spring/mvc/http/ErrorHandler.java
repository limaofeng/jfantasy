package com.fantasy.framework.spring.mvc.http;

import com.fantasy.framework.spring.mvc.error.RestException;
import com.fantasy.framework.util.web.context.ActionContext;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorResponse errorResponse(Exception exception) {
        ActionContext context = ActionContext.getContext();
        if (context == null) {
            return new ErrorResponse(exception.getMessage());
        }
        HttpServletResponse response = context.getHttpResponse();
        if (exception instanceof RestException) {
            response.setStatus(((RestException) exception).getStatusCode());
        } else if (exception instanceof MethodArgumentNotValidException) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            ErrorResponse errorResponse = new ErrorResponse("输入的数据不合法,详情见 fieldErrors 字段");
            for (FieldError fieldError : ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()) {
                errorResponse.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return errorResponse;
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return new ErrorResponse(exception.getMessage());
    }

}

