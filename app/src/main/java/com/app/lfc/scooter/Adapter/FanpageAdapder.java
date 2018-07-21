package com.app.lfc.scooter.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.app.lfc.scooter.Model.Fanpage;
import com.app.lfc.scooter.R;

import java.util.List;

public class FanpageAdapder extends RecyclerView.Adapter<FanpageAdapder.ViewHolder> {

    private Context context;
    private List<Fanpage> list;

    public FanpageAdapder(Context context, List<Fanpage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FanpageAdapder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fanpage, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FanpageAdapder.ViewHolder holder, int position) {
        final Fanpage fanpage = list.get(position);
        holder.txtNameFanpage.setText(fanpage.getName());
        holder.btnFanpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_click));
                Intent intentFB;
                try {
                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    intentFB = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + String.valueOf(fanpage.getId())));
                } catch (Exception e) {
                    intentFB = new Intent(Intent.ACTION_VIEW, Uri.parse(fanpage.getLink()));
                }
                context.startActivity(intentFB);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameFanpage;
        Button btnFanpage;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNameFanpage = itemView.findViewById(R.id.txt_name_fanpage);
            btnFanpage = itemView.findViewById(R.id.btn_fanpage);
        }
    }
}
