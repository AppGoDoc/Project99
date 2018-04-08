package br.com.appgo.appgo.maps;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

public class Rota {
    GoogleMap googleMap;
    Context context;
    String googleMapsserverKey;
    Polyline polyline;
    List<Polyline> polylineList;
    boolean token;

    public Rota(GoogleMap googleMap, Context context, String googleMapsserverKey) {
        this.googleMap = googleMap;
        this.context = context;
        this.googleMapsserverKey = googleMapsserverKey;
        polylineList = new ArrayList<>();
        token = false;
    }

    public Polyline CreateRota(LatLng position, LatLng destination, String transportType){
        GoogleDirection.withServerKey(googleMapsserverKey)
                .from(position)
                .to(destination)
                .language(Language.PORTUGUESE_BRAZIL)
                .transportMode(transportType)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            // Do something
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
                            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(
                                    context, stepList, 5, Color.RED, 3, Color.BLUE);
                            for (PolylineOptions polylineOption : polylineOptionList) {
                                polylineList.add(googleMap.addPolyline(polylineOption));
                            }
                        } else {
                            Toast.makeText(context, "Impossivel criar rota", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(context, "fALHA NA DIRECTION", Toast.LENGTH_SHORT).show();
                    }
                });
        token = true;
        return polyline;
    }
    public void removePolyline(){
        for (Polyline polyline: polylineList){
            polyline.remove();
            token = false;
        }
    }
    public boolean ValidPolyline(){
        return token;
    }
}
