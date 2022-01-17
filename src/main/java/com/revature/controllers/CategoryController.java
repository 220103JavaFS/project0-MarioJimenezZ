package com.revature.controllers;

import com.revature.models.products.Category;
import com.revature.services.CategoryService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class CategoryController implements Controller{

    private CategoryService categoryService;

    public CategoryController() {
        //categoryService = CategoryService.getInstance();
    }

    private final Handler viewAllCategories = ctx -> {
        //ArrayList<Category> list = categoryService.getAllCategories();

        //ctx.json(list);
        //ctx.status(200);
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/categories", viewAllCategories);
    }
}
