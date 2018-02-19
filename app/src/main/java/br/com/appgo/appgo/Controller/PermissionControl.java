package br.com.appgo.appgo.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by hex on 28/01/18.
 */

public class PermissionControl{
    private Context context;
    private Activity activity;
    public PermissionControl(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void askPermission(){
        ActivityCompat.requestPermissions(this.activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.
                                            ACCESS_COARSE_LOCATION}, 123);
    }

    public boolean CheckPermissionOk() {
        //return true if permission is granted
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean LocalizationPermission(){
        if (!CheckPermissionOk()) askPermission();
        return  CheckPermissionOk();
        }
    }

