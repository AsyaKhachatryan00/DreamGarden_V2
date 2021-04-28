package com.example.dreamgarden.ui.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.example.dreamgarden.Adapter.BestDealsAdapter;
import com.example.dreamgarden.Adapter.PopularCategoriesAdapter;
import com.example.dreamgarden.Models.PopularCategory;
import com.example.dreamgarden.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SalesFragment extends Fragment {

    private SalesViewModel salesViewModel;

    Unbinder unbinder;

    @BindView(R.id.pop_categories)
    RecyclerView pop_categories;
    @BindView(R.id.viewpager)
    LoopingViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        salesViewModel =
                new ViewModelProvider(this).get(SalesViewModel.class);

        View root = inflater.inflate(R.layout.fragment_sales, container, false);
        unbinder = ButterKnife.bind(this, root);

        init();

        salesViewModel.getPopularList().observe(this, popularCategories -> {
            PopularCategoriesAdapter adapter = new PopularCategoriesAdapter(getContext(), popularCategories);
            pop_categories.setAdapter(adapter);
        });

        salesViewModel.getBestDealList().observe(this, bestDeals -> {
            BestDealsAdapter adapter = new BestDealsAdapter(getContext(), bestDeals, true);
            viewPager.setAdapter(adapter);
        });

        return root;
    }

    private void init() {
        pop_categories.setHasFixedSize(true);
        pop_categories.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    public void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }
}