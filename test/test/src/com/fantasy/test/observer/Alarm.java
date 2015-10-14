package com.fantasy.test.observer;

import java.util.Observable;
import java.util.Observer;

public class Alarm implements Observer {
    public void makeAlarm() {
        System.out.println("嘀嘀嘀...水已经烧开 ");
    }
    public void update(Observable o, Object arg) {
        makeAlarm();
    }
}