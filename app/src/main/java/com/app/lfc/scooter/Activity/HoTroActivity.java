package com.app.lfc.scooter.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.lfc.scooter.R;

public class HoTroActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btnBack;
    AppCompatEditText edtName, edtEmailSDT, edtNoiDung;
    Button btnGui, btnQuayLai;
    Notification.Builder myNotification;
    int NOTIFICATION_ID = 282;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_tro);
        initView();
        btnBack.setOnClickListener(this);
        btnGui.setOnClickListener(this);
        btnQuayLai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_click));
        switch (v.getId()){
            case R.id.btn_back_hotro:
                this.finish();
                overridePendingTransition(R.anim.intent_exit, R.anim.intent_enter);
                break;
            case R.id.btn_gui_hotro:
                if (checkInfoInput()) {
                    pushNotification();
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Thông báo")
                            .setMessage("Gửi thành công!!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            }).show();
                }
                else new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Thông báo")
                        .setMessage("Vui lòng nhập đầy đủ thông tin yêu cầu!!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
                break;
            case R.id.btn_quaylai_hotro:
                this.finish();
                overridePendingTransition(R.anim.intent_exit, R.anim.intent_enter);
                break;
        }
    }

    private void pushNotification(){
        myNotification = new Notification.Builder(getApplicationContext());
        myNotification.setAutoCancel(true);
        myNotification.setSmallIcon(R.drawable.icon_scooter);
        myNotification.setContentTitle(edtName.getText().toString() + " - " + edtEmailSDT.getText().toString());
        myNotification.setContentText(edtNoiDung.getText().toString());
        myNotification.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo_scooter));

        NotificationManager notificationService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification =  myNotification.build();
        notificationService.notify(NOTIFICATION_ID, notification);
    }

    private boolean checkInfoInput(){
        if (edtName.getText().toString().length() > 0 && edtEmailSDT.getText().toString().length() > 0
                && edtNoiDung.getText().toString().length() > 0)
            return true;
        return false;
    }

    private void initView() {
        btnBack     = findViewById(R.id.btn_back_hotro);
        edtName     = findViewById(R.id.edt_name_hotro);
        edtEmailSDT = findViewById(R.id.edt_email_sdt_hotro);
        edtNoiDung  = findViewById(R.id.edt_noidung_hotro);
        btnGui      = findViewById(R.id.btn_gui_hotro);
        btnQuayLai  = findViewById(R.id.btn_quaylai_hotro);
    }


}
