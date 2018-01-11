package com.nevermore.mapasignala.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.nevermore.mapasignala.R;
import com.nevermore.mapasignala.server.APIClient;
import com.nevermore.mapasignala.server.SignalInfo;
import com.nevermore.mapasignala.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("Registered")
@EActivity(R.layout.activity_map)
@OptionsMenu(R.menu.map)
public class MapActivity extends AppCompatActivity {

    @ViewById protected Spinner provider;
    @ViewById protected Spinner generation;
    private TileProvider mts = new TileProvider(3);
    private TileProvider telenor = new TileProvider(1);
    private TileProvider vip = new TileProvider(5);
    private TileOverlay TOM, TOT, TOV;
    private AlertDialog d, help;

    @AfterViews
    protected void init() {
        if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                Constants.LOCATION_PERMISSION
            );
        } else {
            done();
        }
    }

    private void done() {
        SignalStrength.init(this);
        ReportingService_.intent(this).start();
        d = new AlertDialog.Builder(this)
            .setTitle(R.string.signal_properties)
            .setView(R.layout.prop)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            })
            .create();
        d.show();
        d.dismiss();
        help = new AlertDialog.Builder(this)
            .setTitle(R.string.help)
            .setMessage(R.string.help_text)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            })
            .create();
        final APIClient client = new APIClient();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(final GoogleMap map) {
                if (SignalStrength.loc == null) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.8069457,20.4547698), 17));
                } else {
                    // Startit Centar :)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(SignalStrength.loc.getLatitude(),SignalStrength.loc.getLongitude()), 17));
                }
                map.setMyLocationEnabled(true);
                map.setMaxZoomPreference(17);
                map.setMinZoomPreference(5);
                TOT = map.addTileOverlay(new TileOverlayOptions().transparency(1f).tileProvider(telenor));
                TOV = map.addTileOverlay(new TileOverlayOptions().transparency(1f).tileProvider(vip));
                TOM = map.addTileOverlay(new TileOverlayOptions().transparency(1f).tileProvider(mts));
                getTO().setTransparency(0.5f);
                provider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        TOT.setTransparency(1f);
                        TOM.setTransparency(1f);
                        TOV.setTransparency(1f);
                        try {
                            switch (i) {
                                case 0:
                                    getTO().setTransparency(0.5f);
                                    break;
                                case 1:
                                    TOM.setTransparency(0.5f);
                                    TOV.setTransparency(0.5f);
                                    TOT.setTransparency(0.5f);
                                    break;
                                case 2:
                                    TOM.setTransparency(0.5f);
                                    break;
                                case 3:
                                    TOV.setTransparency(0.5f);
                                    break;
                                case 4:
                                    TOT.setTransparency(0.5f);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });

                map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        final ProgressDialog progress = ProgressDialog.show(MapActivity.this, getString(R.string.loading), getString(R.string.please_wait), true);
                        client.api.signalAll(latLng.latitude,latLng.longitude).enqueue(new Callback<List<List<SignalInfo>>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<List<SignalInfo>>> call, @NonNull Response<List<List<SignalInfo>>> response) {
                                progress.dismiss();
                                try {
                                    clearD();
                                    printMsg(response.body());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                d.show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<List<SignalInfo>>> call, @NonNull Throwable t) {
                                progress.dismiss();
                                Toast toast = Toast.makeText(MapActivity.this, R.string.error, Toast.LENGTH_SHORT);
                                toast.show();
                                t.printStackTrace();
                            }
                        });
                    }
                });

            }
        });
    }

    private TileOverlay getTO() {
        switch (SignalStrength.getTSP()) {
            case 22001: return TOT;
            case 22003: return TOM;
            case 22005: return TOV;
        }
        return TOT;
    }

    private void clearD() {
        for (String s1 : new String[]{ "M", "V", "T" }) {
            for (String s2 : new String[]{ "2", "3", "4" }) {
                setDialog(s1 + s2, "N/A", "N/A", "N/A");
            }
        }
    }

    private void printMsg(List<List<SignalInfo>> body) {
        int size = body.size();
        if (size > 0 && body.get(0) != null) {
            printProv(body.get(0), "T");
        }
        if (size > 2 && body.get(2) != null) {
            printProv(body.get(2), "M");
        }
        if (size > 4 && body.get(4) != null) {
            printProv(body.get(4), "V");
        }
    }

    private void printProv(List<SignalInfo> si, String s) {
        int size = si.size();
        if (size > 1 && si.get(1) != null) {
            printGen(si.get(1), s + "2");
        }
        if (size > 2 && si.get(2) != null) {
            printGen(si.get(2), s + "3");
        }
        if (size > 3 && si.get(3) != null) {
            printGen(si.get(3), s + "4");
        }
    }

    private void setDialog(String s, String minV, String avgV, String maxV) {
        Resources r = getResources();
        TextView min = d.findViewById(r.getIdentifier(s + "Mi", "id", getPackageName()));
        TextView avg = d.findViewById(r.getIdentifier(s + "Av", "id", getPackageName()));
        TextView max = d.findViewById(r.getIdentifier(s + "Mx", "id", getPackageName()));
        if (min != null) {
            min.setText(minV);
        }
        if (avg != null) {
            avg.setText(avgV);
        }
        if (max != null) {
            max.setText(maxV);
        }
    }

    private void printGen(SignalInfo si, String s) {
        // Minimum was unavailable for some reason
        String min = String.valueOf(si.min);
        if (min.equals("1")) {
            min = "N/A";
        }
        // Maximum was unavailable for some reason
        String max = String.valueOf(si.max);
        if (max.equals("1")) {
            max = "N/A";
        }
        // Average was unavailable for some reason
        String avg = String.valueOf((int) si.avg);
        if (max.equals("0")) {
            avg = "N/A";
        }
        setDialog(s, min, avg, max);
    }

    @OptionsItem
    protected void helpSelected() {
        help.show();
    }

    @OptionsItem
    protected void settingsSelected() {
        SettingsActivity_.intent(this).start();
    }

    @OptionsItem
    protected void statusSelected() {
        StatusActivity_.intent(this).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (
            requestCode == Constants.LOCATION_PERMISSION &&
            grantResults.length > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            done();
        } else {
            this.finish();
        }
    }


}
