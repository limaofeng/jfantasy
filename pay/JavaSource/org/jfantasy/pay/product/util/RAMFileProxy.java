package org.jfantasy.pay.product.util;

import org.jfantasy.pay.bean.PayConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RAMFileProxy implements FileProxy {

    private PayConfig payConfig;
    private String key;
    private String name;
    private InputStream input;

    public RAMFileProxy(PayConfig payConfig, String name) {
        this.payConfig = payConfig;
        this.name = name;
        this.key = payConfig.getId() + "-" + name + "-" + payConfig.getModifyTime().getTime();
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public InputStream getInputStream() {
        if (this.input == null) {
            return this.input = new ByteArrayInputStream(payConfig.get(this.name, byte[].class));
        }
        return this.input;
    }

}
