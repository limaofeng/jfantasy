package com.fantasy.framework.httpclient;

import com.fantasy.framework.error.IgnoreException;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * HttpClient连接单向Https
 * 默认接收所有证书验证的类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-8-13 下午03:11:53
 */
public class DefaultProtocolSocketFactory implements ProtocolSocketFactory {

    private final static Log LOG = LogFactory.getLog(DefaultProtocolSocketFactory.class);

    private SSLContext sslcontext = null;

    private SSLContext createSSLContext() {
        try {
            SSLContext newSslcontext = SSLContext.getInstance("SSL");
            newSslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            return newSslcontext;
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage(), e);
            throw new IgnoreException(e.getMessage());
        } catch (KeyManagementException e) {
            LOG.error(e.getMessage(), e);
            throw new IgnoreException(e.getMessage());
        }
    }

    private SSLContext getSSLContext() {
        if (this.sslcontext == null) {
            this.sslcontext = createSSLContext();
        }
        return this.sslcontext;
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket(String host, int port) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
            return socket;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

}
