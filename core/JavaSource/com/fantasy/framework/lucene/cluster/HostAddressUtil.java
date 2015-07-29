package com.fantasy.framework.lucene.cluster;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

public class HostAddressUtil {

    private static final Logger LOGGER = Logger.getLogger(HostAddressUtil.class);

    public static List<String> getLocalAddresses() {
        List<String> list = new ArrayList<String>();
        Enumeration<NetworkInterface> interfaceList = null;
        try {
            interfaceList = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex) {
            LOGGER.error("Error when getting local network interfaces", ex);
        }
        if (interfaceList != null) {
            while (interfaceList.hasMoreElements()) {
                NetworkInterface face = (NetworkInterface) interfaceList.nextElement();
                Enumeration<InetAddress> addressList = face.getInetAddresses();
                if (addressList == null) {
                    continue;
                }
                while (addressList.hasMoreElements()) {
                    InetAddress address = (InetAddress) addressList.nextElement();
                    list.add(address.getHostAddress());
                }
            }
        }
        return list;
    }

}
