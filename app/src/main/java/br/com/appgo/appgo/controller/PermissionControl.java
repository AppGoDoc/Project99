package br.com.appgo.appgo.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by hex on 28/01/18.
 */

public class PermissionControl {
    public static final int REQUEST_PERMISSION_LOCATION = 1211;
    public static final int REQUEST_PERMISSION_STORAGE = 1212;
    private Context context;
    private Activity activity;
    String[] locationPermission, externalStoragePermission;
    public PermissionControl(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        locationPermission = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        externalStoragePermission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    public boolean LocalizationPermission(){
        if (!CheckPermissionLocation())
            askPermissionLocation();
        return  CheckPermissionLocation();
    }

    public boolean RequestWriteExternalStorage(){
        if (!CheckPermissionWriteStorage())
            askPermissionWriteStorage();
        return CheckPermissionWriteStorage();
    }

    public void askPermissionLocation(){
        ActivityCompat.requestPermissions(this.activity,locationPermission, REQUEST_PERMISSION_LOCATION);
    }

    public void askPermissionWriteStorage(){
        ActivityCompat.requestPermissions(this.activity, externalStoragePermission, REQUEST_PERMISSION_STORAGE);
    }

    public boolean CheckPermissionLocation() {
        //return true if permission is granted
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
    public boolean CheckPermissionWriteStorage(){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    
}
