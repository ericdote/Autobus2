package com.example.eric.autobus;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class GeoLocalizacion extends Service implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient apiClient;
    private double latitud, longitud;

    public GeoLocalizacion() {
    }

    //Servicios

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Servicio Creado", Toast.LENGTH_SHORT).show();
        apiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Iniciando servicio", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "TO QUIETOh", Toast.LENGTH_SHORT).show();
    }

    //Localizacion

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "gtrefds5gtr", Toast.LENGTH_SHORT).show();
        //Conectado correctamente a Google Play Services
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Activa el permisos per l'aplicacio", Toast.LENGTH_SHORT).show();
        } else {
            Location loc = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            Toast.makeText(this, "holaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
            updateUI(loc);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services
        Toast.makeText(this, "Suspendido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
   }

    private void updateUI(Location loc) {
        if (loc != null) {
            latitud = loc.getLongitude();
            longitud = loc.getLongitude();
            Toast.makeText(this, latitud+"  "+longitud, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Longitud i Latitud: (desconeguda)", Toast.LENGTH_SHORT).show();
        }
    }
}
