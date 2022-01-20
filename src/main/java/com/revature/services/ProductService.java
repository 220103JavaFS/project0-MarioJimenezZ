package com.revature.services;

import com.revature.dao.*;
import com.revature.models.User;
import com.revature.models.Category;
import com.revature.models.Product;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductService {

    private ProductDAO productDAO = new ProductDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private CategoryDAO categoryDAO = new CategoryDAOImpl();

    public ArrayList<Product> getAllProducts() { return productDAO.getAll(); }

    public Product getProductById(int id) { return productDAO.get(id); }

    public ResponseType addProduct(@NotNull Product p) {
        // Checks strings are not empty
        if (p.getName().isEmpty() || p.getDescription().isEmpty() || p.getPrice() < 1) {
            return ResponseType.INVALID_FIELDS;
        }
        // Checks if category is valid
        Category c = categoryDAO.get(p.getCategoryId());
        if (c != null && c.getId() > 0) {
            Category check = categoryDAO.get(c.getId());
            if (check == null || check.getDescription().isEmpty() || check.getId() < 1) {
                return ResponseType.INVALID_CATEGORY;
            }
        } else {
            return ResponseType.INVALID_CATEGORY;
        }
        // Checks if we have a valid seller
        User seller = userDAO.get(p.getSellerId());
        if (seller == null || seller.getId() < 0) {
            return ResponseType.INVALID_SELLER;
        }
        // Checks if we can save product to db
        if (productDAO.save(p)) {
            return ResponseType.SUCCESS;
        }
        // Should be unreachable
        return ResponseType.UNKNOWN_ERROR;
    }
}
