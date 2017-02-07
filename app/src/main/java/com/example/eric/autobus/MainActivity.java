package com.example.eric.autobus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnSubmit.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        EditText etUser = (EditText) findViewById(R.id.etUser);
        EditText etPass = (EditText) findViewById(R.id.etPass);
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (!(etUser.getText().toString().equals(""))) {
                    conexioBD(etUser, etPass);
                    startService(new Intent(MainActivity.this,
                            GeoLocalizacion.class));
                } else {
                    Toast.makeText(this, "Introdueix valors", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnStop:
                stopService(new Intent(MainActivity.this,
                        GeoLocalizacion.class));
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
}
