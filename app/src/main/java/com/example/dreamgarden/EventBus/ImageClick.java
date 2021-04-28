package com.example.dreamgarden.EventBus;

import com.example.dreamgarden.Models.Images;

public class ImageClick {

    private  boolean success;
    private Images images;

    public ImageClick(boolean success, Images images) {
        this.success = success;
        this.images = images;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}
