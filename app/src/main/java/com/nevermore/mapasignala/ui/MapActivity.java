package com.nevermore.mapasignala.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nevermore.mapasignala.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_map)
public class MapActivity extends AppCompatActivity {

    @ViewById
    public Spinner provider;
    @ViewById
    public Spinner generation;

    @AfterViews
    protected void init() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap map) {
                LatLng lat = new LatLng(SignalStrength.loc.getLatitude(), SignalStrength.loc.getLongitude());
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 13));
                map.addMarker(
                    new MarkerOptions()
                        .title("Sydney")
                        .snippet("The most populous city in Australia.")
                        .position(lat)
                );
            }
        });
    }
}
