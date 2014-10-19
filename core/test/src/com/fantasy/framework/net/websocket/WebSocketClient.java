package com.fantasy.framework.net.websocket;

import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebSocketClient {

    @Test
    public void test() throws Exception {
        Socket socket = null;
        try {
            socket = new Socket("192.168.1.112", 9090);
            OutputStream out = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.write(("GET /test HTTP/1.1\n" +
                    "Host: 192.168.1.112:9090\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                    "Accept-Language: zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Sec-WebSocket-Version: 13\n" +
                    "Origin: null\n" +
                    "Sec-WebSocket-Key: NoFqE7mbCv7lgIrjz4/0og==\n" +
                    "Connection: keep-alive, Upgrade\n" +
                    "Pragma: no-cache\n" +
                    "Cache-Control: no-cache\n" +
                    "Upgrade: websocket\n\n").getBytes());
            out.flush();
            String line;
            while ((line = in.readLine())!=null){
                System.out.print(line);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
//        Thread.currentThread().sleep(2000*1000);
//        HttpClient httpClient = new HttpClient();
//        httpClient.setConnectTimeoutMillis(60 * 1000);
//        IWebSocketConnection webSocketConnection = httpClient.openWebSocketConnection("ws://127.0.0.1:9090/test" , "Sample", new WebSocketHandler());
//        IHttpResponseHeader responseHeader = webSocketConnection.getUpgradeResponseHeader();

    }

}
