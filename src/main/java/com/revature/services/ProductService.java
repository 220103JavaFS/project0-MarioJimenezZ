package com.revature.services;

import com.revature.dao.ProductDAO;
import com.revature.dao.ProductDAOImpl;
import com.revature.models.products.Product;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductService {

    private ProductDAO productDAO = new ProductDAOImpl();

    public ArrayList<Product> getAllProducts() { return productDAO.getAll(); }

    public Product getProductById(int id) { return productDAO.get(id); }

    public ResponseType addProduct(@NotNull Product p) {
        // Checks strings are not empty
        if (p.getName().isEmpty() || p.getDescription().isEmpty() || p.getPrice() < 1) {
            return ResponseType.INVALID_FIELDS;
        }
        // Checks if category is valid
        /*if (p.getCategory() != null && p.getCategory().getId() > 0) {
            Category c = CategoryService.getInstance().getCategoryById(p.getCategory().getId());
            if (c == null || c.getDescription().isEmpty() || c.getId() < 1) {
                return ResponseType.INVALID_CATEGORY;
            }
        } else {
            return ResponseType.INVALID_CATEGORY;
        }*/
        // Checks if we can save product to db
        if (productDAO.save(p)) {
            return ResponseType.SUCCESS;
        }
        // Should be unreachable
        return ResponseType.UNKNOWN_ERROR;
    }
}
