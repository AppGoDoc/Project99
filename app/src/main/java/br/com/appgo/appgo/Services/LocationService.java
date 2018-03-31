package br.com.appgo.appgo.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.location.Location;
import android.os.Bundle;
import static br.com.appgo.appgo.View.MainActivity.LATITUDE_LOCATION;
import static br.com.appgo.appgo.View.MainActivity.LOCATION_RESOURCES;
import static br.com.appgo.appgo.View.MainActivity.LONGITUDE_LOCATION;

public class LocationService extends Service {

        private static final String TAG = "BOOMBOOMTESTGPS";
        private LocationManager mLocationManager = null;
        private static final int LOCATION_INTERVAL = 1000;
        private static final float LOCATION_DISTANCE = 10f;

        private class LocationListener implements android.location.LocationListener {
            Location mLastLocation;

            public LocationListener(String provider){
                Log.e(TAG, "LocationListener " + provider);
                mLastLocation = new Location(provider);
            }


            @Override
            public void onLocationChanged(Location location){
                Log.e(TAG, "onLocationChanged: " + location);
                if (location.getAccuracy()<15){
                    LocationDistance(location, mLastLocation);
                    mLastLocation.set(location);
                    LocationChange(location.getLatitude(), location.getLongitude());
                }

            }

            @Override
            public void onProviderDisabled(String provider){
                Log.e(TAG, "onProviderDisabled: " + provider);
            }

            @Override
            public void onProviderEnabled(String provider){
                Log.e(TAG, "onProviderEnabled: " + provider);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){
                Log.e(TAG, "onStatusChanged: " + provider);
            }
        }

        LocationListener[] mLocationListeners = new LocationListener[] {
                new LocationListener(LocationManager.GPS_PROVIDER),
                new LocationListener(LocationManager.NETWORK_PROVIDER)
        };

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
        return null;
    }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId){
            Log.e(TAG, "onStartCommand");
            super.onStartCommand(intent, flags, startId);
            return START_STICKY;
        }

        @Override
        public void onCreate() {
            Log.e(TAG, "onCreate");
            initializeLocationManager();
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }

        }

        @Override
        public void onDestroy(){
            Log.e(TAG, "onDestroy");
            super.onDestroy();
            if (mLocationManager != null) {
                for (int i = 0; i < mLocationListeners.length; i++) {
                    try {
                        mLocationManager.removeUpdates(mLocationListeners[i]);
                    } catch (Exception ex) {
                        Log.i(TAG, "fail to remove location listners, ignore", ex);
                    }
                }
            }
        }

        private void initializeLocationManager() {
            Log.e(TAG, "initializeLocationManager");
            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }
        }
    private void LocationChange(double latitude, double longitude) {
        try{
            Intent mIntent = new Intent();
            mIntent.setAction(LOCATION_RESOURCES);
            mIntent.putExtra(LATITUDE_LOCATION, latitude);
            mIntent.putExtra(LONGITUDE_LOCATION, longitude);
            sendBroadcast(mIntent);
        }
        catch (Exception e){

        }
    }
    private void LocationDistance(Location location, Location mLasLocation){
        if (location != mLasLocation){
            LocationChange(location.getLatitude(), location.getLongitude());
        }
    }
}
