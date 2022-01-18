package com.revature.dao;

import com.revature.models.products.Category;
import com.revature.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class CategoryDAOImpl implements CategoryDAO {

    private final Logger log = LoggerFactory.getLogger(CategoryDAOImpl.class);

    @Override
    public boolean save(Category c) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("INSERT INTO  categories (name, description)" +
                    " VALUES (?,?);");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setString(++count, c.getName());
            statement.setString(++count, c.getDescription());

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while saving category : " + c);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Category c) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("UPDATE categories SET" +
                    " name = ?, description = ? WHERE id = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setString(++count, c.getName());
            statement.setString(++count, c.getDescription());
            statement.setInt(++count, c.getId());

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while updating category : " + c);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM categories WHERE id = " + id + ";");
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql.toString());

            return true;

        } catch (SQLException e) {
            log.warn("Error while deleting category id : " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<Category> getAll() {
        ArrayList<Category> list = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM categories ORDER BY id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                Category c = new Category();
                c.setName(result.getString("name"));
                c.setDescription(result.getString("description"));
                c.setId(result.getInt("id"));
                c.setProducts(result.getInt("products"));

                list.add(c);
            }


        } catch (SQLException e){
            log.warn("Error while creating category list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Category get(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM categories WHERE id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                Category category = new Category();

                category.setName(result.getString("name"));
                category.setDescription(result.getString("description"));
                category.setId(result.getInt("id"));
                category.setProducts(result.getInt("products"));

                return category;
            }


        } catch (SQLException e){
            log.warn("Error while getting category by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;

    }
}
