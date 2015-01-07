package com.fantasy.framework.lucene.cluster;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class ClusterNode {
	private static final Logger logger = Logger.getLogger(ClusterNode.class);
	private String host;
	private int port;

	public ClusterNode(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public synchronized void transmitMessage(ClusterMessage message) {
		SocketChannel channel = null;
		try {
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			SocketAddress address = new InetSocketAddress(this.host, this.port);
			while ((!channel.connect(address)) && (!channel.finishConnect())) {
                ;
            }
			channel.write(BufferUtil.toBuffer(message));
		} catch (IOException ex) {
			logger.error("Error when transmit message to host: " + this.host + ", port: " + this.port, ex);
		} finally {
			if (channel != null)
				try {
					channel.close();
				} catch (IOException ex) {
					logger.error("Error when close channel host: " + this.host + ", port: " + this.port, ex);
				}
		}
	}
}