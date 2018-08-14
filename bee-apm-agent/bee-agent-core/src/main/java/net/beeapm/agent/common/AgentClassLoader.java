/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.beeapm.agent.common;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AgentClassLoader extends ClassLoader {

    private List<File> jarPathDir;
    private List<File> jarFiles;

    public AgentClassLoader(ClassLoader parent,String[] jarFolder){
        super(parent);
        jarPathDir = new ArrayList<File>();
        for(int i = 0; i < jarFolder.length; i++) {
            jarPathDir.add(new File(BeeAgentJarUtils.getAgentJarDirPath() + "/"+jarFolder[i]));
        }
        jarFiles = getJarFiles();

    }

    private byte[] readClassFile(String jarPath,String className) throws Exception{
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
            if (is != null)
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException ignored) {
                }
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
                    byte[] data = readClassFile(file.getAbsolutePath(),name);
                    return defineClass(name, data, 0, data.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        }catch (Exception e){
            e.printStackTrace();
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
                    if(file.exists()){
                        jarFiles.add(file);
                    }
                }
            }
        }
        return jarFiles;
    }

}
