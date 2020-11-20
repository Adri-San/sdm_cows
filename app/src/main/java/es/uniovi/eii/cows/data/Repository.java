package es.uniovi.eii.cows.data;

import java.util.function.Function;

public interface Repository<T> {

    void add(T t);
    void getAll(Function<T, Void> callback);
}
