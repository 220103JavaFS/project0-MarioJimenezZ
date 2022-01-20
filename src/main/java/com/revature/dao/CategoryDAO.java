package com.revature.dao;

import com.revature.models.Category;

import java.util.ArrayList;

public interface CategoryDAO extends DAO<Category> {

    @Override
    boolean save(Category c);
    @Override
    boolean delete(int id);
    @Override
    ArrayList<Category> getAll();
    @Override
    Category get(int id);
}
