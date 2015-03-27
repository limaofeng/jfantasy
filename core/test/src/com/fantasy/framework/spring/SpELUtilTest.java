package com.fantasy.framework.spring;

import com.fantasy.attr.storage.bean.Article;
import com.fantasy.security.bean.User;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

public class SpELUtilTest {

    private final static Log LOG = LogFactory.getLog(SpELUtilTest.class);

    @Test
    public void testCreateEvaluationContext() throws Exception {
        User user = new User();
        user.setUsername("limaofeng");

        EvaluationContext context = SpELUtil.createEvaluationContext(user);
        Expression expression = SpELUtil.getExpression(" username == 'limaofeng' ");

        Boolean retVal = expression.getValue(context,Boolean.class);

        LOG.debug("value = " + retVal);

        Assert.assertTrue(retVal);

        user.setUsername("hebo");
        context = SpELUtil.createEvaluationContext(user);

        retVal = expression.getValue(context,Boolean.class);

        LOG.debug("value = " + retVal);

        Assert.assertFalse(retVal);

        expression = SpELUtil.getExpression(" true ");

        retVal = expression.getValue(context,Boolean.class);

        LOG.debug("value = " + retVal);

        Assert.assertTrue(retVal);
    }

    @Test
    public void testGetExpression() throws Exception {
        Article article = new Article();
        article.setSummary("aaa");
        article.setIssue(true);
        EvaluationContext context = SpELUtil.createEvaluationContext(article);
        Expression expression = SpELUtil.getExpression(" summary=='aaa' and issue==true ");

        Boolean retVal = expression.getValue(context,Boolean.class);

        LOG.debug("value = " + retVal);

        Assert.assertTrue(retVal);
    }
}