package de.legoshi.linkcraft.database;

public interface SaveableManager<T, U> {

    boolean initObject(T t);

    boolean updateObject(T t);

    boolean deleteObject(U id);

    T requestObjectById(U id);

}
