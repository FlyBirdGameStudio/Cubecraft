package io.flybird.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class DeployJar {
    // 后缀
    private final static String CLAZZ_SUFFIX = ".class";

    // 类加载器
    private ClassLoader classLoader;

    public void loadPath(String jarPath) {
        try {
            File jarFiles = new File(jarPath);

            File[] jarFilesArr = jarFiles.listFiles();
            URL[] jarFilePathArr = new URL[jarFilesArr.length];
            int i = 0;
            for (File jarfile : jarFilesArr) {
                String jarname = jarfile.getName();
                if (!jarname.contains(".jar")) {
                    continue;
                }
                String jarFilePath = "file:\\" + jarPath + File.separator
                        + jarname;
                jarFilePathArr[i] = new URL(jarFilePath);
                i++;
            }

            classLoader = new URLClassLoader(jarFilePathArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadJar(String jarName) {
        if (!jarName.contains(".jar")) {
            return;
        }
        try {
            JarFile jarFile = new JarFile(jarName);
            Enumeration<JarEntry> em = jarFile.entries();
            while (em.hasMoreElements()) {
                JarEntry jarEntry = em.nextElement();
                String clazzFile = jarEntry.getName();

                if (!clazzFile.endsWith(CLAZZ_SUFFIX)) {
                    continue;
                }
                String clazzName = clazzFile.substring(0,
                        clazzFile.length() - CLAZZ_SUFFIX.length()).replace(
                        '/', '.');
                System.out.println(clazzName);

                // loadClass(clazzName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object loadClass(String clazzName) {
        if (this.classLoader == null) {
            return null;
        }
        Class clazz = null;
        try {
            clazz = this.classLoader.loadClass(clazzName);
            Object obj = clazz.newInstance();
            return obj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}