package com.nevermore.mapasignala;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nevermore.mapasignala.server.APIClient;
import com.nevermore.mapasignala.server.ServerStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = MainActivity.class.getSimpleName();

    @ViewById
    public TextView textView11;
    @ViewById
    public TextView textView22;
    @ViewById
    public TextView textView33;
    @ViewById
    public TextView textView44;
    @ViewById
    public Button button1;
    @ViewById
    public Button button2;

    public MainActivity m =this;

    @AfterViews
    protected void init() {
        APIClient client = new APIClient();
        client.api.status().enqueue(new Callback<ServerStatus>() {
            @Override
            public void onResponse(@NonNull Call<ServerStatus> call, @NonNull Response<ServerStatus> response) {
                ServerStatus status = response.body();
                if (status != null) {
                    Log.d(LOG_TAG,  "Version: " + status.version + ", MOTD: " + status.motd);
                } else {
                    Log.d(LOG_TAG, "Response was null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerStatus> call, @NonNull Throwable t) {
                Log.d(LOG_TAG, "Request failed");
                t.printStackTrace();
            }
        });
        PitajMeZaSignalStrength.init(this);
        startService(new Intent(this,ReportingService.class));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapActivity_.intent(m).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void update()
    {
        textView11.setText(PitajMeZaSignalStrength.getDbm()+" dBm");
        textView22.setText(PitajMeZaSignalStrength.getTSPName());
        textView33.setText(PitajMeZaSignalStrength.getStringType(PitajMeZaSignalStrength.getNetworkType())+" "+PitajMeZaSignalStrength.getGeneration(PitajMeZaSignalStrength.getNetworkType())+"G");
        if(PitajMeZaSignalStrength.loc != null){
            textView44.setText(PitajMeZaSignalStrength.loc.getLatitude()+" "+PitajMeZaSignalStrength.loc.getLongitude());
        }
    }

}
