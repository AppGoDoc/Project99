package br.com.appgo.appgo.Controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzq;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.Serializable;
import br.com.appgo.appgo.Model.TesteMarker;
import br.com.appgo.appgo.R;

import static com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class MapLocation{
    Context context;
    LatLng latLng;
    FusedLocationProviderClient mFusedLocationClient;
    Location location;
    GoogleApiClient googleApiClient;
    GoogleMap googleMap;
    Marker marker;

    public MapLocation(Context context, GoogleMap googleMap, GoogleApiClient client) {
        this.context = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.googleApiClient = client;
        this.googleMap = googleMap;
    }

    public void getLastLocation(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            setLocation(location);
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            if (location != null) {
                                atualizarMapa(googleMap);
                            }
                        }
                    });
        }
    }
    public void atualizarMapa(GoogleMap googleMap){
        googleMap.animateCamera(newLatLngZoom(latLng,17.0f));
        googleMap.clear();
        marker = googleMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title("Minha Posição")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.imagemarker)));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17)
                .bearing(90)
                .build();
        TesteMarker testeMarker = new TesteMarker(googleMap);
        testeMarker.testeMarks();
    }
    public void setMyLocationMarke(GoogleMap googleMap, LatLng latLng){
        marker.setPosition(latLng);
        googleMap.animateCamera(newLatLngZoom(latLng,17.0f));
        Log.e("Minha lOcalizacao", "foi alterada");
        }

    public FusedLocationProviderClient getmFusedLocationClient() {
            return mFusedLocationClient;
        }

    public void GoogleMapOptionsSettings(GoogleMap googleMap, int maptype){
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMapType(maptype);
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setmFusedLocationClient(FusedLocationProviderClient mFusedLocationClient) {
        this.mFusedLocationClient = mFusedLocationClient;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public void setAnimateCameraMapBound(GoogleMap googleMap, LatLngBounds area){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(area, 50));
    }
    public void setAnimateCameraMapZoom(GoogleMap googleMap, LatLng latLng){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
    }
    public void MapClear(GoogleMap googleMap){
        googleMap.clear();
    }
}
