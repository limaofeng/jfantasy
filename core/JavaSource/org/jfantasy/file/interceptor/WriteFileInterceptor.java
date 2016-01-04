package org.jfantasy.file.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.jfantasy.file.bean.FileDetail;

@Component
@Aspect
public class WriteFileInterceptor {

    private static final Log LOGGER = LogFactory.getLog(WriteFileInterceptor.class);

    @After("execution(public void org.jfantasy.file.FileManager.writeFile(java.lang.String,java.io.File))")
    public void writeFile(JoinPoint point) throws Throwable {
        LOGGER.debug(point.getTarget());
        LOGGER.debug(point.getKind());
        for (int i = 0; i < point.getArgs().length; i++) {
            LOGGER.debug(point.getArgs()[i]);
        }
        FileContext fileContext = FileContext.getContext();
        FileDetail fileDetail = fileContext.peek();
        if (fileDetail == null) {

            LOGGER.debug("拼装临时 FileDetail 对象");
        }
    }

}
