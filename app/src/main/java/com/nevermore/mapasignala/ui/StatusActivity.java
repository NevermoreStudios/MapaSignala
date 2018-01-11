package com.nevermore.mapasignala.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nevermore.mapasignala.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class StatusActivity extends AppCompatActivity {
    @ViewById protected TextView strength;
    @ViewById protected TextView provider;
    @ViewById protected TextView type;
    @ViewById protected TextView location;

    @AfterViews
    protected void init() {
        SignalStrength.setActivity(this);
        update();
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
