package model.dao;

import java.util.List;

public interface DataAccessObject<T> {

    List<T> readRows();
    void insertRow(T t);
    void deleteRow(T t);
}
