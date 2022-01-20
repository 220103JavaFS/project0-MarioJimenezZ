package com.revature.dao;

import com.revature.models.Product;
import com.revature.utils.ConnectionUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAOImpl implements ProductDAO{

    private UserDAO userDAO = new UserDAOImpl();
    private CategoryDAO categoryDAO = new CategoryDAOImpl();

    private final Logger log = LoggerFactory.getLogger(ProductDAOImpl.class);

    @Override
    public boolean save(@NotNull Product p) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("INSERT INTO  products (seller_id, name, category_id" +
                    ", description, price) VALUES (?,?,?,?,?);");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setInt(++count, p.getSellerId());
            statement.setString(++count, p.getName());
            statement.setInt(++count, p.getCategoryId());
            statement.setString(++count, p.getDescription());
            statement.setDouble(++count, p.getPrice());

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while saving product : " + p);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean update(@NotNull Product p) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("UPDATE products SET" +
                    " seller_id = ?, name = ?, category_id = ?, description = ?," +
                    " price = ? WHERE id = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setInt(++count, p.getSellerId());
            statement.setString(++count, p.getName());
            statement.setInt(++count, p.getCategoryId());
            statement.setString(++count, p.getDescription());
            statement.setDouble(++count, p.getPrice());
            statement.setInt(++count, p.getId());

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while updating product : " + p);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean delete(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM products WHERE id = " + id + ";");
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql.toString());

            return true;

        } catch (SQLException e) {
            log.warn("Error while deleting product id : " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public ArrayList<Product> getAll() {
        ArrayList<Product> list = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM products ORDER BY id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                Product p = new Product();
                p.setName(result.getString("name"));
                p.setDescription(result.getString("description"));
                p.setPrice(result.getDouble("price"));
                p.setId(result.getInt("id"));
                p.setSellerId(result.getInt("seller_id"));
                p.setCategoryId(result.getInt("category_id"));

                list.add(p);
            }


        } catch (SQLException e){
            log.warn("Error while product list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
    @Override
    public Product get(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM products WHERE id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                Product p = new Product();
                p.setName(result.getString("name"));
                p.setDescription(result.getString("description"));
                p.setPrice(result.getDouble("price"));
                p.setId(result.getInt("id"));
                p.setSellerId(result.getInt("seller_id"));
                p.setCategoryId(result.getInt("category_id"));

                return p;
            }


        } catch (SQLException e){
            log.warn("Error while getting product by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
