package com.example.dreamgarden.ui.image;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Images;


public class ImageViewModel extends ViewModel {
    private MutableLiveData<Images> mutableLiveData;

    public ImageViewModel() {   }

    public MutableLiveData<Images> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Common.selectedImage);
        return mutableLiveData;
    }

}