package net.xilla.boot.reflection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import net.xilla.boot.reflection.annotation.Ignored;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * The object processor contains all the serialization methods needed for objects
 * Allows you to serialize an object, and initialize a new object from that data.
 * This can be useful for data storage.
 */
public class ObjectProcessor {

    /**
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    /**
     *
     * @param object
     * @return
     */
    public static JsonObject toJson(Object object) {

        // TODO: Customize it to ignore ignored variables
        // TODO: then add support to rename stuff to make cleaner files

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJsonTree(object).getAsJsonObject();
    }

    /**
     * Converts JSON data into an object that has an empty constructor available
     *
     * @param json JSON Data
     * @param clazz Class to build from
     * @param <T> Final object type, keeps things cast-safe
     * @return Final object
     * @throws ProcessorException Encapsulated error if something happens
     */
    public static <T> T toObject(JsonObject json, Class<T> clazz) throws ProcessorException {
        try {
            T obj = initializeObject(clazz);
            fillFields(json, clazz, obj);
            return obj;
        } catch (Exception exception) {
            throw new ProcessorException("Failed to create object! Check cause.", exception);
        }
    }

    /**
     * Creates an empty base instance of the class using an empty constructor.
     *
     * @param clazz Class to build from
     * @param <T> Final object type, keeps things cast-safe
     * @return Initialized Object
     * @throws Exception Any errors during object creation
     */
    private static <T> T initializeObject(Class<T> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> c : constructors) {
            if (c.getParameterTypes().length == 0) {
                return (T) c.newInstance();
            }
        }
        throw new Exception("No matching constructors!");
    }

    /**
     * Fills the variables of an object using reflection. Pulls data from JSON.
     *
     * @param json
     * @param clazz
     * @param obj
     * @param <T>
     * @throws IllegalAccessException
     */
    public static <T> void fillFields(JsonObject json, Class clazz, Object obj) throws IllegalAccessException {
        Gson gson = new Gson();
        for(Field field : clazz.getDeclaredFields())
            if(checkField(field))
                setField(field, obj, gson.fromJson(json.get(getStorageName(field)), field.getType()));
    }

    /**
     *
     * @param field
     * @return
     */
    private static String getStorageName(Field field) {
        // add annotation check for customization
        return field.getName();
    }

    /**
     *
     * @param field
     * @param obj
     * @param value
     * @throws IllegalAccessException
     */
    private static void setField(Field field, Object obj, Object value) throws IllegalAccessException {
        boolean accessible = true;
        if(!field.isAccessible()) {
            accessible = false;
            field.setAccessible(true);
        }
        field.set(obj, value);
        if(!accessible) {
            field.setAccessible(false);
        }
    }

    /**
     *
     * @param field
     * @return
     */
    private static boolean checkField(Field field) {
        return !field.isAnnotationPresent(Ignored.class);
    }

    /**
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    /**
     *
     * @param object
     * @return
     */
    public static String getName(Object object) {
        try {
            Method method;
            String response;

            method = getMethod(object, "getKey");
            if(method != null) {
                response = method.invoke(object).toString();
                if(response != null) return response;
            }

            method = getMethod(object, "getName");
            if(method != null) {
                response = method.invoke(object).toString();
                if(response != null) return response;
            }

            method = getMethod(object, "getID");
            if(method != null) {
                response = method.invoke(object).toString();
                if(response != null) return response;
            }

            method = getMethod(object, "getId");
            if(method != null) {
                response = method.invoke(object).toString();
                if(response != null) return response;
            }

            return object.toString();
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return object.toString();
        }
    }

    /***
     *
     * @param method
     * @param object
     * @return
     */
    private static String tryMethod(Method method, Object object) {
        try {
            return method.invoke(object).toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /***
     *
     * @param object
     * @param method
     * @return
     */
    private static Method getMethod(Object object, String method) {
        Class clazz = object.getClass();
        try {
            return clazz.getMethod(method);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

}
