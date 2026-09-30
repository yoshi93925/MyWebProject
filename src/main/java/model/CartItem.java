package model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int itemId;
    private String itemName;
    private String location;
    private int genreId;
    private int quantity;
    private int maxStock;

    public CartItem() {
    }

    public CartItem(int itemId, String itemName, String location, int genreId, int quantity, int maxStock) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.location = location;
        this.genreId = genreId;
        this.quantity = quantity;
        this.maxStock = maxStock;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }
}
