package com.example.dreamgarden.ui.gallery;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Callback.IGalleryCallbackListener;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Gallery;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewHolder extends ViewModel implements IGalleryCallbackListener {

    private MutableLiveData<List<Gallery>> galleryListMutable;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private IGalleryCallbackListener iGalleryCallbackListener;

    public GalleryViewHolder() {
        iGalleryCallbackListener = this;     }

    public MutableLiveData<List<Gallery>> getGalleryListMutable() {
        if (galleryListMutable == null) {
            galleryListMutable = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            LoadGalleryListMutable();
        }
        return galleryListMutable;
    }

    private void LoadGalleryListMutable() {
        List<Gallery> tempList = new ArrayList<>();
        DatabaseReference galleryRef = FirebaseDatabase.getInstance().getReference(Common.GALLERY_REF);
        galleryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot:snapshot.getChildren()) {
                    Gallery galleryModel = itemSnapshot.getValue(Gallery.class);
                    galleryModel.setImageId(itemSnapshot.getKey());
                    tempList.add(galleryModel);
                }
                iGalleryCallbackListener.onGalleryLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iGalleryCallbackListener.onGalleryLoadFailed(error.getMessage());

            }
        });

    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onGalleryLoadSuccess(List<Gallery> galleries) {
        galleryListMutable.setValue(galleries);
    }

    @Override
    public void onGalleryLoadFailed(String message) {
        messageError.setValue(message);
    }
}
