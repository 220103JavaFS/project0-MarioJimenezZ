package com.revature.dao;

import com.revature.models.SellerOrder;

import java.util.ArrayList;

public interface SellerOrderDAO extends DAO<SellerOrder> {

    ArrayList<SellerOrder> getBySellerId(int id);
    boolean deleteByCustomerOderId(int id);
    boolean cancelByCustomerOderId(int id);

    @Override
    boolean save(SellerOrder o);
    @Override
    boolean update(SellerOrder o);
    @Override
    boolean delete(int id);
    @Override
    ArrayList<SellerOrder> getAll();
    @Override
    SellerOrder get(int id);
}
