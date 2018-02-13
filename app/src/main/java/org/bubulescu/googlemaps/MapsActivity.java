package org.bubulescu.googlemaps;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static LatLng LAT_LNG_ZG = new LatLng(45.815171, 15.945028);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(45, 15);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        configureMap();

        LatLng customAddress = getLatLngFromAddress("Ilica 242, Zagreb");

        addMarker(customAddress);
        animateCamera(customAddress);

        setupMapListeners();
    }

    private LatLng getLatLngFromAddress(String address) {

        LatLng latLng = LAT_LNG_ZG;

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> lokacije = geocoder.getFromLocationName(address, 1);
            Double lat = lokacije.get(0).getLatitude();
            Double lon = lokacije.get(0).getLongitude();
            latLng = new LatLng(lat, lon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return latLng;

    }

    private void setupMapListeners() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));
                String address = getAddressFromLatLng(latLng);
                Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAddressFromLatLng(LatLng latLng) {
        String address = "";

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> adrese = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address += adrese.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    private void animateCamera(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
    }

    private void addMarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("ALGEBRA Ilica"));
    }

    private void configureMap() {
        mMap.getUiSettings() .setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setTrafficEnabled(true);
    }
}
