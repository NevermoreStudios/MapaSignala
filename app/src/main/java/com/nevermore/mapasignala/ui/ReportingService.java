package com.nevermore.mapasignala.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.nevermore.mapasignala.db.AppDatabase;
import com.nevermore.mapasignala.db.Entry;
import com.nevermore.mapasignala.server.APIClient;
import com.nevermore.mapasignala.server.ResponseStatus;
import com.nevermore.mapasignala.server.SignalData;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("Registered")
@EService
public class ReportingService extends Service{

    @Pref
    protected Settings_ settings;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "db1").allowMainThreadQueries().build();
        final APIClient client = new APIClient();
        final Handler handler = new Handler();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            if (SignalStrength.loc == null) {
                                return;
                            }
                            db.entryDao().insertAll(new Entry(
                                SignalStrength.loc.getLatitude(),
                                SignalStrength.loc.getLongitude(),
                                SignalStrength.getDbm(),
                                SignalStrength.getNetworkType(),
                                SignalStrength.getTSP()
                            ));
                            if (isWifi() || settings.metered().get()) {
                                if (hasInternetConnection()) {
                                    final List<Entry> l = db.entryDao().getAll();
                                    ArrayList<SignalData> s = new ArrayList<>();
                                    for (Entry e : l) {
                                        s.add(e.getSignalData());
                                    }
                                    db.entryDao().delete(l.toArray(new Entry[0]));
                                    client.api.post(s).enqueue(new Callback<ResponseStatus>() {
                                        @Override
                                        public void onResponse(@NonNull Call<ResponseStatus> call, @NonNull Response<ResponseStatus> response) {
                                            ResponseStatus status = response.body();
                                            if (status == null || !status.success) {
                                                System.out.println("Simke je nesto sjebao mozda");
                                                db.entryDao().insertAll(l.toArray(new Entry[0]));
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<ResponseStatus> call, @NonNull Throwable t) {
                                            db.entryDao().insertAll(l.toArray(new Entry[0]));
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 0, Integer.parseInt(settings.interval().get()) * 1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean hasInternetConnection() {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private  boolean isWifi() {
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }
}
