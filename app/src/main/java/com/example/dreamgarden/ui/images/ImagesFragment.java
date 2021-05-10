package com.example.dreamgarden.ui.images;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.dreamgarden.Adapter.ImagesAdapter;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.Images;
import com.example.dreamgarden.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ImagesFragment extends Fragment {

    private ImagesViewModel mViewModel;

    Unbinder unbinder;
    @BindView(R.id.rec_images)
    RecyclerView recyclerViewImage;
    LayoutAnimationController layoutAnimationController;
    ImagesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        mViewModel = new  ViewModelProvider(this).get(ImagesViewModel.class);
        View root =  inflater.inflate(R.layout.fragment_images, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();

        mViewModel.getListMutableLiveData().observe(this, images -> {
            adapter = new ImagesAdapter(getContext(), images);
            recyclerViewImage.setAdapter(adapter);
            recyclerViewImage.setLayoutAnimation(layoutAnimationController);
        });
        return root;
    }

    private void initViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter != null) {
                    switch (adapter.getItemViewType(position)) {
                        case Common.DEFAULT_COLUMN_COUNT: return 1;
                        case Common.FULL_WIDTH_COLUMN : return 2;
                        default: return -1;
                    }
                }
                return -1;
            }
        });

        ((AppCompatActivity)getActivity())
                .getSupportActionBar()
                .setTitle(Common.gallerySelected.getName());

        recyclerViewImage.setLayoutManager(layoutManager);
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.item_from_left);

    }


}