package net.beeapm.agent.common;

import java.io.Closeable;
import java.io.Flushable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class BeeUtils {
    public static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            }catch (Exception e){
            }
        }
    }

    public static void flush(Flushable flushable){
        if(flushable != null) {
            try {
                flushable.flush();
            } catch (Exception e) {
            }
        }
    }

    public static String getLocalIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
        }
        return null;
    }

}
