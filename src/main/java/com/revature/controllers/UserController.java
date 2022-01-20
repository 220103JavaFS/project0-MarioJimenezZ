package com.revature.controllers;

import com.revature.models.User;
import com.revature.models.User.AccountType;
import com.revature.models.dto.UserDTO;
import com.revature.services.ResponseType;
import com.revature.services.UserService;
import com.revature.utils.SessionUtil;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class UserController implements Controller{

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserService userService = new UserService();

    public UserController() {}

    private final Handler getAccount = ctx -> {
        User u = SessionUtil.UserValidate(ctx, AccountType.CUSTOMER);
        if (u != null) {
            User user = userService.getAccountByEmail(u.getEmail());
            ctx.json(user);
            ctx.status(200);
            ctx.req.getSession().setAttribute("user", user);
            log.info("User is viewing account " + u);
        }
    };

    private final Handler getAllAccounts = ctx -> {
        User u = SessionUtil.UserValidate(ctx, AccountType.ADMINISTRATOR);
        if (u != null) {
            ArrayList<User> list = userService.getAllAccounts();

            ctx.json(list);
            ctx.status(200);
        }
    };

    private final Handler addAccount = ctx -> {
        if (ctx.req.getSession(false) != null) {
            ctx.html("ALREADY_LOGGED_IN");
            ctx.status(400);
            log.info("User tried registering a new account while being logged in");
        } else {
            // Creates a data transfer object
            UserDTO dto = ctx.bodyAsClass(UserDTO.class);
            // Creates a new user with the information from the data transfer object
            User u = new User(dto.firstName, dto.lastName, dto.email, dto.password);
            // Tries to save user
            ResponseType result = userService.saveAccount(u);
            if (result == ResponseType.SUCCESS) {
                // Grabs new user information from database (this includes new user id)
                User db_user = userService.getAccountByEmail(u.getEmail());
                // Shows information of new user
                ctx.json(db_user);
                // Creates Session with user information
                ctx.req.getSession().setAttribute("user", db_user);
                // Sends Created OK status
                ctx.status(201);
                // Logs info into log
                log.info("Customer successfully registered with email: " + u.getEmail());
            } else {
                ctx.html("<h1>ERROR_CREATING_ACCOUNT : " + result.name() + "</h1>");
                ctx.status(400);
                log.warn("ERROR_CREATING_ACCOUNT:\n " + u + " \n " + result.name());
            }
        }
    };

    private final Handler updateAccount = ctx -> {
        // Grabs user from session
        User u = SessionUtil.UserValidate(ctx, AccountType.CUSTOMER);
        // Checks if session is set
        if (u != null) {
            // Data transfer object
            UserDTO info = ctx.bodyAsClass(UserDTO.class);
            // Checks if there's already an account with the email that we're changing to
            if (!info.email.equals(u.getEmail())){
                User check = userService.getAccountByEmail(info.email);
                if (check != null){
                    ctx.html("<h1> EMAIL_EXISTS </h1>");
                    ctx.status(400);
                    log.warn("User tried updating with duplicate email : " + u.getEmail());
                    return;
                }
            }
            // Updates account info with the information from the data transfer object
            u.setFirstName(info.firstName);
            u.setLastName(info.lastName);
            u.setEmail(info.email);
            u.setPassword(info.password);
            // Administrators can edit their balance
            if (u.getAccountType() == AccountType.ADMINISTRATOR) {
                u.setBalance(info.balance);
            }
            // Tries to update the account
            ResponseType result = userService.updateAccount(u);

            if (result == ResponseType.SUCCESS) {
                User user = userService.getAccountByEmail(u.getEmail());
                ctx.req.getSession().setAttribute("user", user);
                ctx.json(user);
                ctx.status(200);
                log.info("Customer successfully updated account with email: " + user.getEmail());
            } else {
                ctx.html("<h1>ERROR_UPDATING_ACCOUNT: " + result.name() +"</h1>");
                ctx.status(400);
                log.warn("ERROR_UPDATING_ACCOUNT: \n " + u + "\n " + result.name());
            }
        }
    };

    private final Handler updateAccountById = ctx -> {
        User u = SessionUtil.UserValidate(ctx, AccountType.ADMINISTRATOR);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);
            // user to update
            User updating_user = userService.getAccountById(id);
            if (updating_user == null){
                ctx.html("<h1> ERROR_FINDING_USER </h1>");
                ctx.status(400);
                log.warn("User tried updating with invalid id : " + id);
                return;
            }
            // Data transfer object
            UserDTO info = ctx.bodyAsClass(UserDTO.class);
            // Checks if there's already an account with the email that we're changing to
            if (!info.email.equals(updating_user.getEmail())){
                User check = userService.getAccountByEmail(info.email);
                if (check != null){
                    ctx.html("<h1> EMAIL_EXISTS </h1>");
                    ctx.status(400);
                    log.warn("User tried updating with duplicate email : " + u.getEmail());
                    return;
                }
            }
            // Updates account info with the information from the data transfer object
            updating_user.setFirstName(info.firstName);
            updating_user.setLastName(info.lastName);
            updating_user.setEmail(info.email);
            updating_user.setPassword(info.password);
            updating_user.setBalance(info.balance);
            // Tries to update the account
            ResponseType result = userService.updateAccount(updating_user);

            if (result == ResponseType.SUCCESS) {
                User user = userService.getAccountByEmail(updating_user.getEmail());
                ctx.json(user);
                ctx.status(200);
                log.info("admin updated account with email: " + user.getEmail());
            } else {
                ctx.html("<h1>ERROR_UPDATING_ACCOUNT: " + result.name() +"</h1>");
                ctx.status(400);
                log.warn("ERROR_UPDATING_ACCOUNT: \n " + u + "\n " + result.name());
            }

        }
    };

    private final Handler getAccountById = ctx -> {
        // Grabs user from session
        User u = SessionUtil.UserValidate(ctx, AccountType.ADMINISTRATOR);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);

            User user = userService.getAccountById(id);

            if (user != null) {
                ctx.json(user);
                ctx.status(200);
            } else {
                ctx.html("<h1> Cannot find the user with ID : " + id + "</h1>");
                log.warn("User tried retrieving non-existing account with id : " + id);
                ctx.status(400);
            }
        }
    };

    private final Handler deleteById = ctx -> {
        User u = SessionUtil.UserValidate(ctx, AccountType.ADMINISTRATOR);
        if (u != null) {

            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);
            ResponseType result = userService.deleteAccount(id);

            if (result == ResponseType.SUCCESS) {
                ctx.html("<h1>USER_DELETED : " + id + "</h1>");
                log.info("Successfully deleted account by ID : " + id);
                ctx.status(200);
            } else {
                ctx.html("<h1>ERROR_DELETING_USER : " + result.name() + "</h1>");
                ctx.status(400);
                log.error("Unknown error while trying to delete account by ID: " + id + " | "+ result.name());
            }
        }
    };

    private final Handler deleteAccount = ctx ->  {
        User u = SessionUtil.UserValidate(ctx, AccountType.CUSTOMER);
        if (u != null) {

            int id = u.getId();
            ResponseType result = userService.deleteAccount(id);

            if (result == ResponseType.SUCCESS) {
                ctx.html("<h1>USER_DELETED : " + id + "</h1>");
                log.info("Successfully deleted account by ID : " + id);
                ctx.req.getSession().invalidate();
                ctx.status(200);
            } else {
                ctx.html("<h1>ERROR_DELETING_USER : " + result.name() + "</h1>");
                ctx.status(400);
                log.error("Unknown error while trying to delete account by ID: " + id + " | "+ result.name());
            }
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        // ============ USER ROUTES ========== \\
        // Views User Account
        app.get("/account", getAccount);
        // Updates user Account
        app.put("/account", updateAccount);
        // Deletes user Account
        app.get("/account/delete", deleteAccount);
        // Registers user Account
        app.post("/register", addAccount);

        // ============ ADMIN ROUTES ========== \\
        // Views user by ID
        app.get("/account/view/{id}", getAccountById);
        // Updates user by ID
        app.put("account/view/{id}", updateAccountById);
        // Deletes user by ID
        app.delete("/account/delete/{id}", deleteById);
        // Views all user accounts
        app.get("/accounts", getAllAccounts);
    }
}