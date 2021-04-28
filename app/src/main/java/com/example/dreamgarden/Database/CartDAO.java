package com.example.dreamgarden.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart WHERE uId=:uId")
    Flowable<List<CartItem>> getAllCart(String uId);

    @Query("SELECT COUNT(*) from Cart WHERE uId=:uId")
    Single<Integer> countItemCart(String uId);

    @Query("SELECT SUM(foodPrice*foodCount) + (foodExtraPrice*foodCount) FROM Cart WHERE uId=:uId")
    Single<Long> sumPriceInCart(String uId);

    @Query("SELECT * FROM Cart WHERE foodId=:foodId AND uId=:uId")
    Single<CartItem> getItemInCart(String foodId, String uId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrReplaceAll(CartItem... cartItems);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Single<Integer> updateCartItems(CartItem cartItems);

    @Delete
    Single<Integer> deleteCartItem(CartItem cartItem);

    @Query("DELETE FROM Cart WHERE uId=:uId")
    Single<Integer> cleanCart(String uId);


}
