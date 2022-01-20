package com.revature.controllers;

import com.revature.models.CustomerOrder;
import com.revature.models.SellerOrder;
import com.revature.models.User;
import com.revature.services.CustomerOrderService;
import com.revature.services.ResponseType;
import com.revature.services.SellerOrderService;
import com.revature.utils.SessionUtil;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class OrderController implements Controller {

    private final Logger log = LoggerFactory.getLogger(CartController.class);
    private CustomerOrderService customerOrderService = new CustomerOrderService();
    private SellerOrderService sellerOrderService = new SellerOrderService();

    private final Handler viewCustomerOrders = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            // If user is admin we show all orders
            if (u.getAccountType() == User.AccountType.ADMINISTRATOR){
                ArrayList<CustomerOrder> list = customerOrderService.getAllOrders();
                ctx.json(list);
                ctx.status(200);
                log.info("Admin is viewing all orders : " + u.getEmail());
            } else {
                // If user is not admin and can see this it must be a customer / seller
                ArrayList<CustomerOrder> list = customerOrderService.getOrdersByUserId(u.getId());
                // User hasn't made any orders yet
                if (list.size() == 0){
                    ctx.html("<h1> NO_ORDERS_YET </h1>");
                } else {
                    ctx.json(list);
                }
                ctx.status(200);
                log.info("User is viewing orders : " + u.getEmail());
            }

        }
    };

    private final Handler viewSellerOrders = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.SELLER);
        if (u != null) {
            ArrayList<SellerOrder> list = sellerOrderService.getOrdersBySellerId(u.getId());
            if (list.size() == 0){
                ctx.html("<h1>NO_ORDERS_YET</h1>");
            } else {
                ctx.json(list);
            }
            ctx.status(200);
            log.info("User is viewing orders : " + u.getEmail());
        }
    };

    private final Handler cancelCustomerOrder = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);

            CustomerOrder order = customerOrderService.getOrderById(id);

            ResponseType result = customerOrderService.cancelOrder(order, u);
            if (result == ResponseType.SUCCESS) {
                ctx.html("<h1>CANCELLED_ORDER_SUCCESSFULLY</h1>");
                ctx.status(200);
                log.info("User cancelled order : " + order);
            } else {
                ctx.html("ERROR_CANCELLING_ORDER: " + result.name());
                ctx.status(400);
                log.info("Error cancelling order: " + result.name() + "\n" + order + "\n" + u);
            }


        }
    };

    private final Handler cancelSellerOrders = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.SELLER);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);

            SellerOrder order = sellerOrderService.getOrderById(id);

            ResponseType result = sellerOrderService.cancelOrder(order, u);
            if (result == ResponseType.SUCCESS) {
                ctx.html("<h1>CANCELLED_ORDER_SUCCESSFULLY</h1>");
                ctx.status(200);
                log.info("User cancelled order : " + order);
            } else {
                ctx.html("ERROR_CANCELLING_ORDER: " + result.name());
                ctx.status(400);
                log.info("Error cancelling order: " + result.name() + "\n" + order + "\n" + u);
            }
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        // Views all customer orders
        app.get("/orders", viewCustomerOrders);
        // cancel customer orders
        app.put("/orders/{id}", cancelCustomerOrder);

        // Views all seller orders
        app.get("/seller-orders", viewSellerOrders);
        // Cancel seller oder
        app.put("/seller-orders/{id}", cancelSellerOrders);
    }
}
