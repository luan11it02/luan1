package com.app.lfc.scooter.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.lfc.scooter.Model.ChiNhanh;
import com.app.lfc.scooter.R;

import java.util.List;

public class ChiNhanhAdapter extends RecyclerView.Adapter<ChiNhanhAdapter.ViewHolder> {

    private Context context;
    private List<ChiNhanh> list;

    public ChiNhanhAdapter(Context context, List<ChiNhanh> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChiNhanhAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_chinhanh, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiNhanhAdapter.ViewHolder holder, int position) {
        ChiNhanh chiNhanh = list.get(position);
        holder.txtAds.setText(chiNhanh.getAds());
        String hotline = "Hotline:  " + chiNhanh.getHotline();
        holder.txtHotline.setText(hotline);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAds, txtHotline;

        ViewHolder(View itemView) {
            super(itemView);

            txtAds      = itemView.findViewById(R.id.txt_ads_chinhanh);
            txtHotline  = itemView.findViewById(R.id.txt_hotline_chinhanh);

        }
    }
}
