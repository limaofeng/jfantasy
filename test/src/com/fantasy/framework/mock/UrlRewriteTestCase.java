package com.fantasy.framework.mock;

import junit.framework.TestCase;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.NormalRewrittenUrl;
import org.tuckey.web.filters.urlrewrite.Rule;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;
import org.tuckey.web.filters.urlrewrite.utils.Log;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

public class UrlRewriteTestCase extends TestCase {
	Conf conf;
	UrlRewriter urlRewriter;

	public void loadConf(URL confFileUrl) {
		Log.setLevel("SYSOUT:DEBUG");
		this.conf = new Conf(confFileUrl);
		assertTrue("Conf should load without errors", this.conf.isOk());
		this.urlRewriter = new UrlRewriter(this.conf);
	}

	@SuppressWarnings("unchecked")
	public void assertRuleMatches(String ruleName, String requestUrl) {
		List<Rule> rules = this.urlRewriter.getConf().getRules();
		Rule rule = null;
		if (rules != null) {
			for (int i = 0; i < rules.size(); i++) {
				Rule loopRule = (Rule) rules.get(i);
				if (ruleName.equalsIgnoreCase(loopRule.getName())) {
					rule = loopRule;
				}
			}
		}
		if (rule == null) {
			assertTrue("WaveRule by the name " + ruleName + " does not exist", false);
			return;
		}
		MockResponse response = new MockResponse();
		MockRequest request = new MockRequest(requestUrl);
		NormalRewrittenUrl rewrittenUrl = null;
		try {
			rewrittenUrl = (NormalRewrittenUrl) rule.matches(request.getRequestURI(), request, response);
		} catch (IOException e) {
			assertNull("IOException during rule matching " + e.toString(), e);
		} catch (ServletException e) {
			assertNull("ServletException during rule matching " + e.toString(), e);
		} catch (InvocationTargetException e) {
			assertNull("InvocationTargetException during rule matching " + e.toString(), e);
		}

		assertNotNull("WaveRule " + ruleName + " does not match", rewrittenUrl);
	}

	public void testUrlRerwriteTestCase() {
	}
}