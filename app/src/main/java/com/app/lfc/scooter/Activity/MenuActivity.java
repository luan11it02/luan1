package com.app.lfc.scooter.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.lfc.scooter.Adapter.ContactAdapter;
import com.app.lfc.scooter.Adapter.FanpageAdapder;
import com.app.lfc.scooter.Model.Contact;
import com.app.lfc.scooter.Model.Fanpage;
import com.app.lfc.scooter.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btnBack;
    LinearLayout menuTuVan, menuTinTuc, menuDiaChi, menuPhone, menuFanpage, menuWebsite, menuQuanLy, menuDangXuat;
    DatabaseReference database;
    ArrayList<Fanpage> listFanpage;
    FanpageAdapder adapterFanpage;
    ArrayList<Contact> listContact;
    ContactAdapter adapterContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initView();
        btnBack.setOnClickListener(this);
        menuDangXuat.setOnClickListener(this);
        menuQuanLy.setOnClickListener(this);
        menuWebsite.setOnClickListener(this);
        menuFanpage.setOnClickListener(this);
        menuPhone.setOnClickListener(this);
        menuDiaChi.setOnClickListener(this);
        menuTinTuc.setOnClickListener(this);
        menuTuVan.setOnClickListener(this);
    }

    private void initView() {
        btnBack = findViewById(R.id.btn_back_menu);
        menuTuVan = findViewById(R.id.menu_tuvan);
        menuTinTuc = findViewById(R.id.menu_tintuc);
        menuDiaChi = findViewById(R.id.menu_diachi);
        menuPhone = findViewById(R.id.menu_phone);
        menuFanpage = findViewById(R.id.menu_fanpage);
        menuWebsite = findViewById(R.id.menu_website);
        menuQuanLy = findViewById(R.id.menu_quanly);
        menuDangXuat = findViewById(R.id.menu_dangxuat);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_click));
        final Dialog dialog;
        switch (v.getId()){
            case R.id.btn_back_menu:
                onBackPressed();
                overridePendingTransition(R.anim.intent_exit, R.anim.intent_enter);
                break;
            case R.id.menu_tuvan:
                startActivity(new Intent(this, TuVanActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.menu_tintuc:
                startActivity(new Intent(this, PostActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.menu_diachi:
                startActivity(new Intent(this, AddressActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.menu_phone:
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_contact);
                listContact = new ArrayList<>();
                RecyclerView recyclerContact = dialog.findViewById(R.id.recycler_contact);
                recyclerContact.setLayoutManager(new LinearLayoutManager(this));
                recyclerContact.setItemAnimator(new SlideInUpAnimator());
                adapterContact = new ContactAdapter(this, listContact);
                getDataContact();
                recyclerContact.setAdapter(adapterContact);
                dialog.show();
                break;
            case R.id.menu_fanpage:
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_fanpage);
                listFanpage = new ArrayList<>();
                RecyclerView recyclerFanpage = dialog.findViewById(R.id.recycler_fanpage);
                recyclerFanpage.setLayoutManager(new LinearLayoutManager(this));
                recyclerFanpage.setItemAnimator(new SlideInUpAnimator());
                adapterFanpage = new FanpageAdapder(this, listFanpage);
                getDataFanpage();
                recyclerFanpage.setAdapter(adapterFanpage);
                dialog.show();
                break;
            case R.id.menu_website:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://scootervietnam.vn/")));
                break;
            case R.id.menu_quanly:
                break;
            case R.id.menu_dangxuat:
                AuthUI.getInstance()
                        .signOut(getApplicationContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MenuActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    private void getDataContact(){
        database = FirebaseDatabase.getInstance().getReference("contact");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listContact.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    listContact.add(data.getValue(Contact.class));
                }
                adapterContact.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataFanpage(){
        database = FirebaseDatabase.getInstance().getReference("fanpage");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listFanpage.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    listFanpage.add(data.getValue(Fanpage.class));
                }
                adapterFanpage.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
