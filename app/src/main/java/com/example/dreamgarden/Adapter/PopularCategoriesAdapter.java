package com.example.dreamgarden.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dreamgarden.Callback.IRecyclerClickListener;
import com.example.dreamgarden.EventBus.PopularCategoryClick;
import com.example.dreamgarden.Models.PopularCategory;
import com.example.dreamgarden.R;

import org.greenrobot.eventbus.EventBus;

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

        holder.setListener((view, pos) ->
        EventBus.getDefault().postSticky(new PopularCategoryClick(popularCategories.get(pos))));

    }

    @Override
    public int getItemCount() {
        return popularCategories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    Unbinder unbinder;

    @BindView(R.id.category_name)
        TextView category_name;
    @BindView(R.id.category_image)
        CircleImageView category_image;

    IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
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
