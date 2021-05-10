package com.example.dreamgarden.EventBus;

import com.example.dreamgarden.Models.PopularCategory;

public class PopularCategoryClick {
    private PopularCategory popularCategory;

    public PopularCategoryClick(PopularCategory popularCategory) {
        this.popularCategory = popularCategory;
    }

    public PopularCategory getPopularCategory() {
        return popularCategory;
    }

    public void setPopularCategory(PopularCategory popularCategory) {
        this.popularCategory = popularCategory;
    }

}
