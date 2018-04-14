package br.com.appgo.appgo.view;

import android.accounts.AccountManager;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import br.com.appgo.appgo.maps.GpsServices;
import br.com.appgo.appgo.controller.PermissionControl;
import br.com.appgo.appgo.R;

public class SplashScreen extends AppCompatActivity {
    private PermissionControl control;
    private GpsServices gpsServices;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        updateUI(currentUser);
        control = new PermissionControl(getApplicationContext(), this);
        //Gps Services Class
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsServices = new GpsServices(this, locationManager);
        setContentView(R.layout.activity_splash_screen);
        TextView textView = (TextView) findViewById(R.id.text_view_splashscreen);
        textView.setText(R.string.frase_inicial);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (control.LocalizationPermission() && gpsServices.GPSServicesRequirePermission()){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1500);

    }
    private void updateUI(FirebaseUser user){
        if (user==null){
            SignAnonymous();
        }
    }

    private void SignAnonymous() {
        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Falha de Conexão",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}




