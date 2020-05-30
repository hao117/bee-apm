package net.beeapm.agent.common;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author yuan
 * @date 2018-08-27
 */
public class BeeUtils {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final Set<Class<?>> wrapperPrimitiveMap = new HashSet<Class<?>>(17);
    private static final int ACCESS_ABSTRACT = 0x0400;
    private static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String rootPath;

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


    /**
     * 方法是否抽象方法
     *
     * @param access
     * @return
     */
    public static boolean isAbstract(final int access) {
        return (access & ACCESS_ABSTRACT) != 0;
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
        if(rootPath == null) {
            synchronized (BeeUtils.class){
                if(rootPath != null){
                    return rootPath;
                }
                String path = BeeUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
                File file = new File(path);
                rootPath = file.getParent();
            }
        }
        return rootPath;
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

    public static List<Class<?>> getAllSuperclasses(final Class<?> cls) {
        if (cls == null) {
            return null;
        }
        final List<Class<?>> classes = new ArrayList<>();
        Class<?> superclass = cls.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }

    /**
     * 所有可执行方法
     *
     * @param cls
     * @return
     */
    public static List<Method> getAllMethod(final Class<?> cls) {
        List<Method> methodList = new ArrayList<>(8);
        Method[] methodArray = cls.getDeclaredMethods();
        for (Method m : methodArray) {
            if (!isAbstract(m.getModifiers())) {
                methodList.add(m);
            }
        }
        List<Class<?>> superclassList = getAllSuperclasses(cls);
        for (final Class<?> klass : superclassList) {
            for (Method m : klass.getDeclaredMethods()) {
                if (!isAbstract(m.getModifiers())) {
                    methodList.add(m);
                }
            }
        }
        return methodList;
    }

    /**
     * 简单的日志输出
     *
     * @param s
     * @param t
     * @param fileName
     */
    public static void write(String s, Throwable t, String fileName) {
        StringBuilder msg = new StringBuilder();
        msg.append(dateFmt.format(new Date())).append(" ").append(s).append(" : ");
        if (t != null) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            t.printStackTrace(new java.io.PrintWriter(buf, true));
            msg.append(buf.toString());
            try {
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter writer = null;
        try {
            File out = new File(getJarDirPath() + "/logs/" + fileName);
            if (!out.exists()) {
                out.createNewFile();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out, true), "UTF-8"));
            writer.write(msg.toString());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }


}
