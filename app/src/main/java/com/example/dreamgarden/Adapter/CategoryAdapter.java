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
import com.example.dreamgarden.EventBus.CategoryClick;
import com.example.dreamgarden.Models.Category;
import com.example.dreamgarden.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(categoryList.get(position).getImage())
                .into(holder.menu_image);
        holder.menu_name.setText(new StringBuilder(categoryList.get(position).getName()));

        holder.setListener((view, pos) -> {
            Common.categorySelected = categoryList.get(pos);
            EventBus.getDefault().postSticky(new CategoryClick(true, categoryList.get(pos)));

        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.menu_image)
        ImageView menu_image;
        @BindView(R.id.menu_name)
        TextView menu_name;

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

/*    @Override
    public int getItemViewType(int position) {
        if (categoryList.size() == 1)
            return Common.DEFAULT_COLUMN_COUNT;
        else {
            if (categoryList.size() % 2 == 0 )
                return Common.DEFAULT_COLUMN_COUNT;
            else
                return (position > 1 && position == categoryList.size() - 1) ? Common.FULL_WIDTH_COLUMN:Common.DEFAULT_COLUMN_COUNT;
        }

    }*/
}
