package net.xilla.boot.storage.manager;

public interface ManagerHandler<T> {

    default void objectAdded(T obj) {}

    default void objectRemoved(T obj) {}

}
