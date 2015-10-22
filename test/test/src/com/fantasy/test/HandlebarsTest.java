package com.fantasy.test;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class HandlebarsTest {

    @Test
    public void test() throws IOException {
        Handlebars handlebars = new Handlebars();
        long start = System.currentTimeMillis();
        for(int i=0;i<100;i++) {
            handlebars = new Handlebars();
        }
        System.out.println(System.currentTimeMillis() - start);
        Template template = handlebars.compileInline("Hello {{captcha}}!");
        start = System.currentTimeMillis();
        for(int i=0;i<100;i++)
            template = handlebars.compileInline("Hello {{captcha}}! ");
        System.out.println(System.currentTimeMillis() - start);
        AtomicReference<Map<String, Object>> data = new AtomicReference<Map<String, Object>>(new HashMap<String, Object>());
        data.get().put("config", null);
        data.get().put("captcha", "12345");

        System.out.println(template.apply(data.get()));

    }


}
