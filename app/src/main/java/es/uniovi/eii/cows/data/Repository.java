package es.uniovi.eii.cows.data;

import java.util.function.Function;

public interface Repository<T, P> {

    void add(T t);
    void getAll(Function<P, Void> callback);
    void get(String id, Function<P, Void> callback);
}
