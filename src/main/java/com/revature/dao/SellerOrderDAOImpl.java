package com.revature.dao;

import com.revature.models.Product;
import com.revature.models.SellerOrder;
import com.revature.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellerOrderDAOImpl implements SellerOrderDAO {

    private ProductDAO productDAO = new ProductDAOImpl();

    private final Logger log = LoggerFactory.getLogger(SellerOrderDAOImpl.class);

    @Override
    public boolean save(SellerOrder o) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("INSERT INTO  seller_orders (buyer_id, seller_id, customer_order_id," +
                    "product_list, order_date, order_total, order_status) VALUES (?,?,?,?,?,?,?);");

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
            statement.setInt(++count, o.getBuyerId());
            statement.setInt(++count, o.getSellerId());
            statement.setInt(++count, o.getCustomerOrderId());
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
    public boolean update(SellerOrder o) {

        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("UPDATE seller_orders SET" +
                    " order_status = ? WHERE order_id = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setString(++count, o.getOrderStatus());
            statement.setInt(++count, o.getOrderId());

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while updating seller order : " + o);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM seller_orders WHERE order_id = " + id + ";");
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
    public boolean deleteByCustomerOderId(int id){
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM seller_orders WHERE customer_order_id = " + id + ";");
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
    public boolean cancelByCustomerOderId(int id){
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("UPDATE seller_orders SET order_status = 'CANCELLED' WHERE " +
                    "customer_order_id = " + id + ";");
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
    public ArrayList<SellerOrder> getAll() {
        // Creates new list of carts
        ArrayList<SellerOrder> list = new ArrayList<>();
        // Tries for connection
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM seller_orders ORDER BY order_id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                // Creates new cart
                SellerOrder o = new SellerOrder();
                // Sets Id from DB
                o.setOrderId(result.getInt("order_id"));
                // Sets Buyer User Id from DB
                o.setBuyerId(result.getInt("buyer_id"));
                // Sets Seller User Id from DB
                o.setSellerId(result.getInt("seller_id"));
                // Sets Customer Order Id from db
                o.setCustomerOrderId(result.getInt("customer_order_id"));
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
    public SellerOrder get(int id) {

        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM seller_orders WHERE order_id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                // Creates new cart
                SellerOrder o = new SellerOrder();
                // Sets Id from DB
                o.setOrderId(result.getInt("order_id"));
                // Sets Buyer User Id from DB
                o.setBuyerId(result.getInt("buyer_id"));
                // Sets Seller User Id from DB
                o.setSellerId(result.getInt("seller_id"));
                // Sets Customer Order Id from db
                o.setCustomerOrderId(result.getInt("customer_order_id"));
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
    public ArrayList<SellerOrder> getBySellerId(int id){
        // Creates new list of carts
        ArrayList<SellerOrder> list = new ArrayList<>();
        // Tries for connection
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM seller_orders WHERE seller_id = " + id + " ORDER BY order_date ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                // Creates new cart
                SellerOrder o = new SellerOrder();
                // Sets Id from DB
                o.setOrderId(result.getInt("order_id"));
                // Sets Buyer User Id from DB
                o.setBuyerId(result.getInt("buyer_id"));
                // Sets Seller User Id from DB
                o.setSellerId(result.getInt("seller_id"));
                // Sets Customer Order Id from db
                o.setCustomerOrderId(result.getInt("customer_order_id"));
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
