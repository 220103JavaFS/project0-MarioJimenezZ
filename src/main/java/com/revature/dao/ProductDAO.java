package com.revature.dao;

import com.revature.models.products.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAO extends DAO<Product> {

    private Logger log = LoggerFactory.getLogger(ProductDAO.class);

    public ProductDAO(){ update(); }

    public ArrayList<Product> getAllProducts() { return objects; }

    public Product getProductById(int id) {
        for (Product p : objects) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean update() {
        objects.clear();


        ResultSet products_query = database.executeQuery("SELECT * FROM products");

        try {
            while (products_query.next()) {
                objects.add(new Product(
                        products_query.getString("name"),
                        CategoryDAO.getInstance().getCategoryById(products_query.getInt("category_id")),
                        products_query.getString("description"),
                        products_query.getDouble("price"),
                        products_query.getInt("id"),
                        products_query.getInt("seller_id")
                ));
            }
            products_query.close();
        }  catch(SQLException e){
            log.info("Error while creating product list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean saveObject(Product p) {
        StringBuilder product_query = new StringBuilder();
        
        product_query.append("INSERT INTO products (seller_id, name, category_id, description) VALUES(");
        product_query.append("" + p.getSellerId() + ",");
        product_query.append("'" + p.getName() + "',");
        product_query.append("" + p.getCategory().getId() + ",");
        product_query.append("'" + p.getDescription() + "');");

        if (database.executeUpdate(product_query.toString())) {
            return update();
        }

        return false;
    }
}
