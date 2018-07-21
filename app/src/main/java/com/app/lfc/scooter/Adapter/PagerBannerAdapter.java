package com.app.lfc.scooter.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.lfc.scooter.Model.Banner;
import com.app.lfc.scooter.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PagerBannerAdapter extends PagerAdapter {

    private Context context;
    private List<Banner> list;

    public PagerBannerAdapter(Context context, List<Banner> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_banner, null);
        ImageView img = view.findViewById(R.id.img_banner);
        Glide.with(context).load(list.get(position).getImg()).into(img);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
