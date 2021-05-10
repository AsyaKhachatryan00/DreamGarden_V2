package com.example.dreamgarden.EventBus;

import com.example.dreamgarden.Models.Tables;

public class TableClick {
    private  boolean success;
    private Tables tables;

    public TableClick(boolean success, Tables tables) {
        this.success = success;
        this.tables = tables;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Tables getTables() {
        return tables;
    }

    public void setTables(Tables tables) {
        this.tables = tables;
    }
}
