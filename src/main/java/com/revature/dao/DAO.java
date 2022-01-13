package com.revature.dao;

import java.util.ArrayList;

public abstract class DAO<T> {

    ArrayList<T> objects = new ArrayList<>();

    DatabaseDAO database = DatabaseDAO.getInstance();

    abstract boolean update();
    public abstract boolean saveObject(T o);
}
