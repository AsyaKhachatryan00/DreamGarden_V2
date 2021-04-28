package com.example.dreamgarden.Callback;

import com.example.dreamgarden.Models.BestDeal;

import java.util.List;

public interface IBestDealCallbackListener {
    void onBestDealLoadSuccess(List<BestDeal> bestDeals);
    void onBestDialLoadFailed(String message);
}
