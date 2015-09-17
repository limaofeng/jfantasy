package com.fantasy.test.observer;

import org.junit.Test;

public class TestObserver {

    @Test
    public void test() {
        Heater heater = new Heater();

        Display display = new Display();

        Alarm alarm = new Alarm();

        heater.addObserver(display);

        display.addObserver(alarm);


        heater.boilWater();
    }

}
