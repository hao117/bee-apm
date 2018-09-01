package net.beeapm.agent.common;

import java.io.Closeable;
import java.io.Flushable;

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

}
