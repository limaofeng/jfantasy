package org.jfantasy.framework.spring.mvc.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.spring.mvc.error.SecurityException;
import org.jfantasy.framework.spring.mvc.error.ValidationException;
import org.jfantasy.framework.util.web.context.ActionContext;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ErrorHandler {

    private static Log LOG = LogFactory.getLog(ErrorHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorResponse errorResponse(Exception exception) {
        ActionContext context = ActionContext.getContext();
        if (context == null) {
            return new ErrorResponse(50000, exception.getMessage());
        }
        ErrorResponse error = new ErrorResponse();
        HttpServletResponse response = context.getHttpResponse();
        if (exception instanceof SecurityException) {
            SecurityException _exception = ((SecurityException) exception);
            error.setCode(_exception.getCode());
            error.setMessage(_exception.getMessage());
            response.setStatus(_exception.getStatusCode());
        } else if (exception instanceof ValidationException) {
            ValidationException _exception = ((ValidationException) exception);
            error.setCode(_exception.getCode());
            error.setMessage(_exception.getMessage());
            response.setStatus(_exception.getStatusCode());
        } else if (exception instanceof RestException) {
            RestException _exception = ((RestException) exception);
            error.setMessage(_exception.getMessage());
            response.setStatus(_exception.getStatusCode());
        } else if (exception instanceof MethodArgumentNotValidException) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            error.setCode(40000);
            error.setMessage("输入的数据不合法,详情见 fieldErrors 字段");
            for (FieldError fieldError : ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()) {
                error.addError(fieldError.getField(), fieldError.getDefaultMessage());
            }
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        } else {
            LOG.error(exception.getMessage(), exception);
            error.setMessage(exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return error;
    }

}

