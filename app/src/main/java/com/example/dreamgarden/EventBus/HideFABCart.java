package com.example.dreamgarden.EventBus;

public class HideFABCart {
    private boolean hidden;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public HideFABCart(boolean hidden) {
        this.hidden = hidden;
    }
}
