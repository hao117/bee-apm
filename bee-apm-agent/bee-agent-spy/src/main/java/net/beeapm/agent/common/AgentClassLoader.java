
package net.beeapm.agent.common;

import net.beeapm.agent.log.LogUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author yuan
 * @date 2018-08-12
 */
public class AgentClassLoader extends ClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    private List<File> jarPathDir;
    private List<File> jarFiles;

    public AgentClassLoader(ClassLoader parent, String rootPath, String[] jarFolder) {
        super(parent);
        jarPathDir = new ArrayList<File>();
        for (int i = 0; i < jarFolder.length; i++) {
            jarPathDir.add(new File(rootPath + File.separator + jarFolder[i]));
        }
        jarFiles = getJarFiles();

    }

    private byte[] readClassFile(String jarPath, String className) throws Exception {
        className = className.replace('.', '/').concat(".class");
        URL classFileUrl = new URL("jar:file:" + jarPath + "!/" + className);
        byte[] data = null;
        BufferedInputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new BufferedInputStream(classFileUrl.openStream());
            baos = new ByteArrayOutputStream();
            int ch = 0;
            while ((ch = is.read()) != -1) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } finally {
            close(is);
            close(baos);
        }
        return data;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        for (File file : jarFiles) {
            try {
                JarFile jar = new JarFile(file);
                JarEntry entry = jar.getJarEntry(path);
                if (entry != null) {
                    byte[] data = readClassFile(file.getAbsolutePath(), name);
                    return defineClass(name, data, 0, data.length);
                }
            } catch (Exception e) {
                LogUtil.log("查找类异常" + name, e);
            }
        }
        throw new ClassNotFoundException("Can't find " + name);
    }

    @Override
    protected URL findResource(String name) {
        try {
            for (File file : jarFiles) {
                JarFile jar = new JarFile(file);
                JarEntry entry = jar.getJarEntry(name);
                if (entry != null) {
                    try {
                        return new URL("jar:file:" + file.getAbsolutePath() + "!/" + name);
                    } catch (MalformedURLException e) {
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.log("查找资源异常" + name, e);
        }
        return null;
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        List<URL> allResources = new LinkedList<URL>();
        for (File file : jarFiles) {
            JarFile jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry(name);
            if (entry != null) {
                allResources.add(new URL("jar:file:" + file.getAbsolutePath() + "!/" + name));
            }
        }

        final Iterator<URL> iterator = allResources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }

    private List<File> getJarFiles() {
        jarFiles = new ArrayList<File>(16);
        for (File dir : jarPathDir) {
            if (dir.exists() && dir.isDirectory()) {
                String[] jarFileNames = dir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jar");
                    }
                });
                for (String fileName : jarFileNames) {
                    File file = new File(dir, fileName);
                    if (file.exists()) {
                        jarFiles.add(file);
                    }
                }
            }
        }
        return jarFiles;
    }

    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

}
