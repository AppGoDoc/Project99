package br.com.appgo.appgo.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

public class CreateLatLngBounds {
    private LatLngBounds bounds;

    public CreateLatLngBounds(LatLng center, double radius) {
        bounds = new LatLngBounds.Builder().
                    include(SphericalUtil.computeOffset(center, radius, 0)).
                    include(SphericalUtil.computeOffset(center, radius, 90)).
                    include(SphericalUtil.computeOffset(center, radius, 180)).
                    include(SphericalUtil.computeOffset(center, radius, 270)).build();
    }

    public LatLngBounds getBounds(){
        return bounds;
    }

}
