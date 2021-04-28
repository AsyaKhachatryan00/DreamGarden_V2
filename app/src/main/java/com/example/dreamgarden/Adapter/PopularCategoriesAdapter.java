package com.example.dreamgarden.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dreamgarden.Models.PopularCategory;
import com.example.dreamgarden.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class PopularCategoriesAdapter extends RecyclerView.Adapter<PopularCategoriesAdapter.MyViewHolder> {

    Context context;
    List<PopularCategory> popularCategories;

    public PopularCategoriesAdapter(Context context, List<PopularCategory> popularCategories) {
        this.context = context;
        this.popularCategories = popularCategories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.popular_categores, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(popularCategories.get(position).getImage())
                .into(holder.category_image);
        holder.category_name.setText(popularCategories.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return popularCategories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
    Unbinder unbinder;

    @BindView(R.id.category_name)
        TextView category_name;
    @BindView(R.id.category_image)
        CircleImageView category_image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
