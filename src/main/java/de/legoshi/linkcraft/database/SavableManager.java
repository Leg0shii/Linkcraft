package de.legoshi.linkcraft.database;

public interface SavableManager<T> {

    void initObject(T t);

    void updateObject(T t);

    void deleteObject(String id);

    T requestObjectById(String id);

}
