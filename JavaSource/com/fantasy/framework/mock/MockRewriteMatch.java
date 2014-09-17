package com.fantasy.framework.mock;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;

public class MockRewriteMatch extends RewriteMatch {
	private long calledTime = 0L;

	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		calledTime = System.currentTimeMillis();
		try {
			Thread.sleep(10L);
		} catch (InterruptedException e) {
		}
		return true;
	}

	public long getCalledTime() {
		return calledTime;
	}
}