package br.com.appgo.appgo.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class MapLocation {
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
                            latLng = null;
                            try {
//                                setLocation(location);
                                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            if (latLng != null) {
                                setMyLocationMarke(googleMap);
                            }
                            else{
                                Toast.makeText(context, "Falha nas requisições do Google Maps, " +
                                        "cheque suas permissões para localização e internet.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    public void atualizarMapa(GoogleMap googleMap, LatLng myLocation, float bearing, float zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myLocation)
                .zoom(zoom)
                .bearing(bearing)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void atualizarMapa(GoogleMap googleMap, LatLng myLocation) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myLocation)
                .zoom(14.0f)
//                .bearing(bearing)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void setMyLocationMarke(GoogleMap googleMap) {
            googleMap.animateCamera(newLatLngZoom(latLng, 14.0f));
            new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(14)
//                    .bearing(90)
                    .build();
    }

    public void GoogleMapOptionsSettings(GoogleMap googleMap, int maptype) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setBuildingsEnabled(false);
        googleMap.setTrafficEnabled(false);
        googleMap.setMapType(maptype);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);


    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
