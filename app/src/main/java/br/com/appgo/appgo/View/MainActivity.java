package br.com.appgo.appgo.View;

import android.app.ActionBar;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.ImageView;
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

import org.apache.commons.lang3.SerializationUtils;

import java.util.LinkedList;
import java.util.List;

import br.com.appgo.appgo.Controller.MapLocation;
import br.com.appgo.appgo.Persistence.PhotoPicasso;
import br.com.appgo.appgo.Controller.SPreferences;
import br.com.appgo.appgo.Fragment.ConfirmLogout;
import br.com.appgo.appgo.Fragment.FragmentUserData;
import br.com.appgo.appgo.Model.ListLoja;
import br.com.appgo.appgo.Model.Loja;
import br.com.appgo.appgo.R;
import br.com.appgo.appgo.Services.LoadMarkers;
import br.com.appgo.appgo.Services.LocationService;

import static br.com.appgo.appgo.Constants.StringConstans.ACTION_RECEIVE_MARKER;
import static br.com.appgo.appgo.Constants.StringConstans.LOJAS_LIST_RECEIVE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, ServiceConnection,
        GoogleMap.OnMarkerClickListener{

    private static final int REQUEST_ERRO_PLAY_SERVICES = 1;
    private static final String TAG_FRAGMENT = "UserOptionFragment";
    private static final String TAG_FRAGMENT_LOGOUT = "DialogFragmentConfirmLogout";
    public static final String LOCATION_RESOURCES = "location.resources";
    public static final String LATITUDE_LOCATION = "latitude";
    public static final String LONGITUDE_LOCATION = "longitude";
    private static final String TAG_FRAGMENT_USERDATA = "fragment_dialog_userdata";
    public static final String LOJA_ESCOLHIDA = "loja_escolhida";
    public static final String LOJA_ESCOLHIDA_ACTION = "loja_escolhida_action";
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    MapLocation mapLocation;
    private FloatingActionButton floatingActionButton;
    private SPreferences preferences;
    private IntentFilter intentFilter, intentFilterMarker;
    private Intent serviceIntent, serviceIntentMarker;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    FirebaseAuth.AuthStateListener mAuthListener;
    ListLoja listLoja = null;
    List<Marker> myMarker = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Shared Preferences archive to save configs
        preferences = new SPreferences(getApplicationContext());

        floatingActionButton = findViewById(R.id.floatteste);
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
        CreateMarkers(listLoja);
    }

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
                //Log.d("     +++     ", "on button anunciante");
                FirebaseUser user = mAuth.getCurrentUser();
                //Log.d("     +++     ", "on button anunciante");
                if (user != null) {
                    // User is signed in
                    DialogFragment dialogFragment = new FragmentUserData();
                    dialogFragment.show(dialogCall(TAG_FRAGMENT_USERDATA), TAG_FRAGMENT_USERDATA);
                } else {
                    preferences.setAtividade(null);
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                }
                break;
            case R.id.advertiser_tutorial:
                break;
            case R.id.criar_loja:
                if (mUser == null){

                } else {
                    Intent criarLojaIntent = new Intent(this, CriarAnuncioActivity.class);
                    startActivity(criarLojaIntent);
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
        mapLocation.getLastLocation(googleMap);
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
        CreateMarkers(listLoja);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        CreateMarkers(listLoja);
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
        unregisterReceiver(mBroadcastReceiver);
        stopService(serviceIntentMarker);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(serviceIntent);
        registerReceiver(mReceiver, intentFilter);
        startService(serviceIntentMarker);
        registerReceiver(mBroadcastReceiver, intentFilterMarker);
        CreateMarkers(listLoja);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double latitude = 0f;
            double longitude = 0f;
            if (intent.getAction() == LOCATION_RESOURCES){
                latitude = (Double) intent.getDoubleExtra(LATITUDE_LOCATION, 0);
                longitude = (Double) intent.getDoubleExtra(LONGITUDE_LOCATION, 0);
                LatLng latLng = new LatLng(latitude, longitude);
                mapLocation.setMyLocationMarke(googleMap,latLng);
                }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == ACTION_RECEIVE_MARKER){
                byte[] data = intent.getByteArrayExtra(LOJAS_LIST_RECEIVE);
                listLoja = SerializationUtils.deserialize(data);
                CreateMarkers(listLoja);
            }
        }
    };

    private void CreateMarkers(ListLoja listLoja) {
        if (listLoja != null){
            PhotoPicasso photoPicasso = new PhotoPicasso(this);
            Bitmap bitmap = null;
            for (Loja loja: listLoja.lojas){
                LatLng latLng = new LatLng(loja.local.latitude, loja.local.longitude);
                ImageView imageView = new ImageView(this);
                photoPicasso.Photo24x24(loja.urlIcone, imageView, true);
                myMarker.add(
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker())
                                .title(loja.titulo)
                                .snippet(loja.local.endereco))
                );
            }
            for (int i = 0; i <= listLoja.lojas.size()-1; i++){
                photoPicasso.PhotoMarkerDownload(listLoja.lojas.get(i).urlIcone, myMarker.get(i));
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

  /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listlojas", listLoja);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getSerializable("listlojas");
    }*/

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
        LatLng latLng = marker.getPosition();
        for (Loja loja: listLoja.lojas){
            if (loja.local.latitude == latLng.latitude &&
                    loja.local.longitude == latLng.longitude){
                Intent intentAnuncio = new Intent(this, ActivityAnuncio.class);
                intentAnuncio.setAction(LOJA_ESCOLHIDA_ACTION);
                byte[] data = SerializationUtils.serialize(loja);
                intentAnuncio.putExtra(LOJA_ESCOLHIDA, data);
                //intentAnuncio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentAnuncio);
            }
        }
        return false;
    }
}