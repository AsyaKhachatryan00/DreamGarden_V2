package com.example.dreamgarden.ui.images;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Images;

import java.util.List;

public class ImagesViewModel extends ViewModel {

    private MutableLiveData<List<Images>> listMutableLiveData;

    public ImagesViewModel() {

    }

    public MutableLiveData<List<Images>> getListMutableLiveData() {
        if (listMutableLiveData == null)
            listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.setValue(Common.gallerySelected.getImages());
        return listMutableLiveData;
    }
}