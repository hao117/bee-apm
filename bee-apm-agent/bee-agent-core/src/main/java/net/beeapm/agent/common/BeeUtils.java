package net.beeapm.agent.common;

import java.io.Closeable;

public class BeeUtils {
    public static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            }catch (Exception e){
            }
        }
    }
}
