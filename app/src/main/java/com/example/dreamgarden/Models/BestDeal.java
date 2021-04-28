package com.example.dreamgarden.Models;

public class BestDeal {

    private String MenuId, FoodId, Name, Image;


    public BestDeal() {
    }

    public BestDeal(String menuId, String foodId, String name, String image) {
        MenuId = menuId;
        FoodId = foodId;
        Name = name;
        Image = image;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}