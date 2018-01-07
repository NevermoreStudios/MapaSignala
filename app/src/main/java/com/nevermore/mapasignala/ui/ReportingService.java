package com.nevermore.mapasignala.ui;

import android.app.IntentService;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nevermore.mapasignala.DbStuff.AppDatabase;
import com.nevermore.mapasignala.DbStuff.Entry;
import com.nevermore.mapasignala.R;
import com.nevermore.mapasignala.server.APIClient;
import com.nevermore.mapasignala.server.ResponseStatus;
import com.nevermore.mapasignala.server.ServerStatus;
import com.nevermore.mapasignala.server.SignalData;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.PreferenceChange;
import org.androidannotations.annotations.PreferenceScreen;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EService
public class ReportingService extends Service{

    //TODO Povuci Vreme
    long repval=5000;
    boolean overData = false;

    @Pref
    protected Settings_ settings;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate()
    {
        //repval=Long.parseLong(settings.interval().get())*1000;
        //overData=settings.metered().get();
        System.out.println("HAHAHA"+repval);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "db1").allowMainThreadQueries().build();
        final APIClient client = new APIClient();
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            //"Your function call  "
                            db.entyDao().insertAll(
                            new Entry(
                                    PitajMeZaSignalStrength.loc.getLatitude(),
                                    PitajMeZaSignalStrength.loc.getLongitude(),
                                    PitajMeZaSignalStrength.getDbm(),
                                    PitajMeZaSignalStrength.getNetworkType(),
                                    PitajMeZaSignalStrength.getTSP()
                                    ));
                            if(isWifi() || overData)
                            {
                                if(isInet())
                                {
                                    final List<Entry> l= db.entyDao().getAll();
                                    ArrayList<SignalData> s = new ArrayList<>();
                                    for(Entry e : l)
                                    {
                                        s.add(e.getSignalData());
                                    }
                                    db.entyDao().delete(l.toArray(new Entry[0]));
                                    client.api.post(s).enqueue(new Callback<ResponseStatus>() {
                                        @Override
                                        public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                                            System.out.println(response.body());

                                            System.out.println("Poslao"+l.size());
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseStatus> call, Throwable t) {
                                            db.entyDao().insertAll(l.toArray(new Entry[0]));
                                            System.out.println("Rolling Back");
                                            System.out.println("Nije Poslao Q je "+db.entyDao().getAll().size());
                                        }
                                    });
                                }else{System.out.println("Nije Poslao Q je "+db.entyDao().getAll().size());}
                            }else{System.out.println("Nije Poslao Q je "+db.entyDao().getAll().size());}

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, repval);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isInet()
    {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    private  boolean isWifi()
    {
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnected()) {
            return true;
        }else{return  false;}
    }
}
