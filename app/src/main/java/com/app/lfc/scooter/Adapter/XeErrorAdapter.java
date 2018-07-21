package com.app.lfc.scooter.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.lfc.scooter.Model.XeError;
import com.app.lfc.scooter.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class XeErrorAdapter extends RecyclerView.Adapter<XeErrorAdapter.ViewHolder> {

    private Context context;
    private List<XeError> xeErrorList;

    public XeErrorAdapter(Context context, List<XeError> xeErrorList) {
        this.context = context;
        this.xeErrorList = xeErrorList;
    }

    @NonNull
    @Override
    public XeErrorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xe_error, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final XeErrorAdapter.ViewHolder holder, int position) {
        final XeError xeError = xeErrorList.get(position);
        holder.txt.setText(xeError.getName());
        Glide.with(holder.itemView.getContext()).load(xeError.getImg()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.anim_click));
                AlertDialog.Builder dialog = new AlertDialog.Builder(context.getApplicationContext());
                dialog.setTitle("Nguyên nhân và khắc phục");
                dialog.setMessage(xeError.getKp());
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return xeErrorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_xe_error);
            txt = itemView.findViewById(R.id.txt_xe_error);
        }
    }
}
