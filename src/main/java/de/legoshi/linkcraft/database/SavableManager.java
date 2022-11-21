package de.legoshi.linkcraft.database;

public interface SavableManager<T, U> {

    void initObject(T t);

    void updateObject(T t);

    void deleteObject(U id);

    T requestObjectById(U id);

}
