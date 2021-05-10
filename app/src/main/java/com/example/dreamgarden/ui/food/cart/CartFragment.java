package com.example.dreamgarden.ui.food.cart;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamgarden.Adapter.CartAdapter;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Common.MySwiperHelper;
import com.example.dreamgarden.Database.CartDataSource;
import com.example.dreamgarden.Database.CartDatabase;
import com.example.dreamgarden.Database.CartItem;
import com.example.dreamgarden.Database.LocalCartDataSource;
import com.example.dreamgarden.EventBus.CounterCartEvent;
import com.example.dreamgarden.EventBus.HideFABCart;
import com.example.dreamgarden.EventBus.UpdateItemInCart;
import com.example.dreamgarden.Models.Order;
import com.example.dreamgarden.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartFragment extends Fragment {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CartViewModel mViewModel;
    private Parcelable recyclerViewState;
    private CartDataSource cartDataSource;
    Double price = 0.0;

    private Unbinder unbinder;

    @BindView(R.id.rec_cart)
    RecyclerView rec_cart;
    @BindView(R.id.total_price)
    TextView total_price;
    @BindView(R.id.empty_cart)
    TextView empty_cart;
    @BindView(R.id.group_place_holder)
    CardView group_place_holder;
    @BindView(R.id.empty)
    ImageView empty;

    @OnClick(R.id.confirm)
    void onConfirmOrderClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Վճարում");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.confirm_order, null);

        RadioButton restoran = view.findViewById(R.id.restoran);
        RadioButton delivery = view.findViewById(R.id.delivery);
        EditText table_location = view.findViewById(R.id.edt_table);
        EditText address = view.findViewById(R.id.edt_address);
        RadioButton rdi_cod = view.findViewById(R.id.rdi_cod);
        RadioButton rdi_braintree = view.findViewById(R.id.rdi_braintree);
        RadioButton home = view.findViewById(R.id.home_address);
        RadioButton other = view.findViewById(R.id.other);
        RadioGroup group = view.findViewById(R.id.group);


        home.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                address.setText(Common.currentUser.getAddress());
            }
        });
        other.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                address.setText("");
                address.setHint("Մուտքագրեք ձեր հասցեն");
            }
        });

        restoran.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                table_location.setVisibility(View.VISIBLE);
                group.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
                address.setText("");
                Double total = Double.parseDouble(total_price.getText().toString())*0.1;
                price = price + total;
        } else { price = 0.0; }
    });

        delivery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                table_location.setVisibility(View.GONE);
                group.setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);
                address.setText(Common.currentUser.getAddress());
                price = price + 1000 ;
            } else { price = 0.0; }
        });

        builder.setView(view);
        builder.setNegativeButton("Չեղարկել", (dialog, which) -> dialog.dismiss())
       .setPositiveButton("Հաստատել", (dialog, which) -> {
           if (rdi_cod.isChecked())
               paymentCOD(address.getText().toString(), table_location.getText().toString());
       });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
    }

    private void paymentCOD(String address, String location) {
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getUid())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(cartItems -> {
            cartDataSource.sumPriceInCart(Common.currentUser.getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Double>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {  }
                        @Override
                        public void onSuccess(@NotNull Double totalPrice) {
                            double finalPrice = totalPrice + price;
                            Order order = new Order();
                            order.setUserId(Common.currentUser.getUid());
                            order.setUserName(Common.currentUser.getFName());
                            order.setUserPhone(Common.currentUser.getPNumber());
                            order.setShippingAddress(address);
                            order.setTableLocation(location);
                            order.setCartItemList(cartItems);
                            order.setTotalPayment(totalPrice);
                            order.setDiscount(0);
                            order.setFinalPayment(finalPrice);
                            order.setCod(true);
                            order.setTransactionId("Վճարել կանխիկ");
                            writeOrderToFirebase(order);
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }, throwable -> Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show()));
    }

    private void writeOrderToFirebase(Order order) {
        FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                .child(Common.createOrderNumber())
                .setValue(order)
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnCompleteListener((OnCompleteListener<Void>) task -> {
                    cartDataSource.cleanCart(Common.currentUser.getUid())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {    }

                                @Override
                                public void onSuccess(@NotNull Integer integer) {
                                    Toast.makeText(getContext(), "Պատվերը հաստատված է", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(@NotNull Throwable e) {
                                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                });
    }

    private  CartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        View root =  inflater.inflate(R.layout.cart_fragment, container, false);
        mViewModel.initCartDataSource(getContext());
        mViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                if (cartItems == null || cartItems.isEmpty() ) {
                    rec_cart.setVisibility(View.GONE);
                    group_place_holder.setVisibility(View.GONE);
                    empty_cart.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.VISIBLE);
                } else {
                    rec_cart.setVisibility(View.VISIBLE);
                    group_place_holder.setVisibility(View.VISIBLE);
                    empty_cart.setVisibility(View.GONE);
                    empty.setVisibility(View.GONE);

                    adapter = new CartAdapter(getContext(), cartItems);
                    rec_cart.setAdapter(adapter);
                }
            }
        });

        unbinder = ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        setHasOptionsMenu(true);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDAO());
        EventBus.getDefault().postSticky(new HideFABCart(true));
        rec_cart.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rec_cart.setLayoutManager(layoutManager);
        rec_cart.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        MySwiperHelper mySwiperHelper = new MySwiperHelper(getContext(), rec_cart, 350) {
 @Override
 public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {
     buf.add(new MyButton(getContext(), "Ջնջել", 50, 0, Color.parseColor("#FF3c30"),
      pos -> {
      CartItem cartItem = adapter.getItemAtPosition(pos);
      cartDataSource.deleteCartItem(cartItem)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new SingleObserver<Integer>() {
                  @Override
                  public void onSubscribe(@NotNull Disposable d) {  }
                  @Override
                  public void onSuccess(@NotNull Integer integer) {
                      adapter.notifyItemRemoved(pos);
                      sumAllItemInCart();
                      EventBus.getDefault().postSticky(new CounterCartEvent(true));
                      Toast.makeText(getContext(), "Հեռացված է !!", Toast.LENGTH_SHORT).show();
                  }
                  @Override
                  public void onError(@NotNull Throwable e) {
                      Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                  }
              });
     }));
 }
        };

        sumAllItemInCart();
    }

    private void sumAllItemInCart() {
        cartDataSource.sumPriceInCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Double>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {    }
                    @Override
                    public void onSuccess(@NotNull Double price) {
                        total_price.setText(new StringBuilder().append(price));
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        if (!e.getMessage().contains("Query returned empty"))
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_cart){
            cartDataSource.cleanCart(Common.currentUser.getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NotNull Integer integer) {
                            Toast.makeText(getContext(), "Զամբյուղը դատարկվեց", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().postSticky(new HideFABCart(false));
        mViewModel.onStop();
        EventBus.getDefault().unregister(this);
        compositeDisposable.clear();
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
                        public void onSubscribe(@NonNull Disposable d) {  }

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
                    public void onSubscribe(@NonNull Disposable d) {  }
                    @Override
                    public void onSuccess(@NonNull Double price) {
                        sumAllItemInCart();
                        total_price.setText(new StringBuilder(getResources().getString(R.string.price))
                        .append(Common.formatPrice(price)));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getContext(), "[SUM CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}