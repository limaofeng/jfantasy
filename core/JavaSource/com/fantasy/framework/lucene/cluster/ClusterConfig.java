package com.fantasy.framework.lucene.cluster;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClusterConfig {
	private static final int DEFAULT_PORT = 9200;
	private int serverPort = DEFAULT_PORT;
	private boolean selfNode;
	private List<String> localAddresses;
	private Map<String, ClusterNode> clusterNodes;
	private ExecutorService executor;
	private int threadPoolSize = 10;
	private ExecutorService serverExecutor;
	private ClusterServer server;
	private int bufferSize = 1024;
	private int maxEntitySize = 1048576;

	public ClusterConfig() {
		this.clusterNodes = new ConcurrentHashMap<String, ClusterNode>();
		this.localAddresses = HostAddressUtil.getLocalAddresses();
	}

	public void validate() {
		this.executor = Executors.newFixedThreadPool(this.threadPoolSize);

		this.serverExecutor = Executors.newSingleThreadExecutor();
		this.server = new ClusterServer();
		this.serverExecutor.execute(this.server);
	}

	public void addNode(String host) {
		addNode(host, serverPort);
	}

	public void addNode(String host, int port) {
		if (this.localAddresses.contains(host)) {
			this.selfNode = true;
		} else {
			this.selfNode = false;
			ClusterNode node = new ClusterNode(host, port);
			this.clusterNodes.put(host, node);
		}
	}

	public synchronized void sendMessage(ClusterMessage message) {
		for (ClusterNode node : this.clusterNodes.values()) {
			SendMessageTask task = new SendMessageTask(node, message);
			this.executor.execute(task);
		}
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getThreadPoolSize() {
		return this.threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public int getBufferSize() {
		return this.bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getMaxEntitySize() {
		return this.maxEntitySize;
	}

	public void setMaxEntitySize(int maxEntitySize) {
		this.maxEntitySize = maxEntitySize;
	}

	public ExecutorService getExecutor() {
		return this.executor;
	}

	public void invalidate() {
		if (this.executor != null) {
			this.executor.shutdown();
		}

		this.server.close();
		if (this.serverExecutor != null){
            this.serverExecutor.shutdown();
        }
	}

	public boolean isSelfNode() {
		return this.selfNode;
	}

	public void setClusterNodes(String[] clusterNodes) {
		for (String clusterNode : clusterNodes) {
			String host = clusterNode.split(":")[0];
			int port = Integer.valueOf(clusterNode.split(":")[1]);
//			this.addNode(host, port);
			ClusterNode node = new ClusterNode(host, port);
			this.clusterNodes.put(host, node);
		}
	}

}