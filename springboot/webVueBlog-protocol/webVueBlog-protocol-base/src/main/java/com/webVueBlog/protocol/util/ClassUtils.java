package com.webVueBlog.protocol.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {

    public static List<Class> getClassList(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class> classList = getClassList(packageName);
        classList.removeIf(next -> !next.isAnnotationPresent(annotationClass));
        return classList;
    }

    public static List<Class> getClassList(String packageName) {
        List<Class> classList = new LinkedList<>();
        String path = packageName.replace(".", "/");
        try {
            Enumeration<URL> urls = ClassUtils.getClassLoader().getResources(path);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                if (url != null) {
                    String protocol = url.getProtocol();

                    if (protocol.equals("file")) {
                        addClass(classList, url.toURI().getPath(), packageName);

                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();

                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {

                            JarEntry jarEntry = jarEntries.nextElement();
                            String entryName = jarEntry.getName();

                            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                                String className = entryName.substring(0, entryName.lastIndexOf(".")).replaceAll("/", ".");
                                addClass(classList, className);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Initial class error!");
        }
        return classList;
    }

    private static void addClass(List<Class> classList, String packagePath, String packageName) {
        try {
            File[] files = new File(packagePath).listFiles(file -> (file.isDirectory() || file.getName().endsWith(".class")));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = fileName.substring(0, fileName.lastIndexOf("."));
                        if (packageName != null) {
                            className = packageName + "." + className;
                        }
                        addClass(classList, className);
                    } else {
                        String subPackagePath = fileName;
                        if (packageName != null) {
                            subPackagePath = packagePath + "/" + subPackagePath;
                        }
                        String subPackageName = fileName;
                        if (packageName != null) {
                            subPackageName = packageName + "." + subPackageName;
                        }
                        addClass(classList, subPackagePath, subPackageName);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addClass(List<Class> classList, String className) {
        classList.add(loadClass(className, false));// 加载类，但不初始化
    }

    public static Class loadClass(String className, boolean isInitialized) {
        try {
            return Class.forName(className, isInitialized, getClassLoader());// 使用当前线程的类加载器加载类
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();// 获取当前线程的类加载器
    }

    public static Class getGenericType(Field f) {
        Class typeClass = f.getType();// 获取字段的类型
        if (Collection.class.isAssignableFrom(typeClass)) {
            return (Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];// 获取集合的泛型类型
        } else if (Map.class.isAssignableFrom(typeClass)) {
            return (Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[1];// 获取Map的泛型类型
        } else if (typeClass.isArray()) {
            return typeClass.getComponentType();// 获取数组的元素类型
        }
        return typeClass;
    }
}
