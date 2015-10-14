package com.fantasy.test.observer;

import java.util.Observable;
import java.util.Observer;

public class Display extends Observable implements Observer {
    private String status = "未开";

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayTemputer(int temperature) {
        if (temperature > 95) {
            this.setStatus("沸腾");
            this.setChanged();
            this.notifyObservers();
        }
        System.out.println("状态：" + status + " 现在温度：" + temperature + "");
    }

    public void update(Observable o, Object arg) {
        displayTemputer(((Heater) o).getTemperature());//这里不是很好
    }
}
