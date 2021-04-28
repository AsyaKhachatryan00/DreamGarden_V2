package com.example.dreamgarden.ui.foodDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Foods;
import com.example.dreamgarden.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoodDetailsFragment extends Fragment {

    private FoodDetailsViewModel mViewModel;
    private Unbinder unbinder;
    @BindView(R.id.food)
    ImageView image_food;
    @BindView(R.id.btnCart)
    FloatingActionButton btn_cart;
    @BindView(R.id.foodPrice)
    TextView price;
    @BindView(R.id.nameOfFood)
    TextView name;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.count)
    ElegantNumberButton count;
    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.fav)
    FloatingActionButton favorite;


    public static FoodDetailsFragment newInstance() {
        return new FoodDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(FoodDetailsViewModel.class);

        View view =  inflater.inflate(R.layout.fragment_food_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        mViewModel.getMutableLiveData().observe(this, foods -> {
            displayInfo(foods);
        });

        return  view;
    }

    private void displayInfo(Foods foods) {
        Glide.with(getContext()).load(foods.getImage())
                .into(image_food);
        name.setText(new StringBuilder(foods.getName()));
        price.setText(new StringBuilder(foods.getPrice().toString()));
        description.setText(new StringBuilder(foods.getDescription()));

        ((AppCompatActivity)getActivity())
                .getSupportActionBar()
                .setTitle(Common.selectedFood.getName());

    }


}