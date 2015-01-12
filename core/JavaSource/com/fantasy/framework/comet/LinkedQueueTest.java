package com.fantasy.framework.comet;


public class LinkedQueueTest{

	public static void main(String[] args) throws Exception {
		
		//System.out.println( 8 >> 1);
		
		//AtomicInteger count = new AtomicInteger(0);
		
		//System.out.println(count.getAndIncrement());
		
		//System.out.println(count.get());
		
		/*
		final LinkedQueue<String> linkedQueue = new LinkedQueue<String>(2);
		//linkedQueue.add("123456");
		System.out.println("可用容量:" + linkedQueue.remainingCapacity());
		new Thread(new Runnable() {

			@SuppressWarnings("static-access")
			public void run() {
				do {
					String s = MD5Utils.getInstance().get(DateUtils.format("hhmmssSSS"));
					System.out.println("循环添加元素:" + s);
					try {
						linkedQueue.put(s);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					System.out.println(Thread.currentThread().getId()+"\t元素:" + s + "添加成功");
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (true);
			}

		}).start();
		new Thread(new Runnable() {

			@SuppressWarnings("static-access")
			public void run() {
				do {
					System.out.println("循环获取元素:");
					String s = "";
					try {
						s = linkedQueue.take();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					System.out.println(Thread.currentThread().getId()+"\t元素:" + s + "获取成功");
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (true);
			}

		}).start();
		*/
	}

}