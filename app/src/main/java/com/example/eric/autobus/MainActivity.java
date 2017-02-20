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
/**
 * Activity Principal.
 * Lluis, en aquesta aplicacio, al igual que en la Servidor, hem utilitzat el metode antic per poder localitzar els autobuses
 * Sabem que esta deprecated, pero com ja t'ho vam comentar a clase t'ho tornem a dir per si de cas perque ho recordis el perque ho vam fer aixi.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BroadcastReceiver broadcastReciver;
    Button btnSubmit;


    /**
     * Iniciamos la activity y miramos si tenemos los permisos, en caso de que devuelva false activamos los botones
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!runtime_permissions()){
            enable_buttons();
        }
    }

    /**
     * Tenemos dos botones
     * btnStop que para el servicio de hacer inserts intos
     * btnSubmit que comprueba que no haya campos vacios, una vez comprobado esto mira si el usuario es correcto
     * Si es correcto lanza un Intent a la clase Service para iniciarla.
     * @param v
     */
    @Override
    public void onClick(View v) {
        EditText etUser = (EditText) findViewById(R.id.etUser);
        EditText etPass = (EditText) findViewById(R.id.etPass);
        boolean trobat;
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (!(etUser.getText().toString().equals(""))) {
                    trobat = conexioBD(etUser, etPass);
                    if(trobat){
                        Intent i = new Intent(getApplicationContext(), GeoLocalizacion.class);
                        i.putExtra("matricula", etUser.getText().toString());
                        startService(i);
                        btnSubmit.setEnabled(false);
                    } else {
                        Toast.makeText(this, "L'usuari no existeix", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Introdueix valors", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnStop:
                Intent i = new Intent(getApplicationContext(), GeoLocalizacion.class);
                stopService(i);
                btnSubmit.setEnabled(true);
                break;
        }
    }

    /**
     * Metodo que le llega el usuario y su password, comprueba si existe y devuelvo un boolean
     * true si exise el usuario false si no existe
     * @param etUser
     * @param etPass
     * @return
     */
    public boolean conexioBD(EditText etUser, EditText etPass) {
        String user = etUser.getText().toString();
        String pass = etPass.getText().toString();
        boolean trobat = false;
        SqlClass sql = new SqlClass(this, "Autobusus", null, 1);
        SQLiteDatabase db = sql.getWritableDatabase();
        if (db != null) {
            String[] args = new String[]{user, pass};
            Cursor c = db.rawQuery("SELECT * FROM tablaUsersInterna WHERE ? LIKE matricula AND ? LIKE password", args);
            if (c.moveToFirst()) {
                trobat = true;
            } else {
                trobat = false;
            }
        }
        return trobat;
    }

    /**
     * Metodo que comprueba que los permisos sean a igual a 100.
     * Si es asi llama al metodo que da funcionalidad a los botones
     * En caso contrario llama a un metodo para comprobar permisos.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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

    /**
     * Metodo que activa los botones
     */
    private void enable_buttons() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnSubmit.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    /**
     * Si tenemos los permisos envia true, si no false
     * @return
     */
    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

}
