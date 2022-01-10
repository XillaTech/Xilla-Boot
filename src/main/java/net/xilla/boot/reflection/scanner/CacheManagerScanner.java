package net.xilla.boot.reflection.scanner;

import net.xilla.boot.XillaApplication;
import net.xilla.boot.log.Logger;
import net.xilla.boot.reflection.ClassScan;
import net.xilla.boot.reflection.ClassScanner;
import net.xilla.boot.reflection.annotation.CacheManager;
import net.xilla.boot.storage.manager.Manager;

import java.lang.annotation.Annotation;

public class CacheManagerScanner implements ClassScan {
    @Override
    public boolean scan(Class clazz) {
        Logger.debug("Checking to see if class is a cache manager: " + clazz.getName());
        Annotation[] annotations = clazz.getAnnotationsByType(CacheManager.class);
        if(annotations.length > 0) {
            CacheManager annotation = (CacheManager) annotations[0];
            Manager manager = new Manager(clazz);
            XillaApplication.getInstance().registerManager(manager, ClassScanner.getPriority(clazz));
            return true;
        }
        return false;
    }
}
