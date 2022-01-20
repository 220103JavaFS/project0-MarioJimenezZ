package com.revature.dao;

import com.revature.models.CustomerOrder;
import com.revature.models.Product;
import com.revature.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerOrderDAOImpl implements CustomerOrderDAO {

    private ProductDAO productDAO = new ProductDAOImpl();

    private final Logger log = LoggerFactory.getLogger(CustomerOrderDAOImpl.class);

    @Override
    public boolean save(CustomerOrder o) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("INSERT INTO  customer_orders (user_id, product_list, order_date," +
                    " order_total, order_status) VALUES (?,?,?,?,?);");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            // Gets product ids from products
            List<Product> product_list = o.getProductList();
            // Gets product ids from products
            Object[] product_ids  = new Object[product_list.size()];
            for (int i = 0; i < product_ids.length; i++){
                product_ids[i] = product_list.get(i).getId();
            }
            int count = 0;
            Array products_id_array = conn.createArrayOf("INTEGER", product_ids);
            statement.setInt(++count, o.getUserId());
            statement.setArray(++count, products_id_array);
            statement.setTimestamp(++count, o.getOrderDate());
            statement.setDouble(++count, o.getOrderTotal());
            statement.setString(++count, o.getOrderStatus());

            statement.execute();
            products_id_array.free();

            return true;

        }catch (SQLException e){
            log.warn("Error while saving order : " + o);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(CustomerOrder o) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("UPDATE customer_orders SET" +
                    " order_status = ? WHERE order_id = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setString(++count, o.getOrderStatus());
            statement.setInt(++count, o.getOrderId());

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while updating customer order : " + o);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM customer_orders WHERE order_id = " + id + ";");
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql.toString());

            return true;

        } catch (SQLException e) {
            log.warn("Error while deleting order id : " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<CustomerOrder> getAll() {
        // Creates new list of carts
        ArrayList<CustomerOrder> list = new ArrayList<>();
        // Tries for connection
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM customer_orders ORDER BY order_id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                // Creates new cart
                CustomerOrder o = new CustomerOrder();
                // Sets Id from DB
                o.setOrderId(result.getInt("order_id"));
                // Sets User Id from DB
                o.setUserId(result.getInt("user_id"));
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
                        o.addProduct(p);
                    }
                }
                // Sets the order date
                o.setOrderDate(result.getTimestamp("order_date"));
                // Sets order total
                o.setOrderTotal(result.getDouble("order_total"));
                // Sets order status
                o.setOrderStatus(result.getString("order_status"));

                // Adds cart to the list of carts
                list.add(o);
            }


        } catch (SQLException e){
            log.warn("Error while creating cart list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public CustomerOrder get(int id) {

        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM customer_orders WHERE order_id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                // Creates new cart
                CustomerOrder o = new CustomerOrder();
                // Sets Id from DB
                o.setOrderId(result.getInt("order_id"));
                // Sets User Id from DB
                o.setUserId(result.getInt("user_id"));
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
                        o.addProduct(p);
                    }
                }
                // Sets the order date
                o.setOrderDate(result.getTimestamp("order_date"));
                // Sets order total
                o.setOrderTotal(result.getDouble("order_total"));
                // Sets order status
                o.setOrderStatus(result.getString("order_status"));

                return o;
            }


        } catch (SQLException e){
            log.warn("Error while getting order by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public CustomerOrder getByTimeStamp(Timestamp t, int id){
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM customer_orders WHERE user_id = ? " +
                    "AND order_date = ?");

            PreparedStatement statement = conn.prepareStatement(sql.toString());
            int count = 0;
            statement.setInt(++count, id);
            statement.setTimestamp(++count, t);

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                // Creates new cart
                CustomerOrder o = new CustomerOrder();
                // Sets Id from DB
                o.setOrderId(result.getInt("order_id"));
                // Sets User Id from DB
                o.setUserId(result.getInt("user_id"));
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
                        o.addProduct(p);
                    }
                }
                // Sets the order date
                o.setOrderDate(result.getTimestamp("order_date"));
                // Sets order total
                o.setOrderTotal(result.getDouble("order_total"));
                // Sets order status
                o.setOrderStatus(result.getString("order_status"));

                return o;
            }


        } catch (SQLException e){
            log.warn("Error while getting order by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<CustomerOrder> getByUserId(int id){
        // Creates new list of carts
        ArrayList<CustomerOrder> list = new ArrayList<>();
        // Tries for connection
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM customer_orders WHERE user_id = " + id + " ORDER BY order_date ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                // Creates new cart
                CustomerOrder o = new CustomerOrder();
                // Sets Id from DB
                o.setOrderId(result.getInt("order_id"));
                // Sets User Id from DB
                o.setUserId(result.getInt("user_id"));
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
                        o.addProduct(p);
                    }
                }
                // Sets the order date
                o.setOrderDate(result.getTimestamp("order_date"));
                // Sets order total
                o.setOrderTotal(result.getDouble("order_total"));
                // Sets order status
                o.setOrderStatus(result.getString("order_status"));

                // Adds cart to the list of carts
                list.add(o);
            }


        } catch (SQLException e){
            log.warn("Error while creating cart list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
