package com.app.lfc.scooter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

public class XeFragment extends Fragment {

    View view;
    RadioGroup radioGroup;
    RadioButton rdHonda, rdSuzuki, rdYamaha, rdSYM, rdPiaggio;
    Spinner spinner;
    RecyclerView recycler;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    List<String> listSpinner;
    List<XeError> errorList;
    ArrayAdapter adapter;
    XeErrorAdapter adapterError;
    String xe = "";

    public XeFragment(){

    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_xe, container, false);
        initView();
        errorList   = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
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
        return view;
    }

    private void initView() {
        spinner     = view.findViewById(R.id.spinner);
        recycler    = view.findViewById(R.id.recyclerview_xe);
        rdHonda     = view.findViewById(R.id.honda);
        rdSuzuki    = view.findViewById(R.id.suzuki);
        rdYamaha    = view.findViewById(R.id.yamaha);
        rdSYM       = view.findViewById(R.id.sym);
        rdPiaggio   = view.findViewById(R.id.piaggio);
        radioGroup  = view.findViewById(R.id.radio_group);
    }

    public void getSpinner(String xe){
        databaseReference = FirebaseDatabase.getInstance().getReference("xe").child(xe);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSpinner = new ArrayList<>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Xe xe = data.getValue(Xe.class);
                    listSpinner.add(xe.getTen());
                }
                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, listSpinner);
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
                                adapterError = new XeErrorAdapter(getActivity(), errorList);
                                recycler.setAdapter(adapterError);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getActivity(), "!!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getActivity(), "NÔ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
