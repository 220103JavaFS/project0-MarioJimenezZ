package com.revature.services;

import com.revature.dao.SellerApplicationDAO;
import com.revature.dao.SellerApplicationDAOImpl;
import com.revature.models.SellerApplication;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SellerApplicationService {

    private SellerApplicationDAO sellerApplicationDAO = new SellerApplicationDAOImpl();

    public ArrayList<SellerApplication> getAllApplications() { return sellerApplicationDAO.getAll(); }

    public ArrayList<SellerApplication> getAppsByStatus(@NotNull String status){
        if (!status.equals("pending") && !status.equals("approved")) {
            return new ArrayList<>();
        }
        return sellerApplicationDAO.getAppsByStatus(status);
    }

    public SellerApplication getAppById(int id) { return sellerApplicationDAO.get(id); }

    public SellerApplication getAppByUserId(int user_id) { return sellerApplicationDAO.getByUserId(user_id); }

    public boolean deleteApplicationById(int id)  {
        if (id >= 0) {
            return sellerApplicationDAO.delete(id);
        }
        return false;
    }

    public ResponseType updateApplication(@NotNull SellerApplication app){
        if (!app.getStatus().equals("PENDING") && !app.getStatus().equals("APPROVED")){
            return ResponseType.INVALID_FIELDS;
        }
        if (sellerApplicationDAO.update(app)){
            return ResponseType.SUCCESS;
        }
        return ResponseType.UNKNOWN_ERROR;
    }

    public ResponseType createApplication(@NotNull SellerApplication app) {
        // Checks if user has at least 1 previous order
        if (app.getUser().getNumOfOrders() < 1){
            return ResponseType.INSUFFICIENT_ORDERS;
        }
        // Tries to save application
        if (sellerApplicationDAO.save(app)){
            return ResponseType.SUCCESS;
        }
        // Unknown Error
        return ResponseType.UNKNOWN_ERROR;
    }

}
