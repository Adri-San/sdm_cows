package es.uniovi.eii.cows.data;

import android.util.Pair;

import java.util.function.Function;

public interface Repository<T, P> {

    void add(T t);
    void delete(T p);
    void get(Pair<String, Object> condition, Function<P, Void> callback);
    void getAll(Function<P, Void> callback);

}
