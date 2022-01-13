package com.revature.dao;

import com.revature.models.products.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAO extends DAO<Category> {

    private Logger log = LoggerFactory.getLogger(CategoryDAO.class);

    private static CategoryDAO instance;

    public static CategoryDAO getInstance() {
        if (instance == null) {
            instance = new CategoryDAO();
        }
        return instance;
    }

    private CategoryDAO(){
        update();
    }

    public ArrayList<Category> getAllCategories() { return objects; }

    public Category getCategoryById(int id) {
        for (Category c : objects) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    boolean update() {
        objects.clear();

        ResultSet accounts_query = database.executeQuery("SELECT * FROM categories");

        try {
            while (accounts_query.next()) {
                objects.add(new Category(
                        accounts_query.getString("name"),
                        accounts_query.getString("description"),
                        accounts_query.getInt("id")
                ));
            }
            accounts_query.close();
        }  catch(SQLException e){
            log.info("Error while creating category list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean saveObject(Category c) {
        StringBuilder category_query = new StringBuilder();

        category_query.append("INSERT INTO categories (name, description) VALUES(");
        category_query.append("'" + c.getName() + "',");
        category_query.append("'" + c.getDescription() + "');");

        if (database.executeUpdate(category_query.toString())) {
            return update();
        }

        return false;
    }
}
