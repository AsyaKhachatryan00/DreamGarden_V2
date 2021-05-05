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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Database.CartDataSource;
import com.example.dreamgarden.Database.CartDatabase;
import com.example.dreamgarden.Database.CartItem;
import com.example.dreamgarden.Database.LocalCartDataSource;
import com.example.dreamgarden.EventBus.CounterCartEvent;
import com.example.dreamgarden.Models.Foods;
import com.example.dreamgarden.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FoodDetailsFragment extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CartDataSource cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDAO());;

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

    @OnClick(R.id.btnCart)
    void onCartItemAdd() {
        CartItem cartItem = new CartItem();
        cartItem.setUid(Common.currentUser.getUid());
        cartItem.setUserPhone(Common.currentUser.getPNumber());

        cartItem.setFoodId(Common.selectedFood.getMenuId());
        cartItem.setFoodName(Common.selectedFood.getName());
        cartItem.setFoodImage(Common.selectedFood.getImage());
        cartItem.setFoodPrice(Double.valueOf(String.valueOf(Common.selectedFood.getPrice())));
        cartItem.getFoodCount();
        cartItem.setFoodExtraPrice(0.0);

        compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(getContext(), "Add to Cart success", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().postSticky(new CounterCartEvent(true));
                }, throwable -> {
                    Toast.makeText(getContext(), "[CART ERROR]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));


    }


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