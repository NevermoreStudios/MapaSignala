package com.nevermore.mapasignala;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nevermore.mapasignala.server.APIClient;
import com.nevermore.mapasignala.server.ServerStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = MainActivity.class.getSimpleName();

    @AfterViews
    protected void init() {
        // System.out.println(PitajMeZaSignalStrength.getTSPName());
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
    }

}
