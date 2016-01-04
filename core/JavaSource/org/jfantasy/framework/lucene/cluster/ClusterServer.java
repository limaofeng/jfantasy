package org.jfantasy.framework.lucene.cluster;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.jfantasy.framework.lucene.BuguIndex;

public class ClusterServer implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClusterServer.class);
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private ByteBuffer buffer;
    private ByteBuffer totalBuffer;
    private ClusterConfig cluster = BuguIndex.getInstance().getClusterConfig();

    public void run() {
        try {
            init();
        } catch (IOException ex) {
            LOGGER.error("Error when init cluster server", ex);
        }
        loop();
    }

    private void init() throws IOException {
        this.selector = Selector.open();
        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.configureBlocking(false);
        SocketAddress address = new InetSocketAddress(this.cluster.getServerPort());
        this.serverChannel.socket().bind(address);
        this.serverChannel.register(this.selector, 16);

        this.buffer = ByteBuffer.allocate(this.cluster.getBufferSize());
        this.totalBuffer = ByteBuffer.allocate(this.cluster.getMaxEntitySize());
    }

    private void loop() {
        while (true) {
            int i = 0;
            try {
                i = this.selector.select();
            } catch (IOException ex) {
                LOGGER.error("Error when selecting", ex);
            }
            if (i == 0) {
                continue;
            }
            Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
                it.remove();
            }
        }
    }

    private void handleAccept(SelectionKey key) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        try {
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            sc.register(this.selector, 1);
        } catch (IOException ex) {
            LOGGER.error("Error when handle accept", ex);
        }
    }

    private void handleRead(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        int count = -1;
        try {
            while ((count = sc.read(this.buffer)) >= 0) {
                if (count > 0) {
                    this.buffer.flip();
                    this.totalBuffer.put(this.buffer);
                    this.buffer.clear();
                }
            }
            this.totalBuffer.flip();
            ClusterMessage message = (ClusterMessage) BufferUtil.fromBuffer(this.totalBuffer);
            this.totalBuffer.clear();
            if (message != null) {
                HandleMessageTask task = new HandleMessageTask(message);
                this.cluster.getExecutor().execute(task);
            }
        } catch (IOException ex) {
            LOGGER.error("Error when handle read", ex);
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Error when handle read", ex);
        } finally {
            try {
                sc.close();
            } catch (IOException ex) {
                LOGGER.error("Error when close SocketChannel", ex);
            }
        }
    }

    public void close() {
        try {
            this.serverChannel.close();
            this.selector.close();
        } catch (IOException ex) {
            LOGGER.error("Error when close cluster server", ex);
        }
    }
}