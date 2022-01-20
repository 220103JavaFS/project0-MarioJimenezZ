package com.revature.services;

import com.revature.dao.SellerOrderDAO;
import com.revature.dao.SellerOrderDAOImpl;
import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.models.CustomerOrder;
import com.revature.models.Product;
import com.revature.models.SellerOrder;
import com.revature.models.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SellerOrderService {

    private SellerOrderDAO sellerOrderDAO = new SellerOrderDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    public SellerOrder getOrderById(int id){
        if (id >= 0){
            return sellerOrderDAO.get(id);
        }
        return  null;
    }

    public ArrayList<SellerOrder> getAllOrders(){
        return sellerOrderDAO.getAll();
    }

    public ArrayList<SellerOrder> getOrdersBySellerId(int id) {
        if (id >= 0){
            return sellerOrderDAO.getBySellerId(id);
        }
        return new ArrayList<>();
    }

    public ResponseType cancelOrder(@NotNull SellerOrder o, @NotNull User u){
        // checks if order is still PENDING status
        if (!o.getOrderStatus().equals("PENDING")) {
            return ResponseType.ORDER_ALREADY_COMPLETED;
        }
        // checks if seller is valid and has rights to cancel order
        User seller_user = userDAO.get(u.getId());
        if (seller_user == null || o.getSellerId() != seller_user.getId()) {
            return ResponseType.INVALID_USER;
        }
        // Substract balances back from seller
        double order_price = o.getOrderTotal();
        double order_cut = order_price * (10/100.0);
        double total_refund = order_price - order_cut;
        seller_user.setBalance(seller_user.getBalance() - total_refund);
        // Takes 10% cut back from admin
        User admin_user = userDAO.get(1);
        admin_user.setBalance(admin_user.getBalance() - order_cut);
        // Adds balance back to user
        User buyer_user = userDAO.get(o.getBuyerId());
        buyer_user.setBalance(buyer_user.getBalance() + order_price);
        // Updates All Accounts
        userDAO.update(seller_user);
        userDAO.update(admin_user);
        userDAO.update(buyer_user);
        // Updates Seller Order to cancelled
        o.setOrderStatus("CANCELLED");
        if (!sellerOrderDAO.update(o)){
            return ResponseType.UNKNOWN_ERROR;
        }
        return ResponseType.SUCCESS;
    }

}
