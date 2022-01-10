package net.xilla.boot.storage.setting;

import net.xilla.boot.reflection.ObjectProcessor;
import net.xilla.boot.storage.file.FileLoader;
import net.xilla.boot.storage.file.loader.JsonLoader;

import java.io.IOException;
import java.util.Set;

public class Settings {

    private String name;

    private FileLoader loader;

    private Class<? extends Settings> clazz;

    public Settings(String name, FileLoader loader, Class<? extends Settings> clazz) {
        this.name = name;
        this.loader = loader;
        this.clazz = clazz;
    }

    public Settings(String name, String file, Class<? extends Settings> clazz) {
        this(name, new JsonLoader(file), clazz);
    }

    public Settings(String file, Class<? extends Settings> clazz) {
        this(clazz.getName(), file, clazz);
    }

    public void load() {
        try {
            loader.readFile();
            ObjectProcessor.fillFields(loader.get("settings").getData(), Settings.class, this);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
