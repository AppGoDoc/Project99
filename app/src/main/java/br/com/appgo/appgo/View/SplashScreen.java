package br.com.appgo.appgo.View;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import br.com.appgo.appgo.Controller.GpsServices;
import br.com.appgo.appgo.Controller.PermissionControl;
import br.com.appgo.appgo.R;


public class SplashScreen extends AppCompatActivity {
    private PermissionControl control;
        private GpsServices gpsServices;

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (control.LocalizationPermission() && gpsServices.GPSServicesRequirePermission()){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }}, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        control = new PermissionControl(getApplicationContext(), this);
        //Gps Services Class
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsServices = new GpsServices(this, locationManager);
        setContentView(R.layout.activity_splash_screen);
        TextView textView = (TextView) findViewById(R.id.text_view_splashscreen);
        textView.setText(R.string.frase_inicial);

    }

}




