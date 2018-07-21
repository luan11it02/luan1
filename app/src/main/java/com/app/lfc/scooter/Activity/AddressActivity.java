package com.app.lfc.scooter.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.lfc.scooter.Adapter.ChiNhanhAdapter;
import com.app.lfc.scooter.Model.ChiNhanh;
import com.app.lfc.scooter.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class AddressActivity extends AppCompatActivity {

    TextView txtAddress, txtHotline;
    RecyclerView recyclerMienDong, recyclerMienBac, recyclerMienTrung, recyclerMienNam;
    ChiNhanhAdapter adapter, adapterMienNam, adapterMienTrung, adapterMienBac;
    ArrayList<ChiNhanh> listMienDong, listMienBac, listMienTrung, listMienNam;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference database;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click));
                onBackPressed();
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
            }
        });
    }

    @Override
    protected void onStart() {
        getDataMain();
        getDataMienNam();
        getDataMienTrung();
        getDataMienBac();
        getDataMienDong();
        super.onStart();
    }

    private void getDataMain(){
        database = FirebaseDatabase.getInstance().getReference("diachi").child("main");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String adress = "";
                String hotline = "";
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    adress = data.getValue(ChiNhanh.class).getAds();
                    hotline = "Hotline: " + data.getValue(ChiNhanh.class).getHotline();
                }
                txtAddress.setText(adress);
                txtHotline.setText(hotline);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataMienNam(){
        listMienNam = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerMienNam.setLayoutManager(layoutManager);
        recyclerMienNam.setHasFixedSize(true);
        recyclerMienNam.setItemAnimator(new SlideInUpAnimator());
        adapterMienNam = new ChiNhanhAdapter(this, listMienNam);
        recyclerMienNam.setAdapter(adapterMienNam);
        database = FirebaseDatabase.getInstance().getReference("diachi").child("miennam");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMienNam.clear();
                for (DataSnapshot data: dataSnapshot.getChildren())
                    listMienNam.add(data.getValue(ChiNhanh.class));
                adapterMienNam.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataMienTrung(){
        listMienTrung = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerMienTrung.setLayoutManager(layoutManager);
        recyclerMienTrung.setHasFixedSize(true);
        recyclerMienTrung.setItemAnimator(new SlideInUpAnimator());
        adapterMienTrung = new ChiNhanhAdapter(this, listMienTrung);
        recyclerMienTrung.setAdapter(adapterMienTrung);
        database = FirebaseDatabase.getInstance().getReference("diachi").child("mientrung");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMienTrung.clear();
                for (DataSnapshot data: dataSnapshot.getChildren())
                    listMienTrung.add(data.getValue(ChiNhanh.class));
                adapterMienTrung.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataMienBac(){
        listMienBac = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerMienBac.setLayoutManager(layoutManager);
        recyclerMienBac.setHasFixedSize(true);
        recyclerMienBac.setItemAnimator(new SlideInUpAnimator());
        adapterMienBac = new ChiNhanhAdapter(this, listMienBac);
        recyclerMienBac.setAdapter(adapterMienBac);
        database = FirebaseDatabase.getInstance().getReference("diachi").child("mienbac");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMienBac.clear();
                for (DataSnapshot data: dataSnapshot.getChildren())
                    listMienBac.add(data.getValue(ChiNhanh.class));
                adapterMienBac.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataMienDong(){
        listMienDong = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerMienDong.setLayoutManager(layoutManager);
        recyclerMienDong.setHasFixedSize(true);
        recyclerMienDong.setItemAnimator(new SlideInUpAnimator());
        adapter = new ChiNhanhAdapter(this, listMienDong);
        recyclerMienDong.setAdapter(adapter);
        database = FirebaseDatabase.getInstance().getReference("diachi").child("miendong");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMienDong.clear();
                for (DataSnapshot data: dataSnapshot.getChildren())
                    listMienDong.add(data.getValue(ChiNhanh.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        btnBack             = findViewById(R.id.btn_back_adress);
        txtAddress          = findViewById(R.id.txt_ads_main);
        txtHotline          = findViewById(R.id.txt_hotline_main);
        recyclerMienDong    = findViewById(R.id.recycler_chinhanh_miendong);
        recyclerMienBac     = findViewById(R.id.recycler_chinhanh_mienbac);
        recyclerMienTrung   = findViewById(R.id.recycler_chinhanh_mientrung);
        recyclerMienNam     = findViewById(R.id.recycler_chinhanh_miennam);
    }

}
