package com.example.dreamgarden.ui.tables;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.dreamgarden.Callback.iTableCallbackListener;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Tables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TablesViewModel extends ViewModel implements iTableCallbackListener {
    private MutableLiveData<List<Tables>> mutableLiveData;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private iTableCallbackListener iTableCallbackListener;

    public TablesViewModel() { iTableCallbackListener = this;   }

    public MutableLiveData<List<Tables>> getMutableLiveData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            LoadTableListMutable();
        } return mutableLiveData;
    }

    private void LoadTableListMutable() {
        List<Tables> tempList = new ArrayList<>();
        DatabaseReference tableRef = FirebaseDatabase.getInstance().getReference(Common.TABLES_REF);
        tableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot:snapshot.getChildren()){
                    Tables tablesModel = itemSnapshot.getValue(Tables.class);
                    tablesModel.setTableId(itemSnapshot.getKey());
                    tempList.add(tablesModel);
                }
                iTableCallbackListener.onTablesLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iTableCallbackListener.onGTablesLoadFailed(error.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onTablesLoadSuccess(List<Tables> tables) {
        mutableLiveData.setValue(tables);
    }

    @Override
    public void onGTablesLoadFailed(String message) {
        messageError.setValue(message);
    }
}