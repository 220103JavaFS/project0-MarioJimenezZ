package com.revature.dao;

import com.revature.models.Cart;
import com.revature.models.Product;
import com.revature.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {

    private ProductDAO productDAO = new ProductDAOImpl();

    private final Logger log = LoggerFactory.getLogger(CartDAOImpl.class);

    @Override
    public boolean save(Cart c) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("INSERT INTO  carts (user_id, product_list)" +
                    " VALUES (?,?);");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            // Gets product ids from products
            List<Product> product_list = c.getProducts();
            // Gets product ids from products
            Object[] product_ids  = new Object[product_list.size()];
            for (int i = 0; i < product_ids.length; i++){
                product_ids[i] = product_list.get(i).getId();
            }
            int count = 0;
            Array products_id_array = conn.createArrayOf("INTEGER", product_ids);
            statement.setInt(++count, c.getUserId());
            statement.setArray(++count, products_id_array);

            statement.execute();
            products_id_array.free();

            return true;

        }catch (SQLException e){
            log.warn("Error while saving cart : " + c);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Cart c) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("UPDATE carts SET" +
                    " product_list = ?  WHERE id = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());
            List<Product> product_list = c.getProducts();
            // Gets product ids from products
            Object[] product_ids  = new Object[product_list.size()];
            for (int i = 0; i < product_ids.length; i++){
                product_ids[i] = product_list.get(i).getId();
            }
            int count = 0;
            Array products_id_array = conn.createArrayOf("INTEGER", product_ids);
            statement.setArray(++count, products_id_array);
            statement.setInt(++count, c.getId());

            statement.execute();
            products_id_array.free();

            return true;

        }catch (SQLException e){
            log.warn("Error while updating cart : " + c);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM carts WHERE id = " + id + ";");
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql.toString());

            return true;

        } catch (SQLException e) {
            log.warn("Error while deleting cart id : " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<Cart> getAll() {
        // Creates new list of carts
        ArrayList<Cart> list = new ArrayList<>();
        // Tries for connection
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM carts ORDER BY id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                // Creates new cart
                Cart c = new Cart();
                // Sets Id from DB
                c.setId(result.getInt("id"));
                // Sets User Id from DB
                c.setUserId(result.getInt("user_id"));
                // SQL Array result from DB
                Array a = result.getArray("product_list");
                // Converts SQL Array to java object array upcasting to int[]
                Integer[] product_ids = (Integer[]) a.getArray();
                for (int i = 0; i < product_ids.length; i++) {
                    // Finds products from Database by Id
                    Product p = productDAO.get(product_ids[i]);
                    // If the product is found and not null
                    if (p != null) {
                        // Add it to product list
                        c.addProduct(p);
                    }
                }
                // Adds cart to the list of carts
                list.add(c);
            }


        } catch (SQLException e){
            log.warn("Error while creating cart list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Cart get(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM carts WHERE id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                // Creates new cart
                Cart c = new Cart();
                // Sets Id from DB
                c.setId(result.getInt("id"));
                // Sets User Id from DB
                c.setUserId(result.getInt("user_id"));
                // SQL Array result from DB
                Array a = result.getArray("product_list");
                // Converts SQL Array to java object array upcasting to int[]
                Integer[] product_ids = (Integer[]) a.getArray();
                // Loops the products' id array obj
                for (int i = 0; i < product_ids.length; i++) {
                    // Finds products from Database by Id
                    Product p = productDAO.get(product_ids[i]);
                    // If the product is found and not null
                    if (p != null) {
                        // Add it to product list
                        c.addProduct(p);
                    }
                }

                return c;
            }


        } catch (SQLException e){
            log.warn("Error while getting category by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Cart getByUserId(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM carts WHERE user_id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                // Creates new cart
                Cart c = new Cart();
                // Sets Id from DB
                c.setId(result.getInt("id"));
                // Sets User Id from DB
                c.setUserId(result.getInt("user_id"));
                // SQL Array result from DB
                Array a = result.getArray("product_list");
                // Converts SQL Array to java object array upcasting to int[]
                if (a.getArray() != null) {
                    Integer[] product_ids = (Integer[]) a.getArray();
                    // Loops the products' id array obj
                    for (int i = 0; i < product_ids.length; i++) {
                        // Finds products from Database by Id
                        Product p = productDAO.get(product_ids[i]);
                        // If the product is found and not null
                        if (p != null) {
                            // Add it to product list
                            c.addProduct(p);
                        }
                    }
                }

                return c;
            }

        } catch (SQLException e){
            log.warn("Error while getting cart by user id: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
