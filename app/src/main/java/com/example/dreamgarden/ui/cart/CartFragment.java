package com.example.dreamgarden.ui.cart;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamgarden.Adapter.CartAdapter;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Database.CartDataSource;
import com.example.dreamgarden.Database.CartDatabase;
import com.example.dreamgarden.Database.CartItem;
import com.example.dreamgarden.Database.LocalCartDataSource;
import com.example.dreamgarden.EventBus.HideFABCart;
import com.example.dreamgarden.EventBus.UpdateItemInCart;
import com.example.dreamgarden.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartFragment extends Fragment {

    private CartViewModel mViewModel;
    private Parcelable recyclerViewState;
    private CartDataSource cartDataSource;

    @BindView(R.id.rec_cart)
    RecyclerView rec_cart;
    @BindView(R.id.food_price)
    TextView food_price;
    @BindView(R.id.empty_cart)
    TextView empty_cart;
    @BindView(R.id.group_place_holder)
    CardView group_place_holder;
    @BindView(R.id.empty)
    ImageView empty;

    private Unbinder unbinder;

    public static CartFragment newInstance() {
        return new CartFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        View root =  inflater.inflate(R.layout.cart_fragment, container, false);
        mViewModel.initCartDataSource(getContext());
        mViewModel.getListMutableLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                if (cartItems == null || cartItems.isEmpty() ) {
                    rec_cart.setVisibility(View.GONE);
                    group_place_holder.setVisibility(View.GONE);
                    empty_cart.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.INVISIBLE);
                } else {
                    rec_cart.setVisibility(View.VISIBLE);
                    group_place_holder.setVisibility(View.VISIBLE);
                    empty_cart.setVisibility(View.GONE);
                    empty.setVisibility(View.GONE);

                    CartAdapter adapter = new CartAdapter(getContext(), cartItems);
                    rec_cart.setAdapter(adapter);
                }
            }
        });

        unbinder = ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDAO());

        EventBus.getDefault().postSticky(new HideFABCart(true));

        rec_cart.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rec_cart.setLayoutManager(layoutManager);
        rec_cart.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().postSticky(new HideFABCart(false));
        mViewModel.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateInCartEvent(UpdateItemInCart event){
        if (event.getCartItem() != null ) {
            recyclerViewState = rec_cart.getLayoutManager().onSaveInstanceState();
            cartDataSource.updateCartItems(event.getCartItem())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull Integer integer) {
                            calculateTotalPrice();
                            rec_cart.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(getContext(), "[UPDATE CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void calculateTotalPrice() {
        cartDataSource.sumPriceInCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Double>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Double price) {
                        food_price.setText(new StringBuilder("Price: ")
                        .append(Common.formatPrice(price)));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getContext(), "[SUM CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}