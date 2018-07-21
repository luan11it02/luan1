package com.app.lfc.scooter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lfc.scooter.Activity.WebActivity;
import com.app.lfc.scooter.Model.Post;
import com.app.lfc.scooter.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements Filterable{
    private Context context;
    private List<Post> list;
    private List<Post> listFiltered;
    private DatabaseReference databaseReference;

    public PostAdapter(Context context, List<Post> list) {
        this.context = context;
        this.list = list;
        this.listFiltered = list;
    }


    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, null);


        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Post post = list.get(position);
        holder.txtTitle.setText(post.getTitle());
        Glide.with(holder.itemView.getContext()).load(post.getImg()).into(holder.img);
        String date = "Ngày đăng: " + post.getDate();
        String view = "Lượt xem: " + post.getView();
        holder.txtDate.setText(date);
        holder.txtView.setText(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_click));
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("link", list.get(position).getLink());
                context.startActivity(intent);
                databaseReference = FirebaseDatabase.getInstance().getReference("baiviet").child(String.valueOf(post.getId())).child("view");
                databaseReference.setValue(post.getView() + 1);
            }
        });
    }
    public void setfilter(List<Post> listFilter){
        list = new ArrayList<>();
        list.addAll(listFilter);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    private static String covertStringToURL(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<Post> filteredList = new ArrayList<>();
                    for (Post row : list) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getTitle().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtDate, txtView;
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title_post);
            txtDate = itemView.findViewById(R.id.txt_date_post);
            txtView = itemView.findViewById(R.id.txt_view_post);
            img = itemView.findViewById(R.id.img_post);
        }
    }
}

