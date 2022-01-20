package com.revature.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SellerOrder {

    private int orderId;
    private int buyerId;
    private int sellerId;
    private int customerOrderId;
    private List<Product> productList;
    private double orderTotal;
    private String orderStatus;
    private Timestamp orderDate;


    public SellerOrder(){
        productList = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

        public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(int customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public boolean addProduct(Product p){
        return this.productList.add(p);
    }

    public boolean removeProduct(Product p){
        return this.removeProduct(p);
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellerOrder that = (SellerOrder) o;
        return orderId == that.orderId && buyerId == that.buyerId && sellerId == that.sellerId && Double.compare(that.orderTotal, orderTotal) == 0 && Objects.equals(productList, that.productList) && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(orderDate, that.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, buyerId, sellerId, productList, orderTotal, orderStatus, orderDate);
    }

    @Override
    public String toString() {
        return "SellerOrder{" +
                "orderId=" + orderId +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", productList=" + productList +
                ", orderTotal=" + orderTotal +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}
