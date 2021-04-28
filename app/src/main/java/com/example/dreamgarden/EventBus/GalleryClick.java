package com.example.dreamgarden.EventBus;

import com.example.dreamgarden.Models.Gallery;

public class GalleryClick {

    private  boolean success;
    private Gallery gallery;

    public GalleryClick(boolean success, Gallery gallery) {
        this.success = success;
        this.gallery = gallery;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
