package com.revature.dao;
import com.revature.models.Cart;
import java.util.ArrayList;

public interface CartDAO extends DAO<Cart> {

    Cart getByUserId(int id);

    @Override
    boolean save(Cart c);
    @Override
    boolean update(Cart c);
    @Override
    boolean delete(int id);
    @Override
    ArrayList<Cart> getAll();
    @Override
    Cart get(int id);
}
