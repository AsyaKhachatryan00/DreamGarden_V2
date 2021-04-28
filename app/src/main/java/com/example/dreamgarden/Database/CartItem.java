package com.example.dreamgarden.Database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Cart")
public class CartItem {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "foodId")
    private String foodId;

    @ColumnInfo(name = "foodName")
    private String foodName;

     @ColumnInfo(name = "foodImage")
    private String foodImage;
     @ColumnInfo(name = "foodPrice")
    private String foodPrice;
    @ColumnInfo(name = "foodCount")
    private String foodCount;
    @ColumnInfo(name = "foodExtraPrice")
    private String foodExtraPrice;
    @ColumnInfo(name = "userPhone")
    private String userPhone;
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

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(String foodCount) {
        this.foodCount = foodCount;
    }

    public String getFoodExtraPrice() {
        return foodExtraPrice;
    }

    public void setFoodExtraPrice(String foodExtraPrice) {
        this.foodExtraPrice = foodExtraPrice;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
