package com.example.dreamgarden.Database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalCartDataSource implements CartDataSource{

    private CartDAO cartDAO;

    public LocalCartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Override
    public Flowable<List<CartItem>> getAllCart(String uId) {
        return cartDAO.getAllCart(uId);
    }

    @Override
    public Single<Integer> countItemCart(String uId) {
        return cartDAO.countItemCart(uId);
    }

    @Override
    public Single<Long> sumPriceInCart(String uId) {
        return cartDAO.sumPriceInCart(uId);
    }

    @Override
    public Single<CartItem> getItemInCart(String foodId, String uId) {
        return cartDAO.getItemInCart(foodId, uId);
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItems) {
        return cartDAO.insertOrReplaceAll(cartItems);
    }

    @Override
    public Single<Integer> updateCartItems(CartItem cartItems) {
        return cartDAO.updateCartItems(cartItems);
    }

    @Override
    public Single<Integer> deleteCartItem(CartItem cartItem) {
        return cartDAO.deleteCartItem(cartItem);
    }

    @Override
    public Single<Integer> cleanCart(String uId) {
        return cartDAO.cleanCart(uId);
    }
}
