package com.example.dreamgarden.EventBus;

import com.example.dreamgarden.Models.Foods;

public class FoodItemClick {
    private boolean success;
    private Foods foods;

    public FoodItemClick(boolean success, Foods foods) {
        this.success = success;
        this.foods = foods;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Foods getFoods() {
        return foods;
    }

    public void setFoods(Foods foods) {
        this.foods = foods;
    }
}
