package br.com.appgo.appgo.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.commons.lang3.SerializationUtils;
import java.util.LinkedList;
import java.util.List;
import br.com.appgo.appgo.fragment.DialogFragmentTransportType;
import br.com.appgo.appgo.maps.MapLocation;
import br.com.appgo.appgo.maps.Rota;
import br.com.appgo.appgo.model.Local;
import br.com.appgo.appgo.persistence.PhotoPicasso;
import br.com.appgo.appgo.controller.SPreferences;
import br.com.appgo.appgo.fragment.ConfirmLogout;
import br.com.appgo.appgo.fragment.FragmentUserData;
import br.com.appgo.appgo.model.ListLoja;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.R;
import br.com.appgo.appgo.services.LoadMarkers;
import br.com.appgo.appgo.services.LocationService;
import static br.com.appgo.appgo.constants.StringConstans.ACTION_RECEIVE_MARKER;
import static br.com.appgo.appgo.constants.StringConstans.LOJAS_LIST_RECEIVE;
import static br.com.appgo.appgo.view.ActivityAnuncio.LATITUDE_LOJA;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, ServiceConnection,
        GoogleMap.OnMarkerClickListener, DialogFragmentTransportType.ChooseTransportTypeDialogListener {

    private static final int REQUEST_ERRO_PLAY_SERVICES = 1;
    private static final String TAG_FRAGMENT = "UserOptionFragment";
    private static final String TAG_FRAGMENT_LOGOUT = "DialogFragmentConfirmLogout";
    public static final String LOCATION_RESOURCES = "location.resources";
    public static final String LATITUDE_LOCATION = "latitude";
    public static final String LONGITUDE_LOCATION = "longitude";
    private static final String TAG_FRAGMENT_USERDATA = "fragment_dialog_userdata";
    public static final String LOJA_ESCOLHIDA = "loja_escolhida";
    public static final String LOJA_ESCOLHIDA_ACTION = "loja_escolhida_action";
    private static final String TAG_FRAGMENT_TRANSPORT = "transport_type_fragment";
    private static final int REQUEST_LATLNG_LOJA = 12;
    public static final int RESULT_LATLNG_LOJA = 13;
    public static final String LATLNG_LOJA = "loja_location";
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    MapLocation mapLocation;
    private FloatingActionButton floatingActionButton;
    private SPreferences preferences;
    private IntentFilter intentFilter, intentFilterMarker;
    private Intent serviceIntent, serviceIntentMarker;
    private FirebaseAuth mAuth = null;
    private FirebaseUser mUser = null;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ListLoja listLoja = null;
    List<Marker> myMarker = new LinkedList<>();
    LatLng myLocation = null;
    LatLng destination = null;
    Polyline mPolyline = null;
    Rota rota = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Shared Preferences archive to save configs
        preferences = new SPreferences(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        floatingActionButton = findViewById(R.id.floatteste);
        FloatButtonRota(false);
        //Calling the view components.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Calling Maps components
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        intentFilterMarker = new IntentFilter();
        intentFilterMarker.addAction(ACTION_RECEIVE_MARKER);
        serviceIntentMarker = new Intent(this, LoadMarkers.class);
        intentFilter = new IntentFilter();
        intentFilter.addAction(LOCATION_RESOURCES);
        serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntentMarker);
        registerReceiver(mBroadcastReceiver, intentFilterMarker);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rota.removePolyline();
                FloatButtonRota(false);
            }
        });
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == ACTION_RECEIVE_MARKER){
                byte[] data = intent.getByteArrayExtra(LOJAS_LIST_RECEIVE);
                ListLoja listTemp = SerializationUtils.deserialize(data);
                if (listLoja != listTemp){
                    listLoja = listTemp;
                    CreateMarkers(listLoja);
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Use this menu to set the type of the map

        final int id = item.getItemId();
        switch (id) {
            case R.id.mapmode_hybrid:
                preferences.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.mapmode_none:
                preferences.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapmode_normal:
                preferences.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapmode_satellite:
                preferences.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapmode_terrain:
                preferences.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
        mapLocation.GoogleMapOptionsSettings(googleMap, preferences.getMapType());
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.user_login:
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    DialogFragment dialogFragment = new FragmentUserData();
                    dialogFragment.show(dialogCall(TAG_FRAGMENT_USERDATA), TAG_FRAGMENT_USERDATA);
                } else {
                    preferences.setAtividade(null);
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                    //it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                }
                break;
            case R.id.advertiser_tutorial:
                break;
            case R.id.criar_loja:
                if (mUser == null || mAuth == null){

                } else {
                    Intent criarLojaIntent = new Intent(this, CriarAnuncioActivity.class);
                    startActivity(criarLojaIntent);
                    finish();
                }
                break;
            case R.id.filtrar:
                break;
            case R.id.share:
                break;
            case R.id.sair:
                if (mAuth.getCurrentUser() != null){
                    DialogFragment dialogFragment = new ConfirmLogout();
                    dialogFragment.show(dialogCall(TAG_FRAGMENT_LOGOUT), TAG_FRAGMENT_LOGOUT);
                }
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //mapLocation.getLastLocation(googleMap);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_ERRO_PLAY_SERVICES);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Erro de Conexão: "
                    + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapLocation = new MapLocation(getApplicationContext(), googleMap, mGoogleApiClient);
        mapLocation.GoogleMapOptionsSettings(googleMap, preferences.getMapType());
        mapLocation.getLastLocation(googleMap);
        this.googleMap = googleMap;
    }
    @Override
    public void onFinishDialogFragment(String transportType) {
        rota.CreateRota(myLocation, destination, transportType);
        FloatButtonRota(true);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
        stopService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(serviceIntent);
        registerReceiver(mReceiver, intentFilter);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double latitude = 0f;
            double longitude = 0f;
            if (intent.getAction() == LOCATION_RESOURCES){
                myLocation = new LatLng(
                        (Double) intent.getDoubleExtra(LATITUDE_LOCATION, 0),
                        (Double) intent.getDoubleExtra(LONGITUDE_LOCATION, 0)
                );
                mapLocation.atualizarMapa(googleMap, myLocation);
                mapLocation.getLastLocation(googleMap);

            }
        }
    };

    private void CreateMarkers(ListLoja listLoja) {
        if (listLoja != null){
            PhotoPicasso photoPicasso = new PhotoPicasso(this);
            int i = 0;
            for (Loja loja: listLoja.lojas){
                LatLng latLng = new LatLng(loja.local.latitude, loja.local.longitude);
                ImageView imageView = new ImageView(this);
                photoPicasso.Photo18x18(loja.urlIcone, imageView, true);
                myMarker.add(
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(loja.titulo)
                                .snippet(loja.local.endereco))
                );
                photoPicasso.PhotoMarkerDownload(loja.urlIcone, myMarker.get(i));
                i++;
            }
            googleMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ERRO_PLAY_SERVICES && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
        if (requestCode == REQUEST_LATLNG_LOJA) {
            if (resultCode == RESULT_LATLNG_LOJA) {
                Local tmpLocal = (Local) data.getSerializableExtra(LATITUDE_LOJA);
                destination = new LatLng(tmpLocal.latitude, tmpLocal.longitude);
                myLocation = new LatLng(
                        googleMap.getMyLocation().getLatitude(),
                        googleMap.getMyLocation().getLongitude()
                );
                if (myLocation!=null && destination!=null){
                    if (rota == null){
                        rota = new Rota(googleMap, this, getResources().getString(R.string.google_maps_server_key));
                    } else{
                        rota.removePolyline();
                    }
                    DialogFragment dialogFragment = new DialogFragmentTransportType();
                    dialogFragment.show(dialogCall(TAG_FRAGMENT_TRANSPORT), TAG_FRAGMENT_TRANSPORT);

                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public FragmentTransaction dialogCall(String Tag){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(Tag);
        if (fragment != null)
            fragmentTransaction.remove(fragment);
        fragmentTransaction.addToBackStack(Tag);
        return fragmentTransaction;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //googleMap.clear();
        if (marker != null){
            LatLng latLng = marker.getPosition();
            for (Loja loja: listLoja.lojas){
                if (loja.local.latitude == latLng.latitude &&
                        loja.local.longitude == latLng.longitude){
                    Intent intentAnuncio = new Intent(this, ActivityAnuncio.class);
                    intentAnuncio.setAction(LOJA_ESCOLHIDA_ACTION);
                    byte[] data = SerializationUtils.serialize(loja);
                    intentAnuncio.putExtra(LOJA_ESCOLHIDA, data);
                    startActivityForResult(intentAnuncio, REQUEST_LATLNG_LOJA);
                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        stopService(serviceIntentMarker);
    }
    public void FloatButtonRota(boolean token){
        if (token){
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setClickable(true);
        }
        else{
            floatingActionButton.setClickable(false);
            floatingActionButton.setVisibility(View.GONE);
        }
    }
}