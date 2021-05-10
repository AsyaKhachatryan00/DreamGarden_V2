package com.example.dreamgarden.ui.food.foodDetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Foods;

public class FoodDetailsViewModel extends ViewModel {

    private MutableLiveData<Foods> mutableLiveData;
    public FoodDetailsViewModel() { }

    public MutableLiveData<Foods> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.selectedFood);
        return mutableLiveData;
    }
}