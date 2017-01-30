package com.rumbaapp.LocationMaps;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.location.Location;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.rumbaapp.R;
import com.rumbaapp.activities.InformacionStios;
import com.rumbaapp.activities.MainActivity;
import com.rumbaapp.adaptadores.AdapterSitios;
import com.rumbaapp.dialogs.Dialogs;
import com.squareup.picasso.Picasso;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status> {

    private GoogleMap mapa;
    private View popup;
    int validador;
    private Double latitudSitio=1.0;
    private Double longitudSitio=1.0;
    private String urlImagen;
    private String nombreSitio;
    private String direccionSitio;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    LatLng latLng;
    private final int REQ_PERMISSION = 999;
    private static final String TAG = MainActivity.class.getSimpleName();
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;
    private Dialogs dialogs;
    private LocationManager locationManager;
    boolean isGPSEnabled = false;


    MapFragment mapFragment;


    // flag for network status



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latLng = new LatLng(latitudSitio,longitudSitio);

        createGoogleApi();

        Bundle b = getIntent().getExtras();
        validador = b.getInt("parametro");
        latitudSitio = b.getDouble("latitud");
        longitudSitio = b.getDouble("longitud");
        urlImagen = b.getString("imagen");
        nombreSitio = b.getString("nombre");
        direccionSitio = b.getString("direccion");

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }


    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapa = map;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapa.setMyLocationEnabled(true);


        mapa.setOnMarkerClickListener(this);
        mapa.setOnMapClickListener(this);
        mapa.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {


                if (validador == 1) {

                    if (popup == null) {
                        popup = getLayoutInflater().inflate(R.layout.popupmaps, null);
                    }

                    ImageView iv = (ImageView) popup.findViewById(R.id.iconmap);


                    TextView tv = (TextView) popup.findViewById(R.id.title_marker);

                    tv.setText(marker.getTitle());
                    tv = (TextView) popup.findViewById(R.id.snippetmap);
                    tv.setText(marker.getSnippet());

                    for (int i = 0; i <= MainActivity.arrayListMarkers.size() - 1; i++) {


                        if (marker.getTitle().equals(MainActivity.arrayListMarkers.get(i).getNombreSitio())) {
                            Picasso.with(getApplicationContext()).load(MainActivity.arrayListMarkers.get(i).getImagenSitio()).resize(100, 100).into(iv);
                        }
                    }

                } else if (validador == 0) {
                    if (popup == null) {
                        popup = getLayoutInflater().inflate(R.layout.popupmaps, null);
                    }

                    ImageView iv = (ImageView) popup.findViewById(R.id.iconmap);


                    TextView tv = (TextView) popup.findViewById(R.id.title_marker);

                    tv.setText(marker.getTitle());
                    tv = (TextView) popup.findViewById(R.id.snippetmap);
                    tv.setText(marker.getSnippet());
                    Picasso.with(getApplicationContext()).load(urlImagen).resize(100, 100).into(iv);

                }
                return (popup);

            }
        });

        if (validador == 1) {
            for (int i = 0; i <= MainActivity.arrayListMarkers.size() - 1; i++) {
                // Agregar marcadores
                final String nombre = MainActivity.arrayListMarkers.get(i).getNombreSitio();
                String latitud = MainActivity.arrayListMarkers.get(i).getLatitud();
                String longitud = MainActivity.arrayListMarkers.get(i).getLongitud();
                LatLng coorenadas = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));

                Log.e("++++++++++++++", "" + MainActivity.arrayListMarkers.get(i).getDireccionSitio());
                mapa.addMarker(new MarkerOptions().position(coorenadas).title(nombre).snippet(MainActivity.arrayListMarkers.get(i).getDireccionSitio()));
            }
        } else if (validador == 0) {

            LatLng coordenadas = new LatLng(latitudSitio, longitudSitio);
            mapa.addMarker(new MarkerOptions().position(coordenadas).title(nombreSitio).snippet(direccionSitio));

        }


        mapa.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        AdapterSitios.nombreSitio = marker.getTitle();
        Intent intent = new Intent(getApplicationContext(), InformacionStios.class);
        startActivity(intent);


    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());

                writeLastLocation();
                startLocationUpdates();
            } else {
                validateGps();
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    private boolean validateGps() {


        locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {

            dialogs = new Dialogs(this, "Ubicacion desactivada", "Por favor Activar Gps", "Activar", "", "", Settings.ACTION_LOCATION_SOURCE_SETTINGS, null);
            dialogs.showSettingsAlert(null);
            return false;
        } else {
            return true;
        }
    }



    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }


    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;
    // Start location Updates
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();

    }

    private Marker locationMarker;

    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user));
        if (mapa != null) {
            if (locationMarker != null)
                locationMarker.remove();
            locationMarker = mapa.addMarker(markerOptions);
            float zoom = 12f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mapa.animateCamera(cameraUpdate);
        }
    }

    private void writeActualLocation(Location location) {

        markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
        writeActualLocation(location);
        latitudSitio = location.getLatitude();
        longitudSitio = location.getLongitude();

        if (latLng.latitude != latitudSitio | latLng.longitude != longitudSitio) {


        }
    }
    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    @Override
        public void onMapClick (LatLng latLng){

        }

        @Override
        public boolean onMarkerClick (Marker marker){



            return false;
        }

        @Override
        public void onResult (Status status){

        }

}
