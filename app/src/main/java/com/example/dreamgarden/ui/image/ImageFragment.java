package com.example.dreamgarden.ui.image;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dreamgarden.Models.Images;
import com.example.dreamgarden.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ImageFragment extends Fragment {

    private ImageViewModel mViewModel;
    private Unbinder unbinder;
    @BindView(R.id.fullImage)
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        View root =  inflater.inflate(R.layout.image_fragment, container, false);
        unbinder = ButterKnife.bind(this, root );

        mViewModel.getMutableLiveData().observe(this, images -> {
            displayImage(images);
        });

        return root;
    }

    private void displayImage(Images images) {
        Glide.with(getContext()).load(images.getImage())
                .into(image);
    }

}