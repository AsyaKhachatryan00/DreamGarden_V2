package com.example.dreamgarden.Callback;

import com.example.dreamgarden.Models.Tables;

import java.util.List;

public interface iTableCallbackListener {
    void onTablesLoadSuccess(List<Tables> tables);
    void onGTablesLoadFailed(String message);
}
