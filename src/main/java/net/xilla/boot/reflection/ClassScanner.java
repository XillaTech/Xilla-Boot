package net.xilla.boot.reflection;

import lombok.Setter;
import net.xilla.boot.api.program.ProgramManager;
import net.xilla.boot.api.program.StartupPriority;
import net.xilla.boot.log.Logger;
import net.xilla.boot.reflection.annotation.CacheManager;
import net.xilla.boot.reflection.annotation.StartPriority;
import net.xilla.boot.reflection.annotation.JsonManager;
import net.xilla.boot.storage.file.loader.JsonLoader;
import net.xilla.boot.storage.manager.Manager;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ClassScanner {

    @Setter
    private static ClassLoader classLoader = ClassLoader.getSystemClassLoader();

//    private static List<String> packages = new ArrayList<>(Collections.singleton("net.xilla.boot"));
//
//    public static void registerApplication(Class clazz) {
//        packages.add(clazz.getPackageName());
//    }
//
//    public static void unregisterApplication(Class clazz) {
//        packages.remove(clazz.getPackageName());
//    }
//
//    private boolean isRegistered(Package packge) {
//        for(String packageName : packages) {
//            if(packge.getName().equals(packageName))
//                return true;
//        }
//        return false;
//    }

    private ProgramManager programManager;

    private static List<ClassScan> scans = new ArrayList<>();

    public static void addScan(ClassScan scan) {
        scans.add(scan);
    }

    public static void removeScan(ClassScan scan) {
        scans.remove(scan);
    }

    public ClassScanner(ProgramManager programManager) {
        this.programManager = programManager;
    }

    public void load() {
        for(var pack : getRoot().getDefinedPackages()) {
            try {
                Set<Class> classes = findAllClassesUsingReflectionsLibrary(pack.getName());
//                System.out.println("Searched package " + pack);
//                System.out.println("Found classes " + classes);
                if (classes.size() > 0) {

                    for (var clazz : classes) {
                        scans.forEach((scan) -> scan.scan(clazz));
                    }
                }
            } catch (NoClassDefFoundError ex) {
                ex.printStackTrace();
            }
        }
    }

    public static StartupPriority getPriority(Class clazz) {
        Annotation[] annotations = clazz.getAnnotationsByType(StartPriority.class);
        if(annotations.length == 0) return StartupPriority.MANAGER;
        return ((StartPriority) annotations[0]).priority();
    }

    public Set<Class> findAllClassesUsingReflectionsLibrary(String packageName) throws NoClassDefFoundError {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .collect(Collectors.toSet());
    }

    //
//    public Set<Class> findAllClassesUsingClassLoader(String packageName) {
//        InputStream stream = ClassLoader.getSystemClassLoader()
//                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//        return reader.lines()
//                .filter(line -> line.endsWith(".class"))
//                .map(line -> getClass(line, packageName))
//                .collect(Collectors.toSet());
//    }
//
//    private Class getClass(String className, String packageName) {
//        try {
//            return Class.forName(packageName + "."
//                    + className.substring(0, className.lastIndexOf('.')));
//        } catch (ClassNotFoundException e) {
//            // handle the exception
//        }
//        return null;
//    }

    private ClassLoader getRoot() {
        return classLoader;
    }

}
