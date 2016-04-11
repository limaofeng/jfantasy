package org.jfantasy.framework.spring;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.json.bean.User;
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
        User user = new User();
        user.setUsername("aaa");

        user.setEnabled(true);
        EvaluationContext context = SpELUtil.createEvaluationContext(user);
        Expression expression = SpELUtil.getExpression(" username=='aaa' and enabled==true ");

        Boolean retVal = expression.getValue(context,Boolean.class);

        LOG.debug("value = " + retVal);

        Assert.assertTrue(retVal);
    }
}