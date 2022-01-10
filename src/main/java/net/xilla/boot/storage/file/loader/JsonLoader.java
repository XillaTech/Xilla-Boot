package net.xilla.boot.storage.file.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.xilla.boot.storage.file.FileLoader;
import net.xilla.boot.storage.file.FileSection;

import java.beans.JavaBean;
import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

/***
 * The FileLoader class is used to efficiently read
 * files. Using a buffered reader and only keeping a
 * cache of where objects are located.
 *
 * Best coupled with a data manager that also works as
 * a cache. As constant read/write's can degrade disk
 * health and application performance over time.
 */
public class JsonLoader extends ConcurrentHashMap<String, FileSection> implements FileLoader {

    private File file;

    private File tempFile;

    public JsonLoader(String filePath) {
        this.file = new File(filePath);
        this.tempFile = new File(filePath + ".temp");
    }

    public void put(FileSection object) {
        super.put(object.getKey(), object);
    }

    @Override
    public void remove(String key) {
        super.remove(key);
    }

    // Writing to the file

    public void saveSections() throws FileException {
        tempFile.delete();

        try(FileWriter fileWriter = new FileWriter(tempFile)) {
            for(FileSection section : values()) {
                try {
                    writeSection(fileWriter, section);
                } catch (IOException e) {
                    throw new FileException("Failed to write to the temporary file!", e);
                }
            };
        } catch (IOException e) {
            throw new FileException("Failed to open the file writer for the temporary file!", e);
        }

        if(!file.delete() && file.exists()) {
            throw new FileException("Failed to delete the original file!");
        }
        if (!tempFile.renameTo(file))
            throw new FileException("Failed to rename temporary file!");
    }

    private void writeSection(FileWriter writer, FileSection section) throws IOException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        String json = gson.toJson(section);

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        jsonObject.add("data", section.getData());
        writer.append(gson.toJson(jsonObject)).append(",\n");
    }

    // Reading from the file

    public void readFile() throws IOException {
        System.out.println("File " + file);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            if(!file.createNewFile()) {
                throw new IOException("Failed to create file " + file);
            }
        }

        FileInputStream stream = new FileInputStream(file);

        StringBuilder builder = new StringBuilder();
        StringBuilder tempBuffer = new StringBuilder();
        int start = 0;

        int index = 0;
        while(stream.available() > 0) {

            char character = (char)stream.read();

            builder.append(character);

            tempBuffer.append(character);
            if(tempBuffer.length() > 3) {
                tempBuffer.deleteCharAt(0);
            }

            if(tempBuffer.toString().endsWith("\n},")) {

                String json = builder.substring(0, builder.length() - 1);

                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                FileSection section = gson.fromJson(json, FileSection.class);
                section.setFileLoader(this);
                section.setStart(start);
                section.setEnd(index);

                put(section);

                builder = new StringBuilder();
                start = index + 1;
            }
            index++;
        }
    }

    public FileSection loadData(FileSection section) throws IOException {
        return readSectionWithData(section.getStart(), section.getEnd());
    }

    public FileSection readSectionWithData(int start, int end) throws IOException {
        FileInputStream stream = new FileInputStream(file);

        StringBuilder builder = new StringBuilder();

        for(int i = start; i < end; i++) {

            char character = (char)stream.read();

            builder.append(character);
        }
        String json = builder.substring(0, builder.length());

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);

        FileSection section = gson.fromJson(json, FileSection.class);
        section.setFileLoader(this);
        section.setStart(start);
        section.setEnd(end);
        section.setData(jsonObject.getAsJsonObject("data"));
        return section;
    }

    @Override
    public File getFile() {
        return file;
    }

}
