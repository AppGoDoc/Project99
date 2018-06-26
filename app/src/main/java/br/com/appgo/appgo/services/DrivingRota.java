package br.com.appgo.appgo.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import static br.com.appgo.appgo.view.MainActivity.LOCATION_RESOURCES;

public class DrivingRota extends Service {

    private IntentFilter intentFilter;
    private Intent serviceIntent;

    @Override
    public void onCreate() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(LOCATION_RESOURCES);
        serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        stopService(serviceIntent);
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent.getAction() == LOCATION_RESOURCES){
                AtualizaRota();
            }
        }
    };

    private void AtualizaRota() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
