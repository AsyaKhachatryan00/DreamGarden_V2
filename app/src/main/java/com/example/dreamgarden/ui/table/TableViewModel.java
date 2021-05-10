package com.example.dreamgarden.ui.table;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Tables;

public class TableViewModel extends ViewModel {
    private MutableLiveData<Tables> mutableLiveData;

    public TableViewModel() {    }

    public MutableLiveData<Tables> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.selectedTable);
        return mutableLiveData;
    }
}