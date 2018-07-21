package com.app.lfc.scooter.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.app.lfc.scooter.Adapter.XeErrorAdapter;
import com.app.lfc.scooter.Model.Xe;
import com.app.lfc.scooter.Model.XeError;
import com.app.lfc.scooter.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TuVanActivity extends AppCompatActivity {

    ImageButton btnBack;
    RadioGroup radioGroup;
    RadioButton rdHonda, rdSuzuki, rdYamaha, rdSYM, rdPiaggio;
    Spinner spinner;
    RecyclerView recycler;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference database;
    List<String> listSpinner;
    List<XeError> errorList;
    ArrayAdapter adapter;
    XeErrorAdapter adapterError;
    String xe = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_van);

        initView();

        errorList   = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.honda: xe = "Honda";
                        break;
                    case R.id.suzuki: xe = "Suzuki";
                        break;
                    case R.id.yamaha: xe = "Yamaha";
                        break;
                    case R.id.sym: xe = "SYM";
                        break;
                    case R.id.piaggio: xe = "Piaggio";
                        break;
                }
                getSpinner(xe);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click));
                onBackPressed();
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
            }
        });
    }

    public void getSpinner(String xe){
        database = FirebaseDatabase.getInstance().getReference("xe").child(xe);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSpinner = new ArrayList<>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Xe xe = data.getValue(Xe.class);
                    listSpinner.add(xe.getTen());
                }
                adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("loi").child(listSpinner.get(position));
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                errorList = new ArrayList<>();
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    XeError xeError = data.getValue(XeError.class);
                                    errorList.add(xeError);
                                }
                                adapterError = new XeErrorAdapter(getApplicationContext(), errorList);
                                recycler.setAdapter(adapterError);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void initView() {
        btnBack     = findViewById(R.id.btn_back_tuvan);
        spinner     = findViewById(R.id.spinner);
        recycler    = findViewById(R.id.recycler_tuvan);
        rdHonda     = findViewById(R.id.honda);
        rdSuzuki    = findViewById(R.id.suzuki);
        rdYamaha    = findViewById(R.id.yamaha);
        rdSYM       = findViewById(R.id.sym);
        rdPiaggio   = findViewById(R.id.piaggio);
        radioGroup  = findViewById(R.id.radio_group);
    }
}
