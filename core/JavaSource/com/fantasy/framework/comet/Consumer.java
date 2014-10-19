package com.fantasy.framework.comet;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

import com.fantasy.framework.util.common.DateUtil;

class Consumer implements Runnable {

	private BlockingQueue<String> drop;

	public Consumer(BlockingQueue<String> d) {
		this.drop = d;
	}

	public void run() {
		try {
			String msg = null;
			while (!(msg = (String) this.drop.take()).equals("DONE")) {
				Thread.sleep(1500L);
				System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS") + "\t" + msg);
			}
		} catch (Exception intEx) {
			intEx.printStackTrace();
		}
	}

}