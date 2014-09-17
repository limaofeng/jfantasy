package com.fantasy.file.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fantasy.file.bean.FileDetail;

@Component
@Aspect
public class WriteFileInterceptor {

	@After("execution(public void com.fantasy.file.FileManager.writeFile(java.lang.String,java.io.File))")
	public void writeFile(JoinPoint point) throws Throwable {
		System.out.println(point.getTarget());
		System.out.println(point.getKind());
		for(int i=0;i<point.getArgs().length;i++){
			System.out.println(point.getArgs()[i]);
		}
		FileContext fileContext = FileContext.getContext();
		FileDetail fileDetail = fileContext.peek();
		if (fileDetail == null) {
			//File file = ((File) pjp.getArgs()[1]);
//			Object fm = point.getTarget();
			System.out.println("拼装临时 FileDetail 对象");
		}
	}

}
