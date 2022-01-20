package com.revature.services;

import com.revature.dao.*;
import com.revature.models.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CartService {

    private CartDAO cartDAO = new CartDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private ProductDAO productDAO = new ProductDAOImpl();
    private CustomerOrderDAO customerOrderDAO = new CustomerOrderDAOImpl();
    private SellerOrderDAO sellerOrderDAO = new SellerOrderDAOImpl();

    public ArrayList<Cart> getAllCarts() {
        return cartDAO.getAll();
    }

    public boolean deleteCartById(int id) {
        if (id >= 0) {
            return cartDAO.delete(id);
        }
        return false;
    }

    public Cart getCartByUserId(int id) {
        if (id >= 0) {
            return cartDAO.getByUserId(id);
        }
        return null;
    }

    public ResponseType createCart(@NotNull Cart c) {
        // Checks if cart has valid user
        if (c.getUserId() < 0){
            return ResponseType.INVALID_FIELDS;
        }
        // Checks if user exists
        if (userDAO.get(c.getUserId()) == null) {
            return ResponseType.INVALID_USER;
        }
        // Updates cart to database
        if (cartDAO.save(c)) {
            return ResponseType.SUCCESS;
        }
        return ResponseType.UNKNOWN_ERROR;
    }

    public ResponseType updateCart(@NotNull Cart c) {
        // Checks if cart has valid user
        if (c.getUserId() < 0){
            return ResponseType.INVALID_FIELDS;
        }
        // Checks if user exists
        if (userDAO.get(c.getUserId()) == null) {
            return ResponseType.INVALID_USER;
        }
        // Updates cart to database
        if (cartDAO.update(c)) {
            return ResponseType.SUCCESS;
        }
        return ResponseType.UNKNOWN_ERROR;
    }

    public ResponseType checkoutCart(@NotNull Cart c){
        // Checks if Cart user is valid
        User db_user = userDAO.get(c.getUserId());
        if (db_user == null) {
            return ResponseType.INVALID_USER;
        }
        // Checks if Products are valid in database
        // Adds price from database to protect from session editors
        double total_price = 0.0;
        for (Product p : c.getProducts()) {
            Product db_product = productDAO.get(p.getId());
            if (db_product == null) {
                return ResponseType.INVALID_PRODUCT;
            }
            total_price += db_product.getPrice();
        }
        // Checks if user has enough balance
        if (db_user.getBalance() < total_price) {
            return ResponseType.INSUFFICIENT_BALANCE;
        }
        db_user.setBalance(db_user.getBalance() - total_price);
        db_user.setNumOfOrders(db_user.getNumOfOrders() + 1);
        // Records Order
        CustomerOrder customer_order = new CustomerOrder();
        customer_order.setUserId(db_user.getId());
        customer_order.setProductList(c.getProducts());
        customer_order.setOrderTotal(total_price);
        customer_order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        customer_order.setOrderStatus("PENDING");
        customerOrderDAO.save(customer_order);
        // Updates order with id from database
        customer_order = customerOrderDAO.getByTimeStamp(customer_order.getOrderDate(), db_user.getId());

        // Only continues if we can update user balance
        if (userDAO.update(db_user)) {
            // Adds balance of product sale to sellers - 10% fee
            for (Product p : c.getProducts()){
                // Creates Seller Orders
                User seller = userDAO.get(p.getSellerId());
                // creates new seller order
                SellerOrder seller_order = new SellerOrder();
                seller_order.setBuyerId(db_user.getId());
                seller_order.setSellerId(seller.getId());
                seller_order.setCustomerOrderId(customer_order.getOrderId());
                seller_order.addProduct(p);
                seller_order.setOrderTotal(p.getPrice());
                seller_order.setOrderDate(new Timestamp(System.currentTimeMillis()));
                seller_order.setOrderStatus("PENDING");
                sellerOrderDAO.save(seller_order);
                // Calculates new seller balance , subtracts 10% from sale
                double new_balance = (seller.getBalance() + p.getPrice()) - (p.getPrice() *(10.0/ 100.0));
                seller.setBalance(new_balance);
                userDAO.update(seller);
            }
            // Adds the 10% of the total order to the admin balance
            User admin_user = userDAO.get(1);
            admin_user.setBalance(admin_user.getBalance() + (total_price *(10/100.0)));
            userDAO.update(admin_user);

            // Tries to save order & deletes cart
            if (cartDAO.delete(c.getId())){
                return ResponseType.SUCCESS;
            }

            return ResponseType.UNKNOWN_ERROR;
        }

        return ResponseType.UNKNOWN_ERROR;
    }

}
