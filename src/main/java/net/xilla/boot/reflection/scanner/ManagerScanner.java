package net.xilla.boot.reflection.scanner;

import net.xilla.boot.XillaApplication;
import net.xilla.boot.log.Logger;
import net.xilla.boot.reflection.ClassScan;
import net.xilla.boot.reflection.ClassScanner;
import net.xilla.boot.reflection.ObjectProcessor;
import net.xilla.boot.storage.manager.Manager;

import java.lang.reflect.InvocationTargetException;

public class ManagerScanner implements ClassScan {
    @Override
    public boolean scan(Class clazz) {
        Logger.debug("Checking to see if class is a manager: " + clazz.getName());
        if(clazz.getSuperclass() != null && clazz.getSuperclass().equals(Manager.class)) {
            Logger.info("Registering manager from " + clazz.getName());
            Manager manager = loadManager(clazz);
            if(manager != null) {
                Logger.info("Registered manager " + manager);
                XillaApplication.getInstance().registerManager(manager, ClassScanner.getPriority(manager.getClass()));
                return true;
            }
        }
        return false;
    }

    private Manager loadManager(Class clazz) {
        for (var constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                try {
                    Manager manager = (Manager) constructor.newInstance();
                    return manager;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        return null;
    }

}
