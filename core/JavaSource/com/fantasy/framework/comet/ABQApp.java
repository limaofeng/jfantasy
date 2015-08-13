package com.fantasy.framework.comet;

import java.util.concurrent.LinkedBlockingQueue;

public class ABQApp {
    private ABQApp() {
    }

    public static void main(String[] args) {
        LinkedBlockingQueue drop = new LinkedBlockingQueue();

        new Thread(new Producer(drop)).start();
        new Thread(new Consumer(drop)).start();
    }
}
