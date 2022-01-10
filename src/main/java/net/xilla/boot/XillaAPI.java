package net.xilla.boot;

import net.xilla.boot.storage.config.Config;
import net.xilla.boot.storage.manager.Manager;

public class XillaAPI {

    public static <T extends Manager> T getManager(String name) {
        return XillaApplication.getInstance().getController().getManager(name);
    }

    public static <T extends Manager> T getManager(Class clazz) {
        return XillaApplication.getInstance().getController().getManager(clazz);
    }

    public static <T extends Config> T getConfig(String name) {
        return XillaApplication.getInstance().getController().getConfig(name);
    }

    public static <T extends Config> T getConfig(Class clazz) {
        return XillaApplication.getInstance().getController().getConfig(clazz);
    }

    public static <T> T getObject(Class<T> clazz, String key) {
        Manager<T> manager = getManager(clazz);
        return manager.get(key);
    }

    public static <T> T getObject(String managerName, String key) {
        Manager<T> manager = getManager(managerName);
        return manager.get(key);
    }

}
