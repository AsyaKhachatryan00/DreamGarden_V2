package com.example.dreamgarden.ui.foodList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import com.example.dreamgarden.Adapter.FoodListAdapter;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoodList extends Fragment {

    private FoodListViewModel mViewModel;

    Unbinder unbinder;
    @BindView(R.id.food_list)
    RecyclerView food_list;
    LayoutAnimationController layoutAnimationController;
    FoodListAdapter adapter ;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(FoodListViewModel.class);

        View root = inflater.inflate(R.layout.fragment_food_list, container, false);

        unbinder = ButterKnife.bind(this, root);

        initView();

        mViewModel.getListMutableLiveData().observe(this, foods -> {
            adapter = new FoodListAdapter(getContext(), foods);
            food_list.setAdapter(adapter);
            food_list.setLayoutAnimation(layoutAnimationController);
        });

        return root;
    }

    private void initView() {

        food_list.setHasFixedSize(true);
        food_list.setLayoutManager(new LinearLayoutManager(getContext()));

        ((AppCompatActivity)getActivity())
                .getSupportActionBar()
                .setTitle(Common.categorySelected.getName());

       layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.item_from_left);

    }

}

