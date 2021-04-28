package com.example.dreamgarden.Callback;

import com.example.dreamgarden.Models.Category;
import com.example.dreamgarden.Models.PopularCategory;

import java.util.List;

public interface ICategoryCallbackListener {
    void onCategoryLoadSuccess(List<Category> categories);
    void onCategoryLoadFailed(String message);
}
