package com.revature.controllers;

import com.revature.models.SellerApplication;
import com.revature.models.User;
import com.revature.services.ResponseType;
import com.revature.services.SellerApplicationService;
import com.revature.utils.SessionUtil;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SellerApplicationController implements Controller {

    private final Logger log = LoggerFactory.getLogger(SellerApplicationController.class);
    private SellerApplicationService sellerApplicationService = new SellerApplicationService();

    private final Handler viewByStatus = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.ADMINISTRATOR);
        if (u != null) {
            String status = ctx.pathParam("status");
            ArrayList<SellerApplication> list = sellerApplicationService.getAppsByStatus(status);

            ctx.json(list);
            ctx.status(200);
        }
    };

    private final Handler viewById = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.ADMINISTRATOR);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);

            SellerApplication app = sellerApplicationService.getAppById(id);

            if (app != null) {
                ctx.json(app);
                ctx.status(200);
            } else {
                ctx.html("<h1>INVALID_SELLER_APP_ID : " + id + "</h1>");
                log.warn("Error finding seller app with id : " + id);
                ctx.status(400);
            }
        }
    };

    private final Handler updateById = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.ADMINISTRATOR);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);
            // App to update
            SellerApplication updating_app = sellerApplicationService.getAppById(id);
            if (updating_app == null){
                ctx.html("<h1> ERROR_FINDING_APP </h1>");
                ctx.status(400);
                log.warn("Error finding application with id : " + id);
                return;
            }
            // updated information
            SellerApplication info = ctx.bodyAsClass(SellerApplication.class);
            // Updates app info with the updated information
            updating_app.setStatus(info.getStatus());
            // Tries to update the account
            ResponseType result = sellerApplicationService.updateApplication(updating_app);

            if (result == ResponseType.SUCCESS) {
                ctx.json(updating_app);
                ctx.status(200);
                log.info("updated seller application : " + updating_app);
            } else {
                ctx.html("<h1>ERROR_UPDATING_SELLER_APPLICATION: " + result.name() +"</h1>");
                ctx.status(400);
                log.warn("ERROR_UPDATING_SELLER_APPLICATION: \n " + u + "\n " + result.name());
            }

        }
    };

    private final Handler getAllApps = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.ADMINISTRATOR);
        if (u != null) {
            ArrayList<SellerApplication> list = sellerApplicationService.getAllApplications();

            ctx.json(list);
            ctx.status(200);
        }
    };

    private final Handler createApplication = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            // Checks if user already has existing application
            SellerApplication check = sellerApplicationService.getAppByUserId(u.getId());
            if (check != null) {
                ctx.json(check);
                ctx.status(200);
                return;
            }
            // If not we try to start a new one
            SellerApplication app = new SellerApplication();

            app.setUser(u);
            app.setStatus("PENDING");

            ResponseType result = sellerApplicationService.createApplication(app);

            if (result == ResponseType.SUCCESS){
                ctx.html("SELLER_APPLICATION_SUBMITTED");
                ctx.status(200);
            } else {
                ctx.html("ERROR_SUBMITTING_SELLER_APPLICATION: " + result.name());
                ctx.status(400);
            }
        }
    };

    private final Handler removeById = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.ADMINISTRATOR);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);
            if (sellerApplicationService.deleteApplicationById(id)){
                ctx.html("<h1>APPLICATION_DELETED : " + id + "</h1>");
                log.info("Successfully deleted application by ID : " + id);
                ctx.status(200);
            } else {
                ctx.html("<h1>ERROR_DELETING_APPLICATION : " + id + "</h1>");
                log.info("Error deleting application by ID : " + id);
                ctx.status(400);
            }

        }
    };

    @Override
    public void addRoutes(Javalin app) {
        // Submits seller application
        app.post("/become-seller", createApplication);

        // Views all seller applications
        app.get("/seller-apps", getAllApps);
        // Views all apps by status
        app.get("/seller-apps/status/{status}", viewByStatus);
        // views app by app id
        app.get("/seller-apps/{id}", viewById);
        // Updates app by id
        app.put("/seller-apps/{id}", updateById);
        // Deletes app by id
        app.delete("/seller-apps/{id}", removeById);
    }
}
