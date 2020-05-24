package net.beeapm.agent.common;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author yuan
 * @date 2018-08-27
 */
public class BeeUtils {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final Set<Class<?>> wrapperPrimitiveMap = new HashSet<Class<?>>(17);

    static {
        wrapperPrimitiveMap.add(Boolean.class);
        wrapperPrimitiveMap.add(Byte.class);
        wrapperPrimitiveMap.add(Character.class);
        wrapperPrimitiveMap.add(Short.class);
        wrapperPrimitiveMap.add(Integer.class);
        wrapperPrimitiveMap.add(Long.class);
        wrapperPrimitiveMap.add(Double.class);
        wrapperPrimitiveMap.add(Float.class);
    }


    public static boolean isPrimitiveWrapper(final Class<?> type) {
        return wrapperPrimitiveMap.contains(type);
    }


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

    /**
     * 包含String和封装的基本类型
     *
     * @param obj
     * @return
     */
    public static boolean isPrimitive(Object obj) {
        if (obj == null || obj instanceof String) {
            return true;
        }
        Class<?> type = obj.getClass();
        return type.isPrimitive() || isPrimitiveWrapper(type);
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

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String join(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static String[] split(final String str, final char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    /**
     * from commons-lang3
     *
     * @param str
     * @param separatorChar
     * @param preserveAllTokens
     * @return
     */
    private static String[] splitWorker(final String str, final char separatorChar, final boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<String>(8);
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }


}
