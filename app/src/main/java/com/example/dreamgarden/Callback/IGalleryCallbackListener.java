package com.example.dreamgarden.Callback;

import com.example.dreamgarden.Models.Gallery;

import java.util.List;

public interface IGalleryCallbackListener {
    void onGalleryLoadSuccess(List<Gallery> galleries);
    void onGalleryLoadFailed(String message);
}
