package com.disco.skeletalproduct;

public class Shop {
    private int imageId;
    private String itemName;
    private int itemPrice;

    public Shop(int imageId, String itemName, int itemPrice){
        this.imageId = imageId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
}
