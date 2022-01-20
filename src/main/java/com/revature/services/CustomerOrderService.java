package com.revature.services;

import com.revature.dao.*;
import com.revature.models.CustomerOrder;
import com.revature.models.Product;
import com.revature.models.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomerOrderService {

    private CustomerOrderDAO customerOrderDAO = new CustomerOrderDAOImpl();
    private SellerOrderDAO sellerOrderDAO = new SellerOrderDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    public CustomerOrder getOrderById(int id) { return customerOrderDAO.get(id); }

    public ArrayList<CustomerOrder> getAllOrders(){
        return customerOrderDAO.getAll();
    }

    public ArrayList<CustomerOrder> getOrdersByUserId(int id) {
        if (id >= 0){
            return customerOrderDAO.getByUserId(id);
        }
        return new ArrayList<>();
    }

    public ResponseType cancelOrder(@NotNull CustomerOrder o, @NotNull User u){
        // checks if order is still PENDING status
        if (!o.getOrderStatus().equals("PENDING")) {
            return ResponseType.ORDER_ALREADY_COMPLETED;
        }
        // checks if user is valid and made the order
        User db_user = userDAO.get(u.getId());
        if (db_user == null || o.getUserId() != db_user.getId()) {
            return ResponseType.INVALID_USER;
        }
        for(Product p : o.getProductList()){
            // Substract balances back from seller
            double product_price = p.getPrice();
            double product_cut  = product_price * (10/100.0);
            double total_price = product_price - product_cut;
            User product_seller = userDAO.get(p.getSellerId());
            product_seller.setBalance(product_seller.getBalance() - total_price);
            userDAO.update(product_seller);
        }
        // Adds balance back to buyer
        double order_price = o.getOrderTotal();
        double order_cut = order_price * (10/100.0);
        double total_refund = order_price - order_cut;
        db_user.setBalance(db_user.getBalance() + total_refund);
        // Removes Seller Orders Assosiated
        sellerOrderDAO.cancelByCustomerOderId(o.getOrderId());
        // Updates order to cancelled in database
        o.setOrderStatus("CANCELLED");
        if (!customerOrderDAO.update(o)){
            return ResponseType.UNKNOWN_ERROR;
        }
        return ResponseType.SUCCESS;
    }

}
