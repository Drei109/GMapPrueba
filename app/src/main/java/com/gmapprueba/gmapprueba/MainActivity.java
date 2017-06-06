package com.gmapprueba.gmapprueba;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,GoogleMap.OnMapLongClickListener {

    private final LatLng coordenadasUPT = new LatLng(-18.0038755, -70.225904);
    private GoogleMap mMap;

    private EditText editDesde;
    private EditText editHasta;
    private Button btnTrazar;

    public MainActivity() {
    }


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

        editDesde = (EditText)findViewById(R.id.editDesde);
        editHasta = (EditText)findViewById(R.id.editHasta);
        btnTrazar = (Button)findViewById(R.id.btnTrazar);

        btnTrazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(editDesde.getText().toString().trim())){
                    Toast.makeText(MainActivity.this,"Ingresar coordenadas inciales",Toast.LENGTH_LONG).show();
                }else if("".equals(editHasta.getText().toString().trim())){
                    Toast.makeText(MainActivity.this,"Ingresar coordenadas finales",Toast.LENGTH_LONG).show();
                }else{
                    new RutaMapa(MainActivity.this,mMap,editDesde.getText().toString(),editHasta.getText().toString()).execute();
                }
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

    public class RutaMapa extends AsyncTask<Void,Integer,Boolean> {

        private static final String TOAST_MSG = "Calculando ...";
        private static final String TOAST_MSG_ERR = "No es posible Graficar esa ruta por el momento";

        private Context context;
        private GoogleMap gMap;
        private String editDesde;
        private String editHasta;
        private final ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();

        public RutaMapa(Context context, GoogleMap gMap, String editDesde, String editHasta) {
            this.context = context;
            this.gMap = gMap;
            this.editDesde = editDesde;
            this.editHasta = editHasta;
        }

        @Override
        protected void onPreExecute(){
            Toast.makeText(context,TOAST_MSG, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?sensor=false&language=pt");
                url.append("&origin=");
                url.append(editDesde.replace(' ','+'));
                url.append("&destination=");
                url.append(editHasta.replace(' ','+'));

                final InputStream stream = new URL(url.toString()).openStream();

                final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setIgnoringComments(true);

                final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                final Document document = documentBuilder.parse(stream);
                document.getDocumentElement().normalize();

                final String status = document.getElementsByTagName("status").item(0).getTextContent();

                if(!"OK".equals(status)){
                    return false;
                }

                final Element elementLeg = (Element)document.getElementsByTagName("leg").item(0);
                final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
                final int length = nodeListStep.getLength();

                for(int i=0; i<length;i++){
                    final Node nodeStep = nodeListStep.item(i);

                    if(nodeStep.getNodeType() == Node.ELEMENT_NODE){
                        final Element elementStep = (Element) nodeStep;
                        //Generar  Polyline
                        codificarPolyline(elementStep.getElementsByTagName("points").item(0).getTextContent());
                    }
                }

                return true;

            }catch (final Exception e) {
                return false;
            }
        }

        private void codificarPolyline(final String puntoCodificado){
            int index = 0;
            int lat=0,lng=0;

            while(index<puntoCodificado.length()){
                int b,shift=0,result=0;

                do{
                    b = puntoCodificado.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                }while(b >= 0x20);

                int dlat = ((result & 1)!=0? ~(result>>1):(result>>1));
                lat += dlat;
                shift = 0;
                result = 0;

                do{
                    b = puntoCodificado.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                }while (b >= 0x20);

                int dlng = ((result & 1)!=0? ~(result>>1):(result>>1));
                lng += dlng;

                lstLatLng.add(new LatLng((double)lat/1E5,(double)lng/1E5));
            }
        }

        @Override
        protected void onPostExecute(final Boolean result){
            if(!result) {
                Toast.makeText(context, TOAST_MSG_ERR, Toast.LENGTH_LONG).show();
            }else{
                final PolylineOptions polylines = new PolylineOptions();
                polylines.color(Color.GREEN);

                for(final LatLng latLng : lstLatLng){
                    polylines.add(latLng);
                }

                final MarkerOptions markerA = new MarkerOptions();
                markerA.position(lstLatLng.get(0));
                markerA.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                final MarkerOptions markerB = new MarkerOptions();
                markerB.position(lstLatLng.get(lstLatLng.size()-1));
                markerB.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lstLatLng.get(0),14));
                gMap.addMarker(markerA);
                gMap.addPolyline(polylines);
                gMap.addMarker(markerB);
            }
        }


    }
}
