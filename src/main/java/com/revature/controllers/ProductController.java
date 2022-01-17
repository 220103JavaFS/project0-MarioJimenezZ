package com.revature.controllers;

import com.revature.models.products.Product;
import com.revature.services.ProductService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ProductController implements Controller{

    private ProductService productService = new ProductService();
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
        Product p = ctx.bodyAsClass(Product.class);

        switch (productService.addProduct(p)){

            case SUCCESS:
                ctx.status(201);
                ctx.html("<h1> Added Product to Database! : " + p.getName() + "</h1>");
                log.info("Successfully add product to database " + p);
                break;
            case INVALID_FIELDS:
                ctx.html("<h1> Error Adding Product: Some fields may be empty (Price can't be 0) </h1>");
                ctx.status(400);
                log.warn("User tried adding product with invalid fields: " + p);
                break;
            case INVALID_CATEGORY:
                ctx.html("<h1> Error Adding Product: Invalid Category </h1>");
                ctx.status(400);
                log.warn("User tried adding product with invalid category: " + p.getCategory());
                break;
            default:
            case UNKNOWN_ERROR:
                ctx.html("<h1> Error Adding Product: Unknown Error </h1>");
                ctx.status(400);
                break;
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/products", getAllProducts);
        app.get("/product/{id}", getProduct);

        app.post("/product/add", addProduct);
    }
}
