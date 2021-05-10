package com.example.dreamgarden.Database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDataSource {

    Flowable<List<CartItem>> getAllCart(String uId);

    Single<Integer> countItemCart(String uId);

    Single<Double> sumPriceInCart(String uId);

    Single<CartItem> getItemInCart(String foodId, String uId);

    Completable insertOrReplaceAll(CartItem... cartItems);

    Single<Integer> updateCartItems(CartItem cartItems);

    Single<Integer> deleteCartItem(CartItem cartItem);

    Single<Integer> cleanCart(String uId);

    Single<CartItem> getAllItemInCart(String foodId, String uId);


}
