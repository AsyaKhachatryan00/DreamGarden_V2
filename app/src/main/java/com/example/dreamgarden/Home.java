package com.example.dreamgarden;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.andremion.counterfab.CounterFab;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Database.CartDataSource;
import com.example.dreamgarden.Database.CartDatabase;
import com.example.dreamgarden.Database.LocalCartDataSource;
import com.example.dreamgarden.EventBus.BestDealItemClick;
import com.example.dreamgarden.EventBus.CategoryClick;
import com.example.dreamgarden.EventBus.CounterCartEvent;
import com.example.dreamgarden.EventBus.FoodItemClick;
import com.example.dreamgarden.EventBus.GalleryClick;
import com.example.dreamgarden.EventBus.GetTablesClick;
import com.example.dreamgarden.EventBus.HideFABCart;
import com.example.dreamgarden.EventBus.ImageClick;
import com.example.dreamgarden.EventBus.PopularCategoryClick;
import com.example.dreamgarden.EventBus.TableClick;
import com.example.dreamgarden.Models.Category;
import com.example.dreamgarden.Models.Foods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;

    private CartDataSource cartDataSource;

    android.app.AlertDialog dialog;

    @BindView(R.id.fab)
    CounterFab fab;

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        ButterKnife.bind(this);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               navController.navigate(R.id.cartFragment);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.header);
        name.setText(Common.currentUser.getFName());

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_book,
                R.id.cartFragment, R.id.nav_sales )
        .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        countCartItem();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull @NotNull MenuItem item) {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (!handled) {
                    switch (item.getItemId()) {
                        case R.id.log_out:
                            logOut();
                            break;
                    }
                }
                drawer.closeDrawer(GravityCompat.START);
                return handled;
            }
        });
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Դուրս գալ")
           .setMessage("Ցանկանու՞մ եք դուրս գալ")
           .setNegativeButton("Ոչ", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
               }
           }).setPositiveButton("Այո", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Common.selectedFood = null;
                Common.selectedImage = null;
                Common.categorySelected = null;
                Common.gallerySelected = null;
                Common.currentUser = null;
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(Home.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCategorySelected(CategoryClick event)  {
        if (event.isSuccess()) {
            navController.navigate(R.id.nav_food_list);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onGallerySelected(GalleryClick event) {
        if (event.isSuccess()){
            navController.navigate(R.id.nav_images);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onFoodSelected(FoodItemClick event)
    {
        if (event.isSuccess()) {
            navController.navigate(R.id.nav_food_details);
        }
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onImageClicked(ImageClick event)
    {
        if (event.isSuccess()) {
            navController.navigate(R.id.nav_image);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getTableClicked(GetTablesClick event)
    {
        if (event.isSuccess()) {
            navController.navigate(R.id.nav_table);
        }
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onTableImageClicked(TableClick event)
    {
        if (event.isSuccess()) {
            navController.navigate(R.id.nav_table_image);
        }
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCartCounter(CounterCartEvent event)
    {
        if (event.isSuccess()) {
           countCartItem();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPopularItemClick(PopularCategoryClick event)
    {
    if (event.getPopularCategory() != null) {
        dialog.show();
        FirebaseDatabase.getInstance().getReference("Category")
                .child(event.getPopularCategory().getMenuId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot snapshot) {
        if (snapshot.exists()) {
            Common.categorySelected = snapshot.getValue(Category.class);

            FirebaseDatabase.getInstance().getReference("Category")
                    .child(event.getPopularCategory().getMenuId())
                    .child("foods")
                    .orderByChild("MenuId").equalTo(event.getPopularCategory().getFoodId())
                    .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot itemSnapshot:snapshot.getChildren())
                            Common.selectedFood = itemSnapshot.getValue(Foods.class);
                        navController.navigate(R.id.nav_food_details);
                    } else {
                        Toast.makeText(Home.this, "Item doesn't exists", Toast.LENGTH_SHORT).show();
                    } dialog.dismiss();
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull @NotNull DatabaseError error) {
                    dialog.dismiss();
                    Toast.makeText(Home.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(Home.this, "Item doesn't exists", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelled(DatabaseError error) {
        dialog.dismiss();
        Toast.makeText(Home.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
    }
   });
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBestDealItemClick(BestDealItemClick event)
    {
        if (event.getBestDeal() != null) {
            dialog.show();
            FirebaseDatabase.getInstance().getReference("Category")
                    .child(event.getBestDeal().getMenuId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Common.categorySelected = snapshot.getValue(Category.class);

                                FirebaseDatabase.getInstance().getReference("Category")
                                        .child(event.getBestDeal().getMenuId())
                                        .child("foods")
                                        .orderByChild("MenuId").equalTo(event.getBestDeal().getFoodId())
                                        .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@androidx.annotation.NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            for (DataSnapshot itemSnapshot:snapshot.getChildren())
                                                Common.selectedFood = itemSnapshot.getValue(Foods.class);
                                            navController.navigate(R.id.nav_food_details);
                                        } else {
                                            Toast.makeText(Home.this, "Item doesn't exists", Toast.LENGTH_SHORT).show();
                                        } dialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@androidx.annotation.NonNull @NotNull DatabaseError error) {
                                        dialog.dismiss();
                                        Toast.makeText(Home.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                Toast.makeText(Home.this, "Item doesn't exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            dialog.dismiss();
                            Toast.makeText(Home.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void OnHideFABEvent(HideFABCart event)
    {
        if (event.isHidden()) {
            fab.hide();
        } else fab.show();
    }

    private void countCartItem() {
        cartDataSource.countItemCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        fab.setCount(integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (!e.getMessage().contains("Query returned empty"))
                        Toast.makeText(Home.this, "[COUNT CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        else fab.setCount(0);
                    }
                });
    }

}