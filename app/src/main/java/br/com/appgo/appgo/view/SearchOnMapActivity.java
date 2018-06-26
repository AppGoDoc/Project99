package br.com.appgo.appgo.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.fragment.AdressNotFind;
import br.com.appgo.appgo.fragment.ConfirmLocationFragment;
import br.com.appgo.appgo.maps.MapLocation;
import br.com.appgo.appgo.model.User;
import br.com.appgo.appgo.services.FindAddress;

import static br.com.appgo.appgo.view.CriarAnuncioActivity.ADRESS_LATITUDE;
import static br.com.appgo.appgo.view.CriarAnuncioActivity.ADRESS_LONGITUDE;
import static br.com.appgo.appgo.view.CriarAnuncioActivity.ADRESS_NAME;
import static br.com.appgo.appgo.view.CriarAnuncioActivity.ADRESS_OBS;
import static br.com.appgo.appgo.view.CriarAnuncioActivity.RESULT_FIND_ADDRESS;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class SearchOnMapActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener{

    private String TAG_FRAGMENT = "ConfirmLocationFragment";
    private GoogleMap googleMap;
    private MapLocation mapLocation;
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar progressBar;
    private EditText addressName, addressObs;
    private FindAddress findAddress;
    private List<Address> addressList = null;
    private Marker marker;
    private FloatingActionButton buttonConfirm, button;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference userReference;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("Anunciantes/"+ firebaseUser.getUid());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        addressName = (EditText) findViewById(R.id.edtTextAdress);
        addressObs = (EditText) findViewById(R.id.edtTextAdressObs);
        button = (FloatingActionButton) findViewById(R.id.button2);
        buttonConfirm = (FloatingActionButton)findViewById(R.id.float_button_confirm);
        buttonConfirm.setEnabled(false);
        buttonConfirm.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mapLocation = new MapLocation(getApplicationContext(), googleMap, mGoogleApiClient);
        findAddress = new FindAddress(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressName.getText().toString();
                MudaMeuNome(address);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    buttonConfirm.setEnabled(false);
                    buttonConfirm.setVisibility(View.GONE);
                    if (marker.getPosition()!=null){
                        LatLng latLng = marker.getPosition();
                        Intent intent = new Intent();
                        intent.putExtra(ADRESS_LATITUDE, latLng.latitude);
                        intent.putExtra(ADRESS_LONGITUDE, latLng.longitude);
                        intent.putExtra(ADRESS_NAME, addressName.getText().toString());
                        intent.putExtra(ADRESS_OBS, addressObs.getText().toString());
                        setResult(RESULT_FIND_ADDRESS, intent);
                        finish();
                    }
                    else{
                        Toast.makeText(SearchOnMapActivity.this, "Endereço inválido, tente novamente inserindo" +
                                " um endereço válido.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mapLocation.setMyLocationMarke(googleMap);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 1);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Erro de Conexão: "
                    + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
        startActivity(intent);
    }

    public void AddressShowList(final List<Address> addresses) {
        final String[] addreesName = StreetNames(addresses);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(addreesName, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                if (fragment != null)
                    fragmentTransaction.remove(fragment);
                fragmentTransaction.addToBackStack(TAG_FRAGMENT);
                DialogFragment dialogFragment = new ConfirmLocationFragment();
                dialogFragment.show(fragmentTransaction, TAG_FRAGMENT);

                LatLng lng = new LatLng(addresses.get(item).getLatitude(),
                        addresses.get(item).getLongitude());
                googleMap.animateCamera(newLatLngZoom(lng,17.0f));
                marker = googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker())
                        .position(lng)
                        .draggable(true));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        buttonConfirm.setEnabled(true);
        buttonConfirm.setVisibility(View.VISIBLE);
    }

    private String[] StreetNames(List<Address> addressList) {
        String[] streetNames = new String[addressList.size()];
        for(int i = 0; i < addressList.size(); i++){
            Address address = addressList.get(i);
            streetNames[i] = String.format("%s, %s. %s", address.getThoroughfare(), address.getFeatureName(), address.getLocality());
        }
        return streetNames;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(this, "Arraste o marcador até o local desejado.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
    public void MudaMeuNome(String addressText){
        if (marker != null){
            marker.remove();
        }
        if (!addressText.isEmpty()) {
            addressList = findAddress.getmFindAddress(addressText);
            if (addressList == null){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                if (fragment == null)
                    fragmentTransaction.remove(fragment);
                fragmentTransaction.addToBackStack(TAG_FRAGMENT);
                DialogFragment dialogFragment = new AdressNotFind();
                dialogFragment.show(fragmentTransaction, TAG_FRAGMENT);
            } else {
                AddressShowList(addressList);
                ((InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(addressName.getWindowToken(), 0);
            }
        }
    }
}