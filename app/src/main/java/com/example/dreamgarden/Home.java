package com.example.dreamgarden;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.dreamgarden.EventBus.CategoryClick;
import com.example.dreamgarden.EventBus.FoodItemClick;
import com.example.dreamgarden.EventBus.GalleryClick;
import com.example.dreamgarden.EventBus.ImageClick;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_settings,
                R.id.nav_food_details, R.id.nav_food_list, R.id.nav_images, R.id.nav_image)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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
    public void onCategorySelected(CategoryClick event)
    {
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
//            Toast.makeText(this, "Click to "+event.getImages().getImage(), Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.nav_image);
        }
    }
}