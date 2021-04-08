package net.beeapm.test;

import java.net.InetAddress;

/**
 * @author yuanlong.chen
 * @date 2020/07/30
 */
public class TestPing {

    public static void main(String[] args) {

    }
    public static void ping(){
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            System.out.println(address.isReachable(5000));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
