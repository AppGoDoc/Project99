package br.com.appgo.appgo.Model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by hex on 30/01/18.
 */

public class TesteMarker {
    private GoogleMap googleMap;
    public TesteMarker(GoogleMap googleMap){ this.googleMap = googleMap; }

    public void testeMarks() {
        LatLng latLng = new LatLng(-23.938443, -46.372327);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("Jardim Botânico"));
        LatLng latLng1 = new LatLng(-23.945212, -46.368845);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng1)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("Supermercado Extra"));
        LatLng latLng2 = new LatLng(-23.942604, -46.371774);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng2)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("Praça Domingos Alcino"));
        LatLng latLng3 = new LatLng(-23.941062, -46.379349);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng3)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("Casa Rubens"));
    }
}
