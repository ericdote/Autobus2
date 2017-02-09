package com.example.eric.autobus;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BroadcastReceiver broadcastReciver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!runtime_permissions()){
            enable_buttons();
        }
    }

    @Override
    public void onClick(View v) {
        EditText etUser = (EditText) findViewById(R.id.etUser);
        EditText etPass = (EditText) findViewById(R.id.etPass);
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (!(etUser.getText().toString().equals(""))) {
                    conexioBD(etUser, etPass);
                    Intent i = new Intent(getApplicationContext(), GeoLocalizacion.class);
                    startService(i);
                } else {
                    Toast.makeText(this, "Introdueix valors", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnStop:
                Intent i = new Intent(getApplicationContext(), GeoLocalizacion.class);
                stopService(i);
                break;
        }
    }

    public void conexioBD(EditText etUser, EditText etPass) {
        String user = etUser.getText().toString();
        String pass = etPass.getText().toString();
        SqlClass sql = new SqlClass(this, "Autobusus", null, 1);
        SQLiteDatabase db = sql.getWritableDatabase();
        if (db != null) {
            String[] args = new String[]{user, pass};
            Cursor c = db.rawQuery("SELECT * FROM tablaUsersInterna WHERE ? LIKE matricula AND ? LIKE password", args);
            if (c.moveToFirst()) {
                do {
                    Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
                } while (c.moveToNext());
            } else {
                Toast.makeText(this, "L'usuari no existeix", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            } else {
                runtime_permissions();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReciver != null){
            unregisterReceiver(broadcastReciver);
        }
    }

    private void enable_buttons() {
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnSubmit.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

}
