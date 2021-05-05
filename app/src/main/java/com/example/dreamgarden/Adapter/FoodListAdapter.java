package com.example.dreamgarden.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dreamgarden.Callback.IRecyclerClickListener;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Database.CartDataSource;
import com.example.dreamgarden.Database.CartDatabase;
import com.example.dreamgarden.Database.CartItem;
import com.example.dreamgarden.Database.LocalCartDataSource;
import com.example.dreamgarden.EventBus.CounterCartEvent;
import com.example.dreamgarden.EventBus.FoodItemClick;
import com.example.dreamgarden.Models.Foods;
import com.example.dreamgarden.Models.Gallery;
import com.example.dreamgarden.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FoodListAdapter  extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    private Context context;
    private List<Foods> foodsList;
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;

    public FoodListAdapter(Context context, List<Foods> foodsList) {
        this.context = context;
        this.foodsList = foodsList;
        this.compositeDisposable = new CompositeDisposable();
        this.cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.food_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(foodsList.get(position).getImage())
                .into(holder.food_image);
        holder.food_name.setText(new StringBuilder(foodsList.get(position).getName()));

        holder.setListener((view, pos) -> {
           Common.selectedFood = foodsList.get(pos);
            EventBus.getDefault().postSticky(new FoodItemClick(true, foodsList.get(pos)));
        });

        holder.img_cart.setOnClickListener(v -> {
            CartItem cartItem = new CartItem();
            cartItem.setUid(Common.currentUser.getUid());
            cartItem.setUserPhone(Common.currentUser.getPNumber());

            cartItem.setFoodId(foodsList.get(position).getMenuId());
            cartItem.setFoodName(foodsList.get(position).getName());
            cartItem.setFoodImage(foodsList.get(position).getImage());
            cartItem.setFoodPrice(Double.valueOf(String.valueOf(foodsList.get(position).getPrice())));
            cartItem.getFoodCount();
            cartItem.setFoodExtraPrice(0.0);

           compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe(() -> {
               Toast.makeText(context, "Add to Cart success", Toast.LENGTH_SHORT).show();
               EventBus.getDefault().postSticky(new CounterCartEvent(true));
               }, throwable -> {
                       Toast.makeText(context, "[CART ERROR]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                   }));


        });

    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.foods_name)
        TextView food_name;
        @BindView(R.id.foods_image)
        ImageView food_image;
        @BindView(R.id.img_fav)
        ImageView fav_img;
        @BindView(R.id.img_order)
        ImageView img_cart;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v, getAdapterPosition());
        }
    }

}
