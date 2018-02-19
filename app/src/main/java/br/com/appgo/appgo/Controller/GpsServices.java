package br.com.appgo.appgo.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.location.LocationRequest;

/**
 * Created by hex on 01/02/18.
 */

public class GpsServices {
    Context context;
    LocationManager locationManager;
    public GpsServices(Context context , LocationManager locationManager){
        this.locationManager = locationManager;
        this.context = context;
    }

    public boolean CheckLocationProvider(){
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    private void CreateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Serviços de localização estão desabilitados!");
        builder.setMessage("Por favor, habilite seu GPS");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public boolean GPSServicesRequirePermission(){
        if (!CheckLocationProvider()) CreateDialog();
        return CheckLocationProvider();
    }

}
