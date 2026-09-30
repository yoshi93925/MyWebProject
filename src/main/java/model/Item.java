package model;

import java.io.Serializable;
import java.sql.Timestamp;
import util.DateUtil;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private int itemId;
    private String itemName;
    private int stock;
    private String location;
    private int genreId;
    private Timestamp updatedAt;

    public Item() {
    }

    public Item(int itemId, String itemName, int stock, String location, int genreId, Timestamp updatedAt) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.stock = stock;
        this.location = location;
        this.genreId = genreId;
        this.updatedAt = updatedAt;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getGenreName() {
        return switch (this.genreId) {
            case 1 -> "食料・飲料";
            case 2 -> "衛生・医療用品";
            case 3 -> "生活・日用品";
            case 4 -> "防寒・睡眠・衣類";
            case 5 -> "インフラ・環境整備";
            default -> "その他";
        };
    }

    public String getUpdatedAtFormatted() {
        return DateUtil.formatDetailDate(this.updatedAt);
    }
}
