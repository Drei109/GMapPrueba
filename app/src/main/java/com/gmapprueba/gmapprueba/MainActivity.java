package com.gmapprueba.gmapprueba;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,GoogleMap.OnMapLongClickListener {

    private final LatLng coordenadasUPT = new LatLng(-18.0038755, -70.225904);
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab_1 = (FloatingActionButton)findViewById(R.id.fab_1);
        FloatingActionButton fab_2 = (FloatingActionButton)findViewById(R.id.fab_2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
                if (fab_1.getVisibility() != View.VISIBLE) {
                    fab_1.setVisibility(View.VISIBLE);
                }else{
                    fab_1.setVisibility(View.INVISIBLE);
                }
                FloatingActionButton fab_2 = (FloatingActionButton) findViewById(R.id.fab_2);
                fab_2.setImageResource(R.drawable.logo_upt);
                if (fab_2.getVisibility() != View.VISIBLE) {
                    fab_2.setVisibility(View.VISIBLE);
                }else{
                    fab_2.setVisibility(View.INVISIBLE);
                }
            }
        });

        fab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
                FloatingActionButton fab_2 = (FloatingActionButton) findViewById(R.id.fab_2);
                if (fab_1.getVisibility() == View.VISIBLE) {
                    fab_1.setVisibility(View.INVISIBLE);
                    fab_2.setVisibility(View.INVISIBLE);
                }

                if (ActivityCompat.checkSelfPermission(getApplicationContext()
                        ,Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
                } else{
                    if (!mMap.isMyLocationEnabled()){
                        mMap.setMyLocationEnabled(true);
                    }

                    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if(myLocation==null){
                        Criteria criteria1 = new Criteria();
                        criteria1.setAccuracy(Criteria.ACCURACY_COARSE);
                        String provider = lm.getBestProvider(criteria1,true);
                        myLocation = lm.getLastKnownLocation(provider);
                    } else{
                        LatLng userLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation,14),1500,null);
                    }
                }
            }
        });

        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
                FloatingActionButton fab_2 = (FloatingActionButton) findViewById(R.id.fab_2);
                if (fab_2.getVisibility() == View.VISIBLE) {
                    fab_1.setVisibility(View.INVISIBLE);
                    fab_2.setVisibility(View.INVISIBLE);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordenadasUPT));

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadasUPT, 15));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);


        mMap.addMarker(new MarkerOptions().position(coordenadasUPT).title("Universidad Privada de Tacna"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordenadasUPT));

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

    }


    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Projection proj = mMap.getProjection();
        Point coord = proj.toScreenLocation(latLng);

        Toast.makeText(
                MainActivity.this,
                "Click Largo\n" +
                        "Lat: " + latLng.latitude + "\n" +
                        "Lng: " + latLng.longitude + "\n" +
                        "X: " + coord.x + " - Y: " + coord.y,
                Toast.LENGTH_SHORT).show();

    }
}
