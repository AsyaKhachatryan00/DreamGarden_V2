package com.example.dreamgarden.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dreamgarden.Callback.IRecyclerClickListener;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.EventBus.ImageClick;
import com.example.dreamgarden.Models.Images;
import com.example.dreamgarden.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ImagesAdapter  extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private Context context;
    private List<Images> imagesList;

    public ImagesAdapter(Context context, List<Images> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.images_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(imagesList.get(position).getImage())
                .into(holder.image);

        holder.setListener((view, pos) -> {
            Common.selectedImage = imagesList.get(pos);
            EventBus.getDefault().postSticky(new ImageClick(true, imagesList.get(pos)));
        });

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Unbinder unbinder;
        @BindView(R.id.image)
        ImageView image;
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

    @Override
    public int getItemViewType(int position) {
        if (imagesList.size() == 1)
            return Common.DEFAULT_COLUMN_COUNT;
        else {
            if (imagesList.size() % 2 == 0 )
                return Common.DEFAULT_COLUMN_COUNT;
            else
                return (position > 1 && position == imagesList.size() - 1) ? Common.FULL_WIDTH_COLUMN:Common.DEFAULT_COLUMN_COUNT;
        }

    }

}
