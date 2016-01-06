package org.jfantasy.express.product;

public interface Logistics {

    String getId();

    String getName();

    Bill execute(String sn);

}
