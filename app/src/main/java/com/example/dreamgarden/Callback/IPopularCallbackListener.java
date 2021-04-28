package com.example.dreamgarden.Callback;

import com.example.dreamgarden.Models.PopularCategory;

import java.util.List;

public interface IPopularCallbackListener {
    void onPopularLoadSuccess(List<PopularCategory> popularCategories);
    void onPopularLoadFailed(String message);
}
