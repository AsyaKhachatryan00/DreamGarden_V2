package com.example.dreamgarden.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamgarden.Adapter.GalleryAdapter;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Common.SpaceItemDecoration;
import com.example.dreamgarden.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GalleryFragment extends Fragment {

    private GalleryViewHolder galleryViewHolder;

    Unbinder unbinder;
    @BindView(R.id.rec_gallery)
    RecyclerView rec_gallery;
    GalleryAdapter adapter;
    LayoutAnimationController layoutAnimationController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        galleryViewHolder = new ViewModelProvider(this).get(GalleryViewHolder.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        unbinder = ButterKnife.bind(this, root);
        initViews();

        galleryViewHolder.getMessageError().observe(this, s -> {
            Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
        });

        galleryViewHolder.getGalleryListMutable().observe(this, galleries -> {
            adapter = new GalleryAdapter(getContext(), galleries);
            rec_gallery.setAdapter(adapter);
            rec_gallery.setLayoutAnimation(layoutAnimationController);
        });

        return root;
    }

    private void initViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
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
        rec_gallery.setLayoutManager(layoutManager);
        rec_gallery.addItemDecoration(new SpaceItemDecoration(0));
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.item_from_left);

    }

}
