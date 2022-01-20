package com.revature.controllers;

import com.revature.models.Cart;
import com.revature.models.User;
import com.revature.models.Product;
import com.revature.services.CartService;
import com.revature.services.ResponseType;
import com.revature.services.UserService;
import com.revature.utils.SessionUtil;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CartController implements Controller{

    private final Logger log = LoggerFactory.getLogger(CartController.class);

    private CartService cartService = new CartService();
    private UserService userService = new UserService();

    private final Handler viewCart = ctx -> {
        // Checks if user is logged in
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            // Checks if user has cart
            Cart user_cart = cartService.getCartByUserId(u.getId());
            if (user_cart == null) {
                user_cart = new Cart();
                user_cart.setUserId(u.getId());
                cartService.createCart(user_cart);
                user_cart = cartService.getCartByUserId(u.getId());

            }
            if (user_cart.getProducts().size() == 0) {
                ctx.html("<h1> EMPTY_CART </h1>");
            } else {
                ctx.json(user_cart);
            }
            ctx.status(200);
            log.info("Viewing Cart: \n " + user_cart);
        }
    };

    private final Handler emptyCart = ctx -> {
        // Checks if user is logged in
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            Cart user_cart = cartService.getCartByUserId(u.getId());
            if (user_cart != null) {
                cartService.deleteCartById(user_cart.getId());
            }
            ctx.html("<h1> EMPTIED_CART </h1>");
            ctx.status(200);
            log.info("User Emptied Cart: \n " + u.getEmail());
        }
    };

    private final Handler removeProductById = ctx -> {
        // Checks if user is logged in
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            Cart user_cart = cartService.getCartByUserId(u.getId());
            if (user_cart == null) {
                ctx.html("<h1> EMPTY_CART </h1>");
                ctx.status(400);
                return;
            } else {
                String pathId = ctx.pathParam("id");
                int id = Integer.parseInt(pathId);

                for (Product p : user_cart.getProducts()) {
                    if (p.getId() == id){
                        user_cart.removeProduct(p);
                    }
                }
                cartService.updateCart(user_cart);
                ctx.status(200);
                ctx.html("<h1> REMOVED_PRODUCT_ID : " + id + " </h1>");
                log.info("Removed Product Id " + id + " from cart " + user_cart);
            }
        }
    };

    private final Handler checkoutCart = ctx -> {
        // Checks if user is logged in
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            Cart user_cart = cartService.getCartByUserId(u.getId());
            if (user_cart != null) {
                ResponseType result = cartService.checkoutCart(user_cart);
                if (result == ResponseType.SUCCESS) {
                    ctx.html("ORDER_PLACED_SUCCESSFULLY");
                    // Updates user info
                    u = userService.getAccountById(u.getId());
                    ctx.req.getSession().setAttribute("user", u);
                    ctx.status(200);
                    log.info("User placed new order successfully! :\n " + u);
                } else {
                    ctx.html("ERROR_PLACING_ORDER : " + result.name());
                    ctx.status(400);
                    log.info("Error while trying to checkout : " +
                            result.name() + "\n " + user_cart);
                }
            } else {
                ctx.html("<h1>INVALID_CART</h1>");
                ctx.status(400);
                log.info("User tried checking out without a cart:\n " + u);
            }
        }
    };

    public final Handler viewAllCarts = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.ADMINISTRATOR);
        if (u != null) {
            ArrayList<Cart> cart_list = cartService.getAllCarts();

            ctx.json(cart_list);
            ctx.status(200);
            log.info("Admin is viewing cart list: " + u.getEmail());
        }
    };

    private final Handler deleteById = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.ADMINISTRATOR);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);
            if (cartService.deleteCartById(id)){
                ctx.html("<h1>CART_DELETED : " + id + "</h1>");
                log.info("Successfully deleted cart by ID : " + id);
                ctx.status(200);
            } else {
                ctx.html("<h1>ERROR_DELETING_CART : " + id + "</h1>");
                log.info("Error deleting cart by ID : " + id);
                ctx.status(400);
            }

        }
    };

    @Override
    public void addRoutes(Javalin app) {
        // Admin can view all carts
        app.get("/carts", viewAllCarts);
        // Delete Cart by ID
        app.delete("/cart/remove/{id}", deleteById);

        // View User Cart
        app.get("/cart", viewCart);
        // Empties User Cart
        app.delete("/cart/empty", emptyCart);
        // Deletes product id from cart
        app.put("/cart/remove/{id}", removeProductById);
        // Checks out Products
        app.post("/cart/checkout", checkoutCart);
    }
}
