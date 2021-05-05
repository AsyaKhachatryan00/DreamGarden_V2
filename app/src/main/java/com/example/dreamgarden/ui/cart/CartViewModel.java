package com.example.dreamgarden.ui.cart;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Database.CartDataSource;
import com.example.dreamgarden.Database.CartDatabase;
import com.example.dreamgarden.Database.CartItem;
import com.example.dreamgarden.Database.LocalCartDataSource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;
    private MutableLiveData<List<CartItem>> listMutableLiveData;

    public void onStop() {
        compositeDisposable.clear();
    }

    public MutableLiveData<List<CartItem>> getListMutableLiveData() {
        if (listMutableLiveData == null)
            listMutableLiveData = new MutableLiveData<>();
        getAllCartItems();
        return listMutableLiveData;
    }

    public CartViewModel() {
        compositeDisposable = new CompositeDisposable();
    }

    public void initCartDataSource(Context context) {
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    private void getAllCartItems() {
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    listMutableLiveData.setValue(cartItems);
                }, throwable -> {
                    listMutableLiveData.setValue(null);
                }));
    }
}