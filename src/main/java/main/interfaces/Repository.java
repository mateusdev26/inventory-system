package main.interfaces;

import java.util.List;

public interface Repository<T> {
    int save(T t);
    int delete(Number id );
    int delete(Number id , int limit);
    int delete(Number... ids);
    int delete(int limit ,Number... ids);
    List<T> findAll();
    T findById(Number id);
    int update(Number id, T t);
    int update(Number id, T t,int limit);
}
