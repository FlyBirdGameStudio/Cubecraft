package io.flybird.util;

import io.flybird.cubecraft.client.Cubecraft;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectHelper {
    public static Iterator<Class<?>> getAllClass(){
        Class<?> classLoaderClass = Cubecraft.class.getClassLoader().getClass();
        Field classLoaderField=null;
        try {
            classLoaderField = classLoaderClass.getDeclaredField("classes");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //让我访问
        classLoaderField.setAccessible(true);
        //get all classes in jvm
        Vector<Class<?>> classes = null;
        try {
            classes = (Vector<Class<?>>) classLoaderField.get(ReflectHelper.class.getClassLoader());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return classes.iterator();
    }

    public static void loadJar(String jarPath){
        try {
            for (String s:loadJarFromAbsolute(jarPath).keySet()){
                try {
                    ClassLoader.getPlatformClassLoader().loadClass(s);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Class<?>> loadJarFromAbsolute(String path) throws IOException {
        JarFile jar = new JarFile(path);
        Enumeration<JarEntry> entryEnumeration = jar.entries();
        Map<String, Class<?>> clazzMap = new HashMap<>(16);
        while (entryEnumeration.hasMoreElements()) {
            JarEntry entry = entryEnumeration.nextElement();
            // 先获取类的名称，符合条件之后再做处理，避免处理不符合条件的类
            String clazzName = entry.getName();
            if (clazzName.endsWith(".class")) {
                // 去掉文件名的后缀
                clazzName = clazzName.substring(0, clazzName.length() - 6);
                // 替换分隔符
                clazzName = clazzName.replace("/", ".");
                // 加载类,如果失败直接跳过
                try {
                    Class<?> clazz = Class.forName(clazzName);
                    // 将类名称作为键，类Class对象作为值存入mao
                    // 因为类名存在重复的可能，所以这里的类名是带包名的
                    clazzMap.put(clazzName, clazz);
                } catch (Throwable e) {
                    // 这里可能出现有些类是依赖不全的，直接跳过，不做处理，也没法做处理
                }
            }
        }
        return clazzMap;
    }
}
