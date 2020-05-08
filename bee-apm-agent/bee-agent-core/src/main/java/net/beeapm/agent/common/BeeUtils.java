package net.beeapm.agent.common;

import org.apache.commons.lang3.ClassUtils;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;

/**
 * @author yuan
 * @date 2018-08-27
 */
public class BeeUtils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static void flush(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (Exception e) {
            }
        }
    }

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean isPrimitive(Object obj) {
        if (obj == null || obj instanceof String) {
            return true;
        }
        return ClassUtils.isPrimitiveOrWrapper(obj.getClass());
    }

    public static void shutdown(ExecutorService service) {
        if (service != null) {
            try {
                service.shutdown();
            } catch (Throwable e) {
            }
        }
    }

    public static String getJarDirPath() {
        return new File(BeeUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile())
                .getParent();
    }

}
