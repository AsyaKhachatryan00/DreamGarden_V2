package com.example.dreamgarden.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dreamgarden.Callback.IRecyclerClickListener;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.EventBus.GalleryClick;
import com.example.dreamgarden.Models.Gallery;
import com.example.dreamgarden.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    Context context;
    List<Gallery> galleryList;

    public GalleryAdapter(Context context, List<Gallery> galleries) {
        this.context = context;
        this.galleryList = galleries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(galleryList.get(position).getImage())
                .into(holder.gallery_image);
        holder.gallery_name.setText(new StringBuilder(galleryList.get(position).getName()));

        holder.setListener((view, pos) -> {
            Common.gallerySelected = galleryList.get(pos);
            EventBus.getDefault().postSticky(new GalleryClick(true, galleryList.get(pos)));

        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Unbinder unbinder;
        @BindView(R.id.gallery_image)
        ImageView gallery_image;
        @BindView(R.id.gallery_name)
        TextView gallery_name;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }
        public ViewHolder(@NonNull View itemView) {
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

