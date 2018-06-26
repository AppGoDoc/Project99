package br.com.appgo.appgo.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.location.Location;
import android.os.Bundle;
import static br.com.appgo.appgo.view.MainActivity.LATITUDE_LOCATION;
import static br.com.appgo.appgo.view.MainActivity.LOCATION_RESOURCES;
import static br.com.appgo.appgo.view.MainActivity.LONGITUDE_LOCATION;

public class LocationService extends Service {

    public static final String LOCATION_BEARING = "location_bearing";
    private LocationManager mLocationManager = null;
        private static final int LOCATION_INTERVAL = 2000;
        private static final float LOCATION_DISTANCE = 10f;

        private class LocationListener implements android.location.LocationListener {
            Location mLastLocation;
            Float bearing;

            public LocationListener(String provider){
                mLastLocation = new Location(provider);
            }


            @Override
            public void onLocationChanged(Location location){
                if (location.getAccuracy()<15){
                    LocationDistance(location, mLastLocation);
                    mLastLocation.set(location);
                    bearing = location.getBearing();
                    LocationChange(location.getLatitude(), location.getLongitude(), bearing);
                }

            }

            @Override
            public void onProviderDisabled(String provider){
            }

            @Override
            public void onProviderEnabled(String provider){
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){
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
            super.onStartCommand(intent, flags, startId);
            return START_STICKY;
        }

        @Override
        public void onCreate() {
            initializeLocationManager();
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
            } catch (IllegalArgumentException ex) {
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
            } catch (IllegalArgumentException ex) {
            }

        }

        @Override
        public void onDestroy(){
            super.onDestroy();
            if (mLocationManager != null) {
                for (int i = 0; i < mLocationListeners.length; i++) {
                    try {
                        mLocationManager.removeUpdates(mLocationListeners[i]);
                    } catch (Exception ex) {
                    }
                }
            }
        }

        private void initializeLocationManager() {
            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }
        }
    private void LocationChange(double latitude, double longitude, float bearing) {
        try{
            Intent mIntent = new Intent();
            mIntent.setAction(LOCATION_RESOURCES);
            mIntent.putExtra(LATITUDE_LOCATION, latitude);
            mIntent.putExtra(LONGITUDE_LOCATION, longitude);
            mIntent.putExtra(LOCATION_BEARING, bearing);
            sendBroadcast(mIntent);
        }
        catch (Exception e){

        }
    }
    private void LocationDistance(Location location, Location mLasLocation){
        if (location != mLasLocation){
            LocationChange(location.getLatitude(), location.getLongitude(), location.getBearing());
        }
    }
}
