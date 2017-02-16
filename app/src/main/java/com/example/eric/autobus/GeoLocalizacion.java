package com.example.eric.autobus;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class GeoLocalizacion extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    String matricula;
    double longitut, latitud;
    String date;

    public GeoLocalizacion() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @SuppressWarnings("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ConexionWebService con = new ConexionWebService();
                latitud = location.getLatitude();
                longitut = location.getLongitude();

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                date = df.format(today);

                con.execute();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, listener);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Iniciando servicio", Toast.LENGTH_SHORT).show();
        matricula = intent.getStringExtra("matricula");
        Toast.makeText(this, ""+matricula, Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }


    @SuppressWarnings("MissingPermission")
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(listener);
        }
    }


    private class ConexionWebService extends AsyncTask<Void, Void, Boolean> {

        public ConexionWebService() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://192.168.120.81:8080/WebClientRest/webresources/generic");
            post.setHeader("content-type", "application/json");
            try {

                JSONObject ubicacion = new JSONObject();

                ubicacion.put("matricula",matricula);
                ubicacion.put("latitud", latitud);
                ubicacion.put("longitud", longitut);
                ubicacion.put("data", date);
                StringEntity entity = new StringEntity(ubicacion.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if (!respStr.equals("true")) {
                    resul = true;
                }


            } catch (Exception e) {
                Log.e("ServicioRest", "Error!", e);
                resul = false;
            }
            return resul;
        }


        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(GeoLocalizacion.this, "Insertado OK", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GeoLocalizacion.this, "No insertado", Toast.LENGTH_SHORT).show();
            }
        }


    }


}
