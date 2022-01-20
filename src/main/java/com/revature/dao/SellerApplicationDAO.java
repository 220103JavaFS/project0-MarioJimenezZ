package com.revature.dao;

import com.revature.models.SellerApplication;

import java.util.ArrayList;

public interface SellerApplicationDAO extends DAO<SellerApplication> {


    SellerApplication getByUserId(int id);
    ArrayList<SellerApplication> getAppsByStatus(String status);

    @Override
    boolean save(SellerApplication app);
    @Override
    boolean update(SellerApplication app);
    @Override
    boolean delete(int id);
    @Override
    ArrayList<SellerApplication> getAll();
    @Override
    SellerApplication get(int id);
}
