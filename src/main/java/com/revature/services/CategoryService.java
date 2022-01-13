package com.revature.services;

import com.revature.dao.CategoryDAO;
import com.revature.models.products.Category;

import java.util.ArrayList;

public class CategoryService {

    CategoryDAO categoryDAO;

    public CategoryService() {
        categoryDAO = CategoryDAO.getInstance();
    }

    public ArrayList<Category> getAllCategories() { return categoryDAO.getAllCategories();}

}
