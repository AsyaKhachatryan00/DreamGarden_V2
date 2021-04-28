package com.example.dreamgarden.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.example.dreamgarden.Models.BestDeal;
import com.example.dreamgarden.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BestDealsAdapter extends LoopingPagerAdapter<BestDeal> {


    @BindView(R.id.img_best_deal)
    ImageView img_best_deal;
    @BindView(R.id.best_deal)
    TextView best_deal;
    Unbinder unbinder;

    public BestDealsAdapter(Context context, List<BestDeal> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.beast_deal_item, container, false);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
       unbinder = ButterKnife.bind(this, convertView);
        Glide.with(convertView).load(itemList.get(listPosition).getImage()).into(img_best_deal);
        best_deal.setText(itemList.get(listPosition).getName());

    }
}
