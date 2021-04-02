package com.example.velogps;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView textGPS;
    TextView locationGPS;
    Button buttonGPS;

    LocationManager locationManagerGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Definir variables en pantalla */
        textGPS = (TextView) findViewById(R.id.GPS);
        locationGPS = (TextView) findViewById(R.id.locationGPS);
        buttonGPS = (Button) findViewById(R.id.ButtonGPS);

        getLocation();
    }

    /* Añadir menu en barra aplicación */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Añadir funciones al menu */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.about:
                Toast.makeText(this, "Desarrollado por Carlos Otero Bouzas", Toast.LENGTH_SHORT).show();
                //System.exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Generar localización */
    void getLocation() {
        try {
            locationManagerGPS = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //locationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,5,this);

            if (locationManagerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200,(float) 0.5, this);
            } else if (locationManagerGPS.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManagerGPS.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,200, (float) 0.5,this);
            }
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //textGPS.setText(location.toString());
        if (locationManagerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            textGPS.setText("GPS");
        } else if (locationManagerGPS.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
            textGPS.setText("RED");
        }
        //textGPS.setText(String.valueOf(locationManagerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        locationGPS.setText(String.valueOf((float) location.getLatitude() + "º N, " + (float) location.getLongitude() + "º E"));
        double speed = location.getSpeed() *3.6;
        buttonGPS.setText(String.format("%.1f", speed));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        textGPS.setText("Ubicación desactivada, pulsa aquí");

        textGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingIntent);
                return;
            }
        });

    }
    /* Reiniciar aplicación al volver atrás (Back Button) */
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}