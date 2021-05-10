package com.example.dreamgarden.ui.table;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.dreamgarden.EventBus.HideFABCart;
import com.example.dreamgarden.Models.Tables;
import com.example.dreamgarden.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TableFragment extends Fragment {

    private TableViewModel mViewModel;
    private Unbinder unbinder;
    @BindView(R.id.table_full_image)
    ImageView image;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(TableViewModel.class);
        View view =  inflater.inflate(R.layout.table_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mViewModel.getMutableLiveData().observe(this, tables -> {
            displayTableImage(tables);
        });
        EventBus.getDefault().postSticky(new HideFABCart(true));
        return view;
    }

    private void displayTableImage(Tables tables) {
        Glide.with(getContext()).load(tables.getImage()).into(image);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().postSticky(new HideFABCart(false));
        super.onStop();
    }
}