package com.revature.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart {
    private List<Product> products;
    private int userId;
    private int Id;

    public Cart() {
        products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product p){
        this.products.add(p);
    }

    public void removeProduct(Product p){
        this.products.remove(p);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return userId == cart.userId && Objects.equals(products, cart.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, userId);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "products=" + products +
                ", user_id=" + userId +
                '}';
    }
}
