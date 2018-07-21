package com.app.lfc.scooter.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.app.lfc.scooter.Adapter.PostAdapter;
import com.app.lfc.scooter.Model.Post;
import com.app.lfc.scooter.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView recyclerPost;
    ArrayList<Post> listPost;
    PostAdapter adapterPost;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("baiviet");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnBack = findViewById(R.id.btn_back_post);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click));
                onBackPressed();
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
            }
        });

        recyclerPost       = findViewById(R.id.recycler_post);
        listPost           = new ArrayList<>();
        recyclerPost.setHasFixedSize(true);
        recyclerPost.setLayoutManager(new LinearLayoutManager(this));

        listPost   = new ArrayList<>();
        adapterPost     = new PostAdapter(this, listPost);
        recyclerPost.setAdapter(adapterPost);

    }

    @Override
    public void onStart() {
        getDataPost();
        super.onStart();
    }

    private void getDataPost(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPost.clear();
                for (int i = (int) (dataSnapshot.getChildrenCount()-1); i >= 0; i--){
                    Post post = dataSnapshot.child(String.valueOf(i)).getValue(Post.class);
                    post.setId(i);
                    listPost.add(post);
                }
                adapterPost.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
