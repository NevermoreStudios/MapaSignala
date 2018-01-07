package com.nevermore.mapasignala.ui;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nevermore.mapasignala.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewById
    public TextView strength;
    @ViewById
    public TextView provider;
    @ViewById
    public TextView type;
    @ViewById
    public TextView location;

    @AfterViews
    protected void init() {
        SignalStrength.init(this);
        ReportingService_.intent(this).start();
    }

    @Click
    protected void mapButton() {
        MapActivity_.intent(this).start();
    }

    @Click
    protected void settingsButton() {
        SettingsActivity_.intent(this).start();
    }

    public void update() {
        strength.setText(getString(R.string.strength_format, SignalStrength.getDbm()));
        provider.setText(SignalStrength.getTSPName());
        int ntype = SignalStrength.getNetworkType();
        type.setText(getString(R.string.type_format, SignalStrength.getStringType(ntype), SignalStrength.getGeneration(ntype)));
        if (SignalStrength.loc != null) {
            location.setText(getString(R.string.location_format, SignalStrength.loc.getLatitude(), SignalStrength.loc.getLongitude()));
        }
    }


}
