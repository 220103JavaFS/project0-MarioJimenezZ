package com.revature.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerOrder {

    private int orderId;
    private int userId;
    private List<Product> productList;
    private double orderTotal;
    private String orderStatus;
    private Timestamp orderDate;

    public CustomerOrder(){
        productList = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public boolean addProduct(Product p) { return this.productList.add(p); }

    public boolean removeProduct(Product p) { return this.productList.remove(p); }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", productList=" + productList +
                ", orderTotal=" + orderTotal +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate='" + orderDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerOrder customerOrder = (CustomerOrder) o;
        return orderId == customerOrder.orderId && userId == customerOrder.userId && Double.compare(customerOrder.orderTotal, orderTotal) == 0 && Objects.equals(productList, customerOrder.productList) && Objects.equals(orderStatus, customerOrder.orderStatus) && Objects.equals(orderDate, customerOrder.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, productList, orderTotal, orderStatus, orderDate);
    }
}
