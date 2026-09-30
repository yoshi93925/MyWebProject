package model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderDetailId;
    private int itemId;
    private int orderId;
    private int quantity;
    private String itemName;
    private String location;
    private int genreId;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int itemId, int orderId, int quantity, String itemName, String location, int genreId) {
        this.orderDetailId = orderDetailId;
        this.itemId = itemId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.itemName = itemName;
        this.location = location;
        this.genreId = genreId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}
