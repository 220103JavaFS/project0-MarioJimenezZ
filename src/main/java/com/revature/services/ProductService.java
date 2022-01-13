package com.revature.services;

import com.revature.dao.ProductDAO;
import com.revature.models.products.Product;

import java.util.ArrayList;

public class ProductService {

    private ProductDAO productDAO = new ProductDAO();

    public ArrayList<Product> getAllProducts() { return productDAO.getAllProducts(); }

    public Product getProductById(int id) { return productDAO.getProductById(id); }

    public boolean addProduct(Product p) { return productDAO.saveObject(p); }
}
