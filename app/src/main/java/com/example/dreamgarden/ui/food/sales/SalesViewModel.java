package com.example.dreamgarden.ui.food.sales;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Callback.IBestDealCallbackListener;
import com.example.dreamgarden.Callback.IPopularCallbackListener;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.BestDeal;
import com.example.dreamgarden.Models.PopularCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SalesViewModel extends ViewModel implements IPopularCallbackListener, IBestDealCallbackListener {

    private MutableLiveData<List<PopularCategory>> popularList;
    private MutableLiveData<List<BestDeal>> bestDealList;
    private MutableLiveData<String> messageError;
    private IPopularCallbackListener popularCallbackListener;
    private IBestDealCallbackListener bestDealCallbackListener;

    public SalesViewModel() {
        popularCallbackListener = this;
        bestDealCallbackListener = this;
    }

    public MutableLiveData<List<BestDeal>> getBestDealList() {
        if (bestDealList == null) {
            bestDealList = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadBestDealList();
        }
        return bestDealList;
    }

    private void loadBestDealList() {
        List<BestDeal> tempList = new ArrayList<>();
        DatabaseReference bestDealRef = FirebaseDatabase.getInstance().getReference(Common.BEST_DEALS_REF);
        bestDealRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapShot:snapshot.getChildren()) {
                    BestDeal model = itemSnapShot.getValue(BestDeal.class);
                    tempList.add(model);
                }
                bestDealCallbackListener.onBestDealLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bestDealCallbackListener.onBestDialLoadFailed(error.getMessage());
            }
        });
    }

    public MutableLiveData<List<PopularCategory>> getPopularList() {
        if (popularList == null) {
            popularList = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadPopularList();
        }
        return popularList;
    }

    private void loadPopularList() {
        List<PopularCategory> tempList = new ArrayList<>();
        DatabaseReference popularRef = FirebaseDatabase.getInstance().getReference(Common.POPULAR_CATEGORY_REF);
        popularRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapShot:snapshot.getChildren()) {
                    PopularCategory model = itemSnapShot.getValue(PopularCategory.class);
                    tempList.add(model);
                }
                popularCallbackListener.onPopularLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                popularCallbackListener.onPopularLoadFailed(error.getMessage());

            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onPopularLoadSuccess(List<PopularCategory> popularCategories) {
        popularList.setValue(popularCategories);
    }

    @Override
    public void onPopularLoadFailed(String message) {
        messageError.setValue(message);
    }

    @Override
    public void onBestDealLoadSuccess(List<BestDeal> bestDeals) {
        bestDealList.setValue(bestDeals);
    }

    @Override
    public void onBestDialLoadFailed(String message) {
    messageError.setValue(message);
    }
}