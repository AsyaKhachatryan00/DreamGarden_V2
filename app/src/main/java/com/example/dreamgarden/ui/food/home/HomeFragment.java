package com.example.dreamgarden.ui.food.home;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dreamgarden.Adapter.CategoryAdapter;
import com.example.dreamgarden.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

private MenuViewHolder menuViewHolder;

Unbinder unbinder;
@BindView(R.id.rec_menu)
RecyclerView rec_menu;
CategoryAdapter adapter;
   LayoutAnimationController layoutAnimationController;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        menuViewHolder = new ViewModelProvider(this).get(MenuViewHolder.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, root);
        initViews();
        menuViewHolder.getMessageError().observe(this, s -> {
            Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
        });
        menuViewHolder.getCategoryListMutable().observe(this, categories -> {
            adapter = new CategoryAdapter(getContext(), categories);
            rec_menu.setAdapter(adapter);
            rec_menu.setLayoutAnimation(layoutAnimationController);

        });
        

        return root;
    }

    private void initViews() {

        rec_menu.setHasFixedSize(true);
        rec_menu.setLayoutManager(new LinearLayoutManager(getContext()));

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.item_from_left);

    }


}