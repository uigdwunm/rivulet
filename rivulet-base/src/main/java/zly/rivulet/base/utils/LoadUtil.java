package zly.rivulet.base.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LoadUtil {

    public static List<Class<?>> scan(String basePackage) {
        try {
            //通过当前线程得到类加载器从而得到URL的枚举
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(basePackage.replace(".", "/"));

            // 收集所有全路径类名
            List<String> classNames = new ArrayList<>();
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                if(url.getProtocol().equals("file")) {
                    // src
                    collectFileClassName(basePackage, url, classNames);
                } else if(url.getProtocol().equals("jar")) {
                    // jar
                    collectJarClassName(basePackage, url, classNames);
                }
            }

            List<Class<?>> result = new ArrayList<>();
            for (String className : classNames) {
                result.add(Class.forName(className));
            }
            return result;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void collectJarClassName(String basePackage, URL url, Collection<String> classNames) throws IOException {
        String protocol = url.getProtocol();//大概是jar
        if ("jar".equalsIgnoreCase(protocol)) {
            //转换为JarURLConnection
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            if (connection != null) {
                JarFile jarFile = connection.getJarFile();
                if (jarFile != null) {
                    //得到该jar文件下面的类实体
                    Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                    while (jarEntryEnumeration.hasMoreElements()) {
                        /*entry的结果大概是这样：
                                org/
                                org/junit/
                                org/junit/rules/
                                org/junit/runners/*/
                        JarEntry entry = jarEntryEnumeration.nextElement();
                        if (entry.isDirectory()) {
                            continue;
                        }

                        String jarEntryName = entry.getName();
                        if (!jarEntryName.contains(".class")  ) {
                            continue;
                        }
                        jarEntryName = jarEntryName.replaceAll("/", ".");
                        jarEntryName = jarEntryName.replaceAll("\\\\", ".");
                        int basePackageIndex = jarEntryName.lastIndexOf(basePackage);
                        if (basePackageIndex < 0) {
                            continue;
                        }

                        String className = jarEntryName.substring(basePackageIndex, jarEntryName.length() - 6);
                        classNames.add(className);
                    }
                }
            }
        }
    }

    private static void collectFileClassName(String basePackage, URL url, Collection<String> classFileNames) {
        listFiles(basePackage, new File(url.getFile()), classFileNames);
        
    }
    private static void listFiles(String basePackage, File file, Collection<String> classNames) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File f : files) {
                listFiles(basePackage, f, classNames);
            }
        } else {
            String path = file.getPath();
            if (path.endsWith(".class")) {
                path = path.replaceAll("/", ".");
                path = path.replaceAll("\\\\", ".");
                int basePackateIndex = path.lastIndexOf(basePackage);
                if (basePackateIndex < 0) {
                    return;
                }

                String className = path.substring(basePackateIndex, path.length() - 6);
                classNames.add(className);
            }
        }
    }
}
