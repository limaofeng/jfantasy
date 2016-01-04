package org.jfantasy.framework.util.common;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

public class PinyinUtilTest extends TestCase {

    @Test
    public void testHanziToPinyin() throws Exception {
//        Assert.assertEquals(PinyinUtil.hanziToPinyin("长沙"),"chang sha");

//        Assert.assertEquals(Arrays.toString(PinyinUtil.stringToPinyin("长沙",true," ")),Arrays.toString(new String[]{"chang sha"}));

        Assert.assertEquals(PinyinHelper.convertToPinyinString("长沙"," ", PinyinFormat.WITHOUT_TONE),"chang sha");

        Assert.assertEquals(PinyinHelper.convertToPinyinString("重庆"," ", PinyinFormat.WITHOUT_TONE),"chong qing");
    }
}