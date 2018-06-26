package br.com.appgo.appgo.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.SerializationUtils;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.controller.PermissionControl;
import br.com.appgo.appgo.controller.SPreferences;
import br.com.appgo.appgo.maps.GpsServices;
import br.com.appgo.appgo.model.ListLoja;
import br.com.appgo.appgo.services.LoadMarkers;

import static br.com.appgo.appgo.constants.StringConstans.ACTION_RECEIVE_MARKER;
import static br.com.appgo.appgo.constants.StringConstans.LOJAS_LIST_RECEIVE;

public class SplashScreen extends AppCompatActivity {
    public static final String LIST_LOJA_PACK = "list_loja_downloaded";
    public static final String LIST_LOJA_ACTION = "list_loja_action";
    private PermissionControl control;
    private GpsServices gpsServices;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    ListLoja listLoja;
    private IntentFilter intentFilterMarker;
    private Intent serviceIntentMarker;
    SPreferences preferences;
    int iconSize = 120;
    boolean tokenPermission, tokenListLoja, tokenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenListLoja = tokenPermission = tokenUser = false;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        preferences = new SPreferences(this);
        if (currentUser == null){
            updateUI(null);
        }
        control = new PermissionControl(getApplicationContext(), this);
        //Gps Services Class
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsServices = new GpsServices(this, locationManager);
        setContentView(R.layout.activity_splash_screen);
        TextView textView = (TextView) findViewById(R.id.text_view_splashscreen);
        textView.setText(R.string.frase_inicial);
        intentFilterMarker = new IntentFilter();
        intentFilterMarker.addAction(ACTION_RECEIVE_MARKER);
        serviceIntentMarker = new Intent(this, LoadMarkers.class);
        iconSize = preferences.GetIntShared();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(serviceIntentMarker);
        registerReceiver(mBroadcastReceiver, intentFilterMarker);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
        stopService(serviceIntentMarker);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_RECEIVE_MARKER)){
                byte[] data = intent.getByteArrayExtra(LOJAS_LIST_RECEIVE);
                ListLoja listTemp = SerializationUtils.deserialize(data);
                if (listLoja != listTemp){
                    listLoja = listTemp;
//                    CreateMarkers(listLoja);
                    RequestPermissions();
                }
            }
        }
    };

//    private void CreateMarkers(final ListLoja listLoja) {
//        PhotoPicasso picasso = new PhotoPicasso(this);
//        for (int i=0; i<listLoja.lojas.size();i++){
//            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(listLoja.lojas.get(i).urlIcone);
//            final int finalI = i;
//            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    try {
//                        listLoja.lojas.get(finalI).bitmap = Picasso.with(getApplicationContext())
//                                                                .load(uri)
//                                                                .get();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            picasso.PhotoMarkerDownload(listLoja.lojas.get(i).urlIcone, listLoja.lojas.get(i), iconSize);
//        }
//    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionControl.REQUEST_PERMISSION_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.setAction(LIST_LOJA_ACTION);
                        byte[] data = SerializationUtils.serialize(listLoja);
                        intent.putExtra(LIST_LOJA_PACK, data);
                        startActivity(intent);
                        finish();

                } else {
                    Toast.makeText(this, "O AppGo! precisa de permissões de localidade para funcionar."
                            , Toast.LENGTH_SHORT).show();
                    RequestPermissions();

                }
                return;
            }

        }
    private void RequestPermissions(){
        if (gpsServices.GPSServicesRequirePermission()){
            control.askPermissionLocation();
        }
    }
}




