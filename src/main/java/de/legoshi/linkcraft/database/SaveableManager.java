package de.legoshi.linkcraft.database;

public interface SaveableManager<T, U> {

    int initObject(T t);

    boolean updateObject(T t);

    boolean deleteObject(U id);

    T requestObjectById(U id);

}
