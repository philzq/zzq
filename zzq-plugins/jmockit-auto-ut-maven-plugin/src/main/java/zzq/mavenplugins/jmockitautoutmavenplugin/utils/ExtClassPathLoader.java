package zzq.mavenplugins.jmockitautoutmavenplugin.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 执行mvn dependency:list 获取项目所有的依赖jar
 *
 * URLClassLoader--->读取所有的jar中的class
 *
 * ClassPool--->加载所有的class
 *
 * ClassPool--->读取class并返回CtClass对象
 */
public class ExtClassPathLoader {

    private static Method addMethod;
    public static URLClassLoader classloader;
    public static ClassPool classPool;

    public static void init() {
        classloader = new URLClassLoader(new URL[0], ClassLoader.getPlatformClassLoader());
        Thread.currentThread().setContextClassLoader(classloader);
        classPool = new ClassPool(true);
    }

    public static void addClassPath(String path) {
        File file = new File(path);
        try {
            addMethod.invoke(classloader, file.toURI().toURL());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        try {
            classPool.appendClassPath(path);
        } catch (NotFoundException var3) {
            var3.printStackTrace();
        }

    }

    public static Class<?> getClass(String name) {
        try {
            return classloader.loadClass(name);
        } catch (ClassNotFoundException var4) {
            try {
                return Class.forName(name, false, classloader);
            } catch (ClassNotFoundException var3) {
                return null;
            }
        }
    }

    public static CtClass getCtClass(String name) {
        try {
            return classPool.getCtClass(name);
        } catch (NotFoundException var2) {
            return null;
        }
    }

    static {
        try {
            addMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addMethod.setAccessible(true);
        } catch (NoSuchMethodException var1) {
            var1.printStackTrace();
        }

    }


    public static void main(String[] args){
        ExtClassPathLoader.init();
        String path = "D:\\1.jar";//外部jar包的路径
        Set<Class<?>> classes = new LinkedHashSet<>();//所有的Class对象
        Map<Class<?>, Annotation[]> classAnnotationMap = new HashMap<>();//每个Class对象上的注释对象
        Map<Class<?>, Map<Method, Annotation[]>> classMethodAnnoMap = new HashMap<Class<?>, Map<Method,Annotation[]>>();//每个Class对象中每个方法上的注释对象
        ExtClassPathLoader.addClassPath("D:\\Users\\zhiqiangzhou\\.m2\\repository");//自己定义的loader路径可以找到
        try {
            JarFile jarFile = new JarFile(new File(path));
            URL url = new URL("file:" + path);
            Enumeration<JarEntry> es = jarFile.entries();
            Set<String> filterSet = new HashSet<>();
            filterSet.add("BOOT-INF");
            while (es.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) es.nextElement();
                String name = jarEntry.getName();
                System.out.println(name);
                if(name != null && !filterSet.contains(name.split("/")[0]) && name.endsWith(".class")){//只解析了.class文件，没有解析里面的jar包
                    //默认去系统已经定义的路径查找对象，针对外部jar包不能用
                    String className = name.replace("/", ".").substring(0, name.length() - 6);
                    Class<?> c = ExtClassPathLoader.getClass(className);
                    CtClass ctClass = ExtClassPathLoader.getCtClass("SystemPropertyUtils");
                    classes.add(c);
                    Annotation[] classAnnos = c.getDeclaredAnnotations();
                    classAnnotationMap.put(c, classAnnos);
                    Method[] classMethods = c.getDeclaredMethods();
                    Map<Method, Annotation[]> methodAnnoMap = new HashMap<Method, Annotation[]>();
                    for(int i = 0;i<classMethods.length;i++){
                        Annotation[] a = classMethods[i].getDeclaredAnnotations();
                        methodAnnoMap.put(classMethods[i], a);
                    }
                    classMethodAnnoMap.put(c, methodAnnoMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
