package com.revature.dao;

import com.revature.models.SellerApplication;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class SellerApplicationDAOImpl implements SellerApplicationDAO {

    private final Logger log = LoggerFactory.getLogger(SellerApplicationDAOImpl.class);

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public boolean save(SellerApplication app) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("INSERT INTO  seller_apps (user_id, status)" +
                    " VALUES (?,?);");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setInt(++count, app.getUser().getId());
            statement.setString(++count, "PENDING");

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while saving seller app : " + app);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(SellerApplication app) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String s = app.getStatus().toUpperCase();
            StringBuffer sql = new StringBuffer("UPDATE seller_apps SET" +
                    " status = ? WHERE id = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setString(++count, s);
            statement.setInt(++count, app.getId());

            statement.execute();

            // Updates User to Seller
            User seller = app.getUser();
            if(s.equals("APPROVED")){
                seller.setAccountType(User.AccountType.SELLER);
                userDAO.update(seller);
            } else {
                // Revokes seller privileges if we rollback app
                seller.setAccountType(User.AccountType.CUSTOMER);
                userDAO.update(seller);
            }
            return true;

        }catch (SQLException e){
            log.warn("Error while updating seller app : " + app);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM seller_apps WHERE id = " + id + ";");
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql.toString());

            return true;

        } catch (SQLException e) {
            log.warn("Error while deleting seller application id : " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<SellerApplication> getAll() {
        ArrayList<SellerApplication> list = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM seller_apps ORDER BY id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                SellerApplication app = new SellerApplication();
                app.setId(result.getInt("id"));
                app.setUser(userDAO.get(result.getInt("user_id")));
                app.setStatus(result.getString("status"));

                list.add(app);
            }

        } catch (SQLException e){
            log.warn("Error while creating seller application list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public ArrayList<SellerApplication> getAppsByStatus(String status) {
        ArrayList<SellerApplication> list = new ArrayList<>();
        String s = status.toUpperCase();
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM seller_apps WHERE status = '"
                    + s + "' ORDER BY id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                SellerApplication app = new SellerApplication();
                app.setId(result.getInt("id"));
                app.setUser(userDAO.get(result.getInt("user_id")));
                app.setStatus(result.getString("status"));

                list.add(app);
            }

        } catch (SQLException e){
            log.warn("Error while creating seller application list by status : " + s);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public SellerApplication get(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM seller_apps WHERE id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                SellerApplication app = new SellerApplication();

                app.setId(result.getInt("id"));
                app.setUser(userDAO.get(result.getInt("user_id")));
                app.setStatus(result.getString("status"));

                return app;
            }

        } catch (SQLException e){
            log.warn("Error while getting seller app by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public SellerApplication getByUserId(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM seller_apps WHERE user_id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                SellerApplication app = new SellerApplication();

                app.setId(result.getInt("id"));
                app.setUser(userDAO.get(result.getInt("user_id")));
                app.setStatus(result.getString("status"));

                return app;
            }

        } catch (SQLException e){
            log.warn("Error while getting seller app by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
