package net.xilla.boot.reflection.scanner;

import net.xilla.boot.XillaApplication;
import net.xilla.boot.log.Logger;
import net.xilla.boot.reflection.ClassScan;
import net.xilla.boot.reflection.ClassScanner;
import net.xilla.boot.storage.config.Config;
import net.xilla.boot.storage.manager.Manager;

import java.lang.reflect.InvocationTargetException;

public class ConfigScanner implements ClassScan {

    @Override
    public boolean scan(Class clazz) {
        Logger.debug("Checking to see if class is a config: " + clazz.getName());
        if(clazz.getSuperclass() != null && clazz.getSuperclass().equals(Config.class)) {
            Logger.debug("Loading manager from " + clazz.getName());
            Config config = loadConfig(clazz);
            if(config != null) {
                Logger.debug("Loaded config " + config);
                XillaApplication.getInstance().registerConfig(config, ClassScanner.getPriority(config.getClass()));
                return true;
            }
        }
        return false;
    }

    private Config loadConfig(Class clazz) {
        for (var constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                try {
                    Config config = (Config) constructor.newInstance();
                    return config;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        return null;
    }

}
