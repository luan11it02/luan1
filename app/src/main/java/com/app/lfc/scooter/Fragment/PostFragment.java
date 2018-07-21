package com.app.lfc.scooter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.app.lfc.scooter.Adapter.PostAdapter;
import com.app.lfc.scooter.Model.Post;
import com.app.lfc.scooter.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class PostFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<Post> arrayList;
    PostAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("baiviet");

    public PostFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post, container, false);
        initView();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        arrayList   = new ArrayList<>();
        adapter     = new PostAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);
      //  getDataPost();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                   shuffleItems();
            }
        });
        return view;
    }

    private void initView() {
        recyclerView        = view.findViewById(R.id.recyclerview_post);
       // swipeRefreshLayout  = view.findViewById(R.id.refresh_post);
        arrayList           = new ArrayList<>();
    }

    public void shuffleItems() {
      //  Collections.shuffle(arrayList, new Random(System.currentTimeMillis()));
        adapter = new PostAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);
    }

    private void getDataPost(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (int i = (int) (dataSnapshot.getChildrenCount()-1); i >= 0; i--){
                    Post post = dataSnapshot.child(String.valueOf(i)).getValue(Post.class);
                    post.setId(i);
                    arrayList.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onStart() {
        getDataPost();
        super.onStart();
    }
}
