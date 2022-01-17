package com.revature.dao;

import com.revature.models.Account;
import java.util.ArrayList;


public interface AccountDAO extends DAO<Account> {

    Account getByEmail(String email);

    @Override
    ArrayList<Account> getAll();
    @Override
    Account get(int id);
    @Override
    boolean save(Account a);
    @Override
    boolean update(Account o);
    @Override
    boolean delete(int id);

}
