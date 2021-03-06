package com.example.dreamgarden.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Cart", primaryKeys = {"foodId","uId" } )

public class CartItem {
    @NonNull
    @ColumnInfo(name = "foodId")
    private String foodId;

    @ColumnInfo(name = "foodName")
    private String foodName;

     @ColumnInfo(name = "foodImage")
    private String foodImage;

     @ColumnInfo(name = "foodPrice")
    private Double foodPrice;

    @ColumnInfo(name = "foodCount")
    private int foodCount;

    @ColumnInfo(name = "foodExtraPrice")
    private Double foodExtraPrice;

    @ColumnInfo(name = "userPhone")
    private String userPhone;

    @NonNull
    @ColumnInfo(name = "uId")
    private String uid;

    @NonNull
    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(@NonNull String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public Double getFoodExtraPrice() {
        return foodExtraPrice;
    }

    public void setFoodExtraPrice(Double foodExtraPrice) {
        this.foodExtraPrice = foodExtraPrice;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CartItem))
            return false;
        CartItem cartItem = (CartItem)obj;
        return cartItem.getFoodId().equals(this.foodId);
    }
}
