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
import com.example.dreamgarden.EventBus.TableClick;
import com.example.dreamgarden.Models.Tables;
import com.example.dreamgarden.R;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>{
    private Context context;
    private List<Tables> tablesList;

    public TableAdapter(Context context, List<Tables> tablesList) {
        this.context = context;
        this.tablesList = tablesList;
    }

    @NonNull
    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.table_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(tablesList.get(position).getImage())
                .into(holder.table);
        holder.location.setText(new StringBuilder(tablesList.get(position).getNumber()));
        holder.setListener((view, pos) -> {
            Common.selectedTable = tablesList.get(pos);
            EventBus.getDefault().postSticky(new TableClick(true, tablesList.get(pos)));
        });
    }

    @Override
    public int getItemCount() {
        return tablesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.table_image)
        ImageView table;
        @BindView(R.id.loc)
        TextView location;
        IRecyclerClickListener listener;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v, getAdapterPosition());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (tablesList.size() == 1)
            return Common.DEFAULT_COLUMN_COUNT;
        else {
            if (tablesList.size() % 2 == 0 )
                return Common.DEFAULT_COLUMN_COUNT;
            else
                return (position > 1 && position == tablesList.size() - 1) ? Common.FULL_WIDTH_COLUMN:Common.DEFAULT_COLUMN_COUNT;
        }

    }


}
