package com.revature.models;

import java.util.Objects;

public class SellerApplication {

    private String status;
    private User user;
    private int id;

    public SellerApplication(){}

    public SellerApplication(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellerApplication that = (SellerApplication) o;
        return id == that.id && Objects.equals(status, that.status) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, user, id);
    }

    @Override
    public String toString() {
        return "SellerApplication{" +
                "status='" + status + '\'' +
                ", user=" + user +
                ", id=" + id +
                '}';
    }
}
