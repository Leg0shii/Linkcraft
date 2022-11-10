package de.legoshi.linkcraft.database;

public interface SavableManager<T> {

    void initObject(T t);

    void saveObject(T t);

    void updateObject(T t);

    T requestObject(String where);

}
