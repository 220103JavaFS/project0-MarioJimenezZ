package com.revature.dao;

import com.revature.models.CustomerOrder;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface CustomerOrderDAO extends DAO<CustomerOrder> {

    ArrayList<CustomerOrder> getByUserId(int id);
    CustomerOrder getByTimeStamp(Timestamp t, int id);

    @Override
    boolean save(CustomerOrder o);
    @Override
    boolean update(CustomerOrder o);
    @Override
    boolean delete(int id);
    @Override
    ArrayList<CustomerOrder> getAll();
    @Override
    CustomerOrder get(int id);
}
