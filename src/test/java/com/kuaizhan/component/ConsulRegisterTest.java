package com.kuaizhan.component;

import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/5/5.
 */
public class ConsulRegisterTest {
    @Test
    public void getAddress() throws Exception {
//        ConsulRegister serviceRegister = new ConsulRegister();
//        System.out.println("---->" + serviceRegister.getAddress());
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println(i.getHostAddress());
            }
        }
    }

    @Test
    public void getPort() throws Exception {
        String content = "jetty.http.port=5555";
        Pattern pattern = Pattern.compile(".*jetty.http.port=(\\d+).*");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            System.out.println("---->" + matcher.group(1));
        }
    }

}