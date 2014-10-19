package com.fantasy.framework.lucene.cluster;

public class SendMessageTask implements Runnable {
	private ClusterNode node;
	private ClusterMessage message;

	public SendMessageTask(ClusterNode node, ClusterMessage message) {
		this.node = node;
		this.message = message;
	}

	public void run() {
		this.node.transmitMessage(this.message);
	}
	
}
