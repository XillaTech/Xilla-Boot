package net.xilla.boot.api.storage;

import com.google.gson.Gson;

import java.util.List;

public interface ConfigFile {

    // CONTROLS

    void save();

    void reload();

    void load();

}
