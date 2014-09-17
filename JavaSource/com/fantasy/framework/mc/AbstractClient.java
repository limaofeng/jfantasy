package com.fantasy.framework.mc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fantasy.framework.util.LinkedBlockingQueue;

public abstract class AbstractClient implements Client {

	protected Log logger = LogFactory.getLog(getClass());
	
	//待发送的消息
	public LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();
	
	//已发送的消息
	public LinkedBlockingQueue<Message> sendQueue = new LinkedBlockingQueue<Message>();
	
	//正在读的消息（接收不完整的数据）
	
	//以读取的消息(已成功接收的消息)
	public LinkedBlockingQueue<Message> queue01 = new LinkedBlockingQueue<Message>();
	
	//待向中心传递的消息
	public LinkedBlockingQueue<Message> pushQueue = new LinkedBlockingQueue<Message>();
	
	private Thread pushThread = new Thread(new Runnable() {

		public void run() {
			try {
				pushQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	});
	
	public AbstractClient() {
		pushThread.start();
	}

	public String connect() {
		return null;
	}

	public Message pull() {
		return null;
	}

	public void push(Message message) {
		try {
			pushQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Info getInfo() {
		return null;
	}

	public void setInfo(Info info) {
	}

}
