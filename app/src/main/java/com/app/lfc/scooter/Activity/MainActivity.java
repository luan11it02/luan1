package com.app.lfc.scooter.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lfc.scooter.Adapter.PagerBannerAdapter;
import com.app.lfc.scooter.Model.Banner;
import com.app.lfc.scooter.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager pagerBanner;
    TextView txtNameUser, txtUser;
    Button btnHoTro, btnTuVan, btnTinTuc;
    CardView cardDiaDiem, cardLienHe;
    ImageButton btnMenu, btnSearch;
    DatabaseReference database;
    ArrayList<Banner> bannerArrayList;
    PagerBannerAdapter bannerAdapter;
    Handler handler;
    Runnable runnable;
    int currentItem;
    String nameUser;
    FirebaseAuth mAuth;
    List<AuthUI.IdpConfig> providers;
    private static final int RC_SIGN_IN = 123;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 345;
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 576;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mAuth = FirebaseAuth.getInstance();
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        btnMenu.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnHoTro.setOnClickListener(this);
        btnTuVan.setOnClickListener(this);
        btnTinTuc.setOnClickListener(this);
        cardDiaDiem.setOnClickListener(this);
        cardLienHe.setOnClickListener(this);
    }


    private void logIn(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_click));
        switch (v.getId()){
            case R.id.btn_menu_main:
                startActivity(new Intent(this, MenuActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.btn_search_main:
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.btn_hotro:
                startActivity(new Intent(this, HoTroActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.btn_tuvan:
                startActivity(new Intent(this, TuVanActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.btn_tintuc:
                startActivity(new Intent(this, PostActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.card_diadiem:
                startActivity(new Intent(this, MapsActivity.class));
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
                break;
            case R.id.card_lienhe:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:0866566655"));
                    startActivity(phoneIntent);
                }
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == RC_SIGN_IN) {
         //   IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user.getEmail() != null)
                    txtUser.setText(user.getEmail());
                else txtUser.setText(user.getPhoneNumber());

                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_input_name_user);
                final AppCompatEditText edtNameUser = dialog.findViewById(R.id.edt_name_user);
                Button btnNameUser = dialog.findViewById(R.id.btn_name_user);
                btnNameUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtNameUser.getText().toString().length() > 0) {
                            nameUser = edtNameUser.getText().toString();
                            txtNameUser.setText(nameUser);
                            SharedPreferences sharedPreferences = getSharedPreferences("NameUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Name", edtNameUser.getText().toString());
                            editor.apply();
                            dialog.dismiss();
                        }
                        else Toast.makeText(MainActivity.this, "Vui lòng nhập tên của bạn!!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isConnectionAvailable();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCallPhonePermission();
            checkLocationPermission();
        }
        getBanner();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            txtNameUser.setText("");
            txtUser.setText("");
            logIn();

        } else {
            if (currentUser.getEmail() != null && !currentUser.getEmail().equals(""))
                txtUser.setText(currentUser.getEmail());
            else txtUser.setText(currentUser.getPhoneNumber());
            SharedPreferences sharedPreferences = getSharedPreferences("NameUser", Context.MODE_PRIVATE);
            txtNameUser.setText(sharedPreferences.getString("Name", ""));
        }
    }

    public void getBanner(){
        bannerArrayList = new ArrayList<>();
        bannerAdapter = new PagerBannerAdapter(this, bannerArrayList);
        database = FirebaseDatabase.getInstance().getReference("banner");
        database.addValueEventListener(new ValueEventListener() {
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
        pagerBanner.setAdapter(bannerAdapter);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentItem = pagerBanner.getCurrentItem();
                currentItem ++;
                if (currentItem >= pagerBanner.getAdapter().getCount())
                    currentItem = 0;
                pagerBanner.setCurrentItem(currentItem, true);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean checkCallPhonePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
            return false;
        } else {
            return true;
        }
    }



    private boolean isConnectionAvailable() {

        boolean netCon = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ("WIFI".equals(networkInfo.getTypeName()) || "MOBILE".equals(networkInfo.getTypeName()) && networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting()) {
                netCon = true;
            }
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Network error!")
                    .setMessage("Không có kết nối Internet!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    }).show();
        }
        return netCon;
    }


    private void initView() {
        pagerBanner     = findViewById(R.id.viewpager_banner);
        txtNameUser     = findViewById(R.id.txt_name_user);
        txtUser         = findViewById(R.id.txt_user);
        btnHoTro        = findViewById(R.id.btn_hotro);
        btnTuVan        = findViewById(R.id.btn_tuvan);
        btnTinTuc       = findViewById(R.id.btn_tintuc);
        cardDiaDiem     = findViewById(R.id.card_diadiem);
        cardLienHe      = findViewById(R.id.card_lienhe);
        btnMenu         = findViewById(R.id.btn_menu_main);
        btnSearch       = findViewById(R.id.btn_search_main);
    }
}
