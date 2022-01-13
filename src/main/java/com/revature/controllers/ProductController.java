package com.revature.controllers;

import com.revature.models.products.Product;
import com.revature.services.ProductService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import java.util.ArrayList;

public class ProductController implements Controller{

    private ProductService productService = new ProductService();

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
        if (productService.addProduct(p)){
            ctx.status(201);
        } else {
            ctx.html("<h1> Adding Product </h1>");
            ctx.status(400);
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/products", getAllProducts);
        app.get("/product/{id}", getProduct);

        app.post("/product/add", addProduct);
    }
}
