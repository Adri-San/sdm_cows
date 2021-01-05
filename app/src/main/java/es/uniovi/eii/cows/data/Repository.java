package es.uniovi.eii.cows.data;

import android.util.Pair;

import java.util.function.Function;

public interface Repository<T, P> {

    void add(T t, Function<T, Void> callback);
    void delete(T p, Function<T, Void> callback);
    void get(Pair<String, Object> condition, Function<T, Void> callback);
    void getAll(Function<T, Void> callback);

}
