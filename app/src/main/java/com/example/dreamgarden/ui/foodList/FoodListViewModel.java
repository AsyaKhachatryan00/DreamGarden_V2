package com.example.dreamgarden.ui.foodList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Foods;
import java.util.List;

public class FoodListViewModel extends ViewModel  {

    private MutableLiveData<List<Foods>> listMutableLiveData;

    public FoodListViewModel () {    }

    public MutableLiveData<List<Foods>> getListMutableLiveData() {
        if (listMutableLiveData == null){
            listMutableLiveData = new MutableLiveData<>(); }
        listMutableLiveData.setValue(Common.categorySelected.getFoods());
        return listMutableLiveData;
    }

}

