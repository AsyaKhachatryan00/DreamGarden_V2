package com.example.dreamgarden.EventBus;

public class GetTablesClick {
    private  boolean success;

    public GetTablesClick(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
