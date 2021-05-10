package com.example.dreamgarden.ui.food.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Callback.ICategoryCallbackListener;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MenuViewHolder extends ViewModel implements ICategoryCallbackListener {

    private MutableLiveData<List<Category>> categoryListMutable;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private ICategoryCallbackListener categoryCallbackListener;

    public MenuViewHolder() {
        categoryCallbackListener = this;
    }

    public MutableLiveData<List<Category>> getCategoryListMutable() {
        if (categoryListMutable == null) {
            categoryListMutable = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            LoadCategoryListMutable();
        }
        return categoryListMutable;
    }

    private void LoadCategoryListMutable() {
        List<Category> tempList = new ArrayList<>();
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF);
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot:snapshot.getChildren()) {
                 Category categoryModel = itemSnapshot.getValue(Category.class);
                 categoryModel.setMenuId(itemSnapshot.getKey());
                 tempList.add(categoryModel);
                }
                categoryCallbackListener.onCategoryLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryCallbackListener.onCategoryLoadFailed(error.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onCategoryLoadSuccess(List<Category> categories) {
        categoryListMutable.setValue(categories);
    }

    @Override
    public void onCategoryLoadFailed(String message) {
        messageError.setValue(message);
    }
}