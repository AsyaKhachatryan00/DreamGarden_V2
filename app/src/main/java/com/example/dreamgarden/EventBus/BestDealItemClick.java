package com.example.dreamgarden.EventBus;

import com.example.dreamgarden.Models.BestDeal;

public class BestDealItemClick {
    private BestDeal bestDeal;

    public BestDealItemClick(BestDeal bestDeal) {
        this.bestDeal = bestDeal;
    }

    public BestDeal getBestDeal() {
        return bestDeal;
    }

    public void setBestDeal(BestDeal bestDeal) {
        this.bestDeal = bestDeal;
    }
}
