package net.xilla.boot.reflection.scanner;

import net.xilla.boot.XillaApplication;
import net.xilla.boot.log.Logger;
import net.xilla.boot.reflection.ClassScan;
import net.xilla.boot.reflection.ClassScanner;
import net.xilla.boot.reflection.ObjectProcessor;
import net.xilla.boot.reflection.annotation.JsonManager;
import net.xilla.boot.storage.file.loader.JsonLoader;
import net.xilla.boot.storage.manager.Manager;

import java.lang.annotation.Annotation;

public class JsonManagerScanner implements ClassScan {
    @Override
    public boolean scan(Class clazz) {
        Logger.debug("Checking to see if class is a json manager: " + clazz.getName());
        Annotation[] annotations = clazz.getAnnotationsByType(JsonManager.class);
        if(annotations.length > 0) {
            JsonManager annotation = (JsonManager) annotations[0];
            Manager manager = new Manager(clazz, new JsonLoader(annotation.fileName()));
            XillaApplication.getInstance().registerManager(manager, ClassScanner.getPriority(clazz));
            return true;
        }
        return false;
    }
}
