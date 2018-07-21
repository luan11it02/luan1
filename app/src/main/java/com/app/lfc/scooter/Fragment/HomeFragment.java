package com.app.lfc.scooter.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lfc.scooter.Activity.MainActivity;
import com.app.lfc.scooter.Activity.MapsActivity;
import com.app.lfc.scooter.Adapter.PagerBannerAdapter;
import com.app.lfc.scooter.Adapter.PostAdapter;
import com.app.lfc.scooter.Model.Banner;
import com.app.lfc.scooter.Model.ChiNhanh;
import com.app.lfc.scooter.Model.Post;
import com.app.lfc.scooter.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.LinePageIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;


public class HomeFragment extends Fragment implements View.OnClickListener{

    View view;
    TextView txtDistance;
    RecyclerView recyclerPost;
    ArrayList<Post> listPost;
    PostAdapter adapterPost;
    LinearLayoutManager layoutManager;
    Button btnXemthem, btnAdressHome, btnHotlineHome;
    ViewPager viewPagerBanner;
    LinePageIndicator indicator;
    DatabaseReference databaseReference;
    ArrayList<Banner> bannerArrayList;
    PagerBannerAdapter bannerAdapter;
    Handler handler;
    Runnable runnable;
    int currentItem;

    public HomeFragment() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();

        listPost = new ArrayList<>();
        adapterPost = new PostAdapter(getActivity(), listPost);
        recyclerPost.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerPost.setLayoutManager(layoutManager);
        recyclerPost.setItemAnimator(new ScaleInAnimator());
        recyclerPost.setAdapter(adapterPost);

        btnAdressHome.setOnClickListener(this);
        btnHotlineHome.setOnClickListener(this);
        btnXemthem.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        getBanner();
        getDataPost();
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_click));
        switch (v.getId()){
            case R.id.btn_address_home:
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hotline_home:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:0866566655"));
                    startActivity(phoneIntent);
                }
                break;
            case R.id.btn_xemthem:
              //  MainActivity.viewPager.setCurrentItem(2);
                break;
        }
    }

    private void initBaiViet() {
        listPost = new ArrayList<>();
        adapterPost = new PostAdapter(getActivity(), listPost);
        recyclerPost.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerPost.setLayoutManager(layoutManager);
        recyclerPost.setItemAnimator(new ScaleInAnimator());
        recyclerPost.setAdapter(adapterPost);
    }

    public void getBanner(){
        bannerArrayList = new ArrayList<>();
        bannerAdapter = new PagerBannerAdapter(getActivity(), bannerArrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference("banner");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bannerArrayList.clear();
                for (DataSnapshot data: dataSnapshot.getChildren())
                    bannerArrayList.add(data.getValue(Banner.class));
                bannerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        viewPagerBanner.setAdapter(bannerAdapter);
        indicator.setViewPager(viewPagerBanner);


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentItem = viewPagerBanner.getCurrentItem();
                currentItem ++;
                if (currentItem >= viewPagerBanner.getAdapter().getCount())
                    currentItem = 0;
                viewPagerBanner.setCurrentItem(currentItem, true);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    public void getDataPost(){
        databaseReference = FirebaseDatabase.getInstance().getReference("baiviet");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPost.clear();
                for (int i = (int) (dataSnapshot.getChildrenCount()-1); i >= (int) dataSnapshot.getChildrenCount()-2; i--){
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


    private void initView() {
        txtDistance     = view.findViewById(R.id.txt_distance);
        viewPagerBanner = view.findViewById(R.id.viewpager_banner);
        indicator       = view.findViewById(R.id.indicator_banner);
        recyclerPost    = view.findViewById(R.id.recycler_post_home);
        btnAdressHome   = view.findViewById(R.id.btn_address_home);
        btnHotlineHome  = view.findViewById(R.id.btn_hotline_home);
        btnXemthem      = view.findViewById(R.id.btn_xemthem);
    }


}

