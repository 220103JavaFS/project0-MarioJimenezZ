package com.revature.dao;

import java.util.ArrayList;

public interface DAO<T> {
    
    boolean save(T o);

    boolean update(T o);

    boolean delete(int id);

    ArrayList<T> getAll();

    T get(int id);
}
