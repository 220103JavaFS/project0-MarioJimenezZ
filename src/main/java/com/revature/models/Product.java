package com.revature.models;

import java.util.Objects;

public class Product {

    String name;
    int categoryId;
    String description;
    double price;
    int id;
    int sellerId;

    public Product(String name, int categoryId, String description, double price, int sellerId) {
        this.name = name;
        this.categoryId = categoryId;
        this.description = description;
        this.price = price;
        this.sellerId = sellerId;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSellerId() { return sellerId; }

    public void setSellerId(int sellerId) { this.sellerId = sellerId;  }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", id=" + id +
                ", sellerId=" + sellerId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return categoryId == product.categoryId && Double.compare(product.price, price) == 0 && id == product.id && sellerId == product.sellerId && Objects.equals(name, product.name) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, categoryId, description, price, id, sellerId);
    }
}
