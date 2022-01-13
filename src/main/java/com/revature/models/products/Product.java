package com.revature.models.products;

import java.util.Objects;

public class Product {

    String name;
    Category category;
    String description;
    double price;
    int id;
    int sellerId;

    public Product(String name, Category category, String description, double price, int id, int sellerId) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.id = id;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(category, product.category) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, description, price);
    }
}
