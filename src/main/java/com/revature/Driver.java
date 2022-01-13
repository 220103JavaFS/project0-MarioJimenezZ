package com.revature;

import com.revature.controllers.*;
import io.javalin.Javalin;

public class Driver {

    private static Javalin app;

    public static void main(String[] args) {
        app = Javalin.create();

        configure(new AccountController(),new ProductController(), new CategoryController());

        app.start(7000);

    }

    public static void configure(Controller... controllers) {
        for (Controller c : controllers) {
            c.addRoutes(app);
        }
    }
}
