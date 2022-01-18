package com.revature.controllers;

import com.revature.models.User;
import com.revature.models.products.Product;
import com.revature.models.products.ProductDTO;
import com.revature.services.CategoryService;
import com.revature.services.ProductService;
import com.revature.services.ResponseType;
import com.revature.utils.SessionUtil;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ProductController implements Controller{

    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    public ProductController(){}

    private final Handler getAllProducts = ctx -> {
        ArrayList<Product> list = productService.getAllProducts();

        ctx.json(list);
        ctx.status(200);
    };

    private final Handler getProduct = ctx -> {
        String pathId = ctx.pathParam("id");
        int id = Integer.parseInt(pathId);

        Product p = productService.getProductById(id);

        ctx.json(p);
        ctx.status(200);
    };

    private final Handler addProduct = ctx -> {
        // Checks if user is at least a seller or higher
        User u = SessionUtil.UserValidate(ctx, User.AccountType.SELLER);
        if (u != null) {
            // Creates Data Transfer Object
            ProductDTO dto = ctx.bodyAsClass(ProductDTO.class);
            // Creates new product with info
            Product p = new Product(dto.name, categoryService.getCategoryById(dto.categoryId),
                    dto.description, dto.price, u);
            // Attempts to add product to database
            ResponseType result = productService.addProduct(p);
            // If we succeed
            if (result == ResponseType.SUCCESS) {
                ctx.status(201);
                ctx.html("<h1> ADDED_PRODUCT : " + p.getName() + "</h1>");
                log.info("ADDED PRODUCT TO DATABASE: \n " + p);
            } else {
                ctx.html("<h1>ERROR_ADDING_PRODUCT : " + result.name() + "</h1>");
                ctx.status(400);
                log.warn("ERROR_ADDING_PRODUCT : \n" + p + " \n" + result.name());
            }
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/products", getAllProducts);
        app.get("/product/{id}", getProduct);
        app.post("/product/add", addProduct);
    }
}
