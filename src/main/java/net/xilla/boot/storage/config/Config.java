package net.xilla.boot.storage.config;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.xilla.boot.api.storage.ConfigFile;
import net.xilla.boot.log.Logger;
import net.xilla.boot.reflection.ObjectProcessor;
import net.xilla.boot.storage.file.FileLoader;
import net.xilla.boot.storage.file.FileSection;

import java.io.IOException;

public class Config implements ConfigFile {

    @Getter
    private transient String name;

    private transient FileLoader loader;

    public Config(String name, FileLoader loader) {
        this.name = name;
        this.loader = loader;
    }

    public Config(FileLoader loader) {
        this(loader.getFile().getAbsolutePath(), loader);
    }

    public void save() {
        FileSection data = loader.get("data");
        if(data == null) {
            Logger.info("Creating FileLoader");
            data = new FileSection("data", -1, -1);
            loader.put(data);
        }

        Logger.info("Saving file " + loader.getFile().getAbsolutePath());

        if(!loader.getFile().exists()) {
            Logger.info("Creating file");
            try {
                loader.getFile().getParentFile().mkdirs();
                loader.getFile().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonObject json = ObjectProcessor.toJson(this);
        data.setData(json);

        try {
            loader.saveSections();
        } catch (FileLoader.FileException e) {
            e.printStackTrace();
        }
        Logger.info("Saving to file: Loader data " + loader.keySet());
        Logger.info("New data " + json);
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void load() {
        if(loader.getFile().exists()) {
            try {
                loader.readFile();
                FileSection data = loader.get("data");
                loader.loadData(data);

                ObjectProcessor.fillFields(data.getData(), this.getClass(), this);
            } catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Logger.info("Config file (" + loader.getFile() + ") does not exist, creating default!");
            save();
        }
    }

}
