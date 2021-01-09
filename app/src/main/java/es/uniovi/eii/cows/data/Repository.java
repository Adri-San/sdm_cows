package es.uniovi.eii.cows.data;

import android.util.Pair;

import java.util.function.Function;

/**
 * Interface that represents a repository where model objects will be stored.
 * Repositories will manage asynchronous processes so objects/feedback will
 * always be returned on callback function.
 *
 * @param <T> type of the repository (NewsItem, Like or Save)
 */
public interface Repository<T> {

    void add(T t, Function<T, Void> callback);                              //method that stores an object
    void delete(T t, Function<T, Void> callback);                           //method that deletes an object
    void get(Pair<String, Object> condition, Function<T, Void> callback);   //method that returns an object if condition is true
    void getAll(Function<T, Void> callback);                                //method that returns all stored objects

}
