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
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.controller.SPreferences;
import br.com.appgo.appgo.fragment.ConfirmLogout;
import br.com.appgo.appgo.fragment.DialogFragmentTransportType;
import br.com.appgo.appgo.fragment.FragmentUserData;
import br.com.appgo.appgo.maps.CreateLatLngBounds;
import br.com.appgo.appgo.maps.MapLocation;
import br.com.appgo.appgo.maps.Rota;
import br.com.appgo.appgo.model.ListLoja;
import br.com.appgo.appgo.model.Local;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.PhotoPicasso;
import br.com.appgo.appgo.persistence.ShareImage;
import br.com.appgo.appgo.services.LocationService;

import static br.com.appgo.appgo.constants.StringConstans.YOUR_ADMOB_APP_ID;
import static br.com.appgo.appgo.services.LocationService.LOCATION_BEARING;
import static br.com.appgo.appgo.view.ActivityAnuncio.LATITUDE_LOJA;
import static br.com.appgo.appgo.view.ActivityAnuncio.USER_LOJA;
import static br.com.appgo.appgo.view.ActivityAnuncio.USER_LOJA_ACTION;
import static br.com.appgo.appgo.view.FiltrarActivity.RADIUS_SEARCH;
import static br.com.appgo.appgo.view.FiltrarActivity.RAMO_SEARCH;
import static br.com.appgo.appgo.view.FiltrarActivity.RESULT_RESET_SEARCH;
import static br.com.appgo.appgo.view.FiltrarActivity.RESULT_SEARCH_FILTER;
import static br.com.appgo.appgo.view.SplashScreen.LIST_LOJA_ACTION;
import static br.com.appgo.appgo.view.SplashScreen.LIST_LOJA_PACK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, ServiceConnection,
        GoogleMap.OnInfoWindowClickListener, DialogFragmentTransportType.ChooseTransportTypeDialogListener,
        View.OnClickListener{
    private AdView mAdView;
    public static final int REQUEST_ERRO_PLAY_SERVICES = 1;
    public static final int REQUEST_CONFIG = 11;
    public static final int RESULT_CONFIG_SAVE = 12;
    public static final String TAXI_STRING = "Serviço Taxí";
    private static final String TAG_FRAGMENT = "UserOptionFragment";
    private static final String TAG_FRAGMENT_LOGOUT = "DialogFragmentConfirmLogout";
    public static final String LOCATION_RESOURCES = "location.resources";
    public static final String LATITUDE_LOCATION = "latitude";
    public static final String LONGITUDE_LOCATION = "longitude";
    private static final String TAG_FRAGMENT_USERDATA = "fragment_dialog_userdata";
    private static final String CLICK_VER_ANUNCIO = "Clique para ver o anúncio.";
    public static final String LOJA_ESCOLHIDA = "loja_escolhida";
    public static final String LOJA_ESCOLHIDA_ACTION = "loja_escolhida_action";
    private static final String TAG_FRAGMENT_TRANSPORT = "transport_type_fragment";
    private static final int REQUEST_LATLNG_LOJA = 12;
    public static final int RESULT_LATLNG_LOJA = 13;
    public static final int REQUEST_LOGIN = 1819;
    public static final String LATLNG_LOJA = "loja_location";
    private static final int REQUEST_FILTER_SEARCH = 1514;
    private static final String ROTA_PARCEL = "rota_parcelable";
    public static final String LIST_LOJA_ACTION_CONFIG = "list_loja_action_config";
    public static final String LIST_LOJA_PACK_CONFIG = "list_loja_pack_config";
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    MapLocation mapLocation;
    private SPreferences preferences;
    private IntentFilter intentFilter, intentFilterRota;
    private Intent serviceIntent, serviceRota;
    private FirebaseAuth mAuth = null;
    private FirebaseUser mUser = null;
    private List<Polyline> polylineList;
    ListLoja listLoja = null;
    List<Marker> myMarker = new LinkedList<>();
    LatLng myLocation = null;
    LatLng destination = null;
    Rota rota = null;
    private float bearing = 0;
    boolean doubleBackToExitPressedOnce = false;
    NavigationView navigationView;
    private ImageView mUserImage, mBtnTaxi, mResetFiltro, mSearchOnMap, floatingActionButton;
    TextView mUserName;
    int sizeIcon = 0;
    String transportType;
    private boolean mTaxiBtnEnabled, mFilterToken;
    ProgressBar mProgressMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Shared Preferences archive to save configs

        mFilterToken = false;
        preferences = new SPreferences(getApplicationContext());
        sizeIcon = preferences.GetIntShared();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        floatingActionButton = findViewById(R.id.floatteste);
        floatingActionButton.setOnClickListener(this);
        FloatButtonRota(false);
        mResetFiltro = findViewById(R.id.img_filtro);
        mResetFiltro.setOnClickListener(this);
        //Calling the view components.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
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

        intentFilter = new IntentFilter();
        intentFilter.addAction(LOCATION_RESOURCES);
        serviceIntent = new Intent(this, LocationService.class);
        listLoja = RequestListLoja();
        mBtnTaxi = findViewById(R.id.btn_taxi);
        mBtnTaxi.setOnClickListener(this);
        mTaxiBtnEnabled = false;
        mSearchOnMap = findViewById(R.id.search_onmap);
        mSearchOnMap.setOnClickListener(this);
        mProgressMarkers = findViewById(R.id.progress_itens);
        MobileAds.initialize(this, YOUR_ADMOB_APP_ID);
        mAdView = findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            try {
                super.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Clique VOLTAR novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mUserImage = findViewById(R.id.image_logo_menu);
        mUserName = findViewById(R.id.nome_usuario);
        getUserName(mUserName,mUserImage);
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getUserName(mUserName, mUserImage);
    }

    private void hideItens(NavigationView view) {
        Menu nav_menu = view.getMenu();
        if (mUser==null||mUser.isAnonymous()){
            nav_menu.findItem(R.id.user_login).setVisible(true);
            nav_menu.findItem(R.id.criar_loja).setVisible(false);
            nav_menu.findItem(R.id.criar_anuncio).setVisible(false);
            nav_menu.findItem(R.id.sair).setVisible(false);
        }
        else{
            getUserName(mUserName,mUserImage);
            nav_menu.findItem(R.id.user_login).setVisible(false);
            nav_menu.findItem(R.id.criar_loja).setVisible(true);
            nav_menu.findItem(R.id.criar_anuncio).setVisible(true);
            nav_menu.findItem(R.id.sair).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Use this menu to set the type of the map

        final int id = item.getItemId();
        switch (id) {
            case R.id.mapmode_hybrid:
                preferences.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
        mUser = mAuth.getCurrentUser();

        int id = item.getItemId();
        switch (id) {
            case R.id.user_login: LoginProcedure();
                break;
            case R.id.advertiser_tutorial: TutorialProcedure();
                break;
            case R.id.config: Configuration();
                break;
            case R.id.criar_loja: CreateStore();
                break;
            case R.id.criar_anuncio: CriarAnuncio();
                break;
            case R.id.filtrar: FilterProcedure();
                break;
            case R.id.share: ShareAppGo();
                break;
            case R.id.sair: ExitProcedure();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CriarAnuncio() {
        boolean tokenVerifiedUserCreateStore = false;
        if (listLoja != null && !mUser.isAnonymous() && mUser!= null){
            for (Loja loja: listLoja.lojas){
                if (loja.anunciante.equals(mUser.getUid())){
                    tokenVerifiedUserCreateStore = true;
                    Intent postarAnuncio = new Intent(getApplicationContext(), PostarActivity.class);
                    postarAnuncio.setAction(USER_LOJA_ACTION);
                    byte[] data = SerializationUtils.serialize(loja);
                    postarAnuncio.putExtra(USER_LOJA, data);
                    startActivity(postarAnuncio);
                }
            }
            if (!tokenVerifiedUserCreateStore){
                Toast.makeText(this, "Você precisa criar o perfil da sua loja no AppGo! " +
                        "para poder postar seus anúncios", Toast.LENGTH_SHORT).show();
            }
        }
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
        if (listLoja != null){
            CreateMarkers(listLoja);
        }
    }
    @Override
    public void onFinishDialogFragment(String transportType) {
        if (myLocation == null){
            Toast.makeText(this, "Impossível criar rota, seu apareho apresenta" +
                    "problemas no envio de sua localização.", Toast.LENGTH_SHORT).show();
        }
        else{
            this.transportType = transportType;
            polylineList = rota.CreateRota(myLocation, destination, transportType);
            FloatButtonRota(true);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        startService(serviceIntent);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        unregisterReceiver(mReceiver);
        stopService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideItens(navigationView);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent.getAction().equals(LOCATION_RESOURCES)&&destination!=null){
                myLocation = new LatLng(
                        intent.getDoubleExtra(LATITUDE_LOCATION, 0),
                        intent.getDoubleExtra(LONGITUDE_LOCATION, 0)
                );
                bearing = intent.getFloatExtra(LOCATION_BEARING, 90);
                mapLocation.atualizarMapa(googleMap, myLocation, bearing, 17.0f);

            }
            if (intent.getAction().equals(LOCATION_RESOURCES)&&destination==null){
                myLocation = new LatLng(
                        intent.getDoubleExtra(LATITUDE_LOCATION, 0),
                        intent.getDoubleExtra(LONGITUDE_LOCATION, 0)
                );
                mapLocation.atualizarMapa(googleMap, myLocation);
            }
        }
    };
//    private void ConsumePolyline(LatLng latLng){
//        boolean token = false;
//        if (polylineList==null||polylineList.isEmpty()){
//            return;
//        }
//        else {
//            for (int i=0;i<polylineList.size();i++){
//                List<LatLng> points = polylineList.get(i).getPoints();
//                for (int i=0;i<points.size();i++){
//                    if (points.get(i).equals(latLng)){
//                        token = true;
//                    }
//                }
//            }
//        }
//    }

    private void CreateMarkers(ListLoja listLoja) {
        try{
            ProgressMarkers(true);
            myMarker = new ArrayList<>();
            googleMap.clear();
            if (listLoja != null && myMarker.isEmpty()){
                PhotoPicasso photoPicasso = new PhotoPicasso(this);
                int i = 0;
                for (Loja loja: listLoja.lojas){
                    LatLng latLng = new LatLng(loja.local.latitude, loja.local.longitude);
                    myMarker.add(
                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(loja.titulo)
                                    .snippet(loja.local.endereco)
                                    .icon(BitmapDescriptorFactory.defaultMarker()))
                    );
                    photoPicasso.PhotoMarkerDownload(loja.urlIcone, myMarker.get(i), sizeIcon);
                    i++;
                }
                googleMap.setOnInfoWindowClickListener(this);
            }
            ProgressMarkers(false);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    private void CreateMarkersFilter(ListLoja listLoja, String filter, double kilometers) {
        ProgressMarkers(true);
        myMarker = new ArrayList<>();
        googleMap.clear();
        LatLng myLatlng = getMyLocation();
        if (listLoja != null && myMarker.isEmpty()){
            CreateLatLngBounds bounds = new CreateLatLngBounds(myLatlng, kilometers);
            try{
                PhotoPicasso photoPicasso = new PhotoPicasso(this);
                int i = 0;
                for (Loja loja: listLoja.lojas){
                    LatLng storeLatlng = new LatLng(loja.local.latitude, loja.local.longitude);
                    if ((loja.ramo.toLowerCase().equals(filter.toLowerCase()) ||
                            filter.toLowerCase().equals("escolha o tipo de ramo"))
                        && bounds.getBounds().contains(storeLatlng)){
                        LatLng latLng = new LatLng(loja.local.latitude, loja.local.longitude);
                        myMarker.add(
                                googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(loja.titulo)
                                        .snippet(loja.local.endereco)
                                        .icon(BitmapDescriptorFactory.defaultMarker()))
                        );
                        photoPicasso.PhotoMarkerDownload(loja.urlIcone, myMarker.get(i), sizeIcon);
                        i++;
                    }
                }
                googleMap.setOnInfoWindowClickListener(this);
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Erro" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Falha na requisição da localização do aparelho, " +
                    "cheque o estado das configurações de localização e tente novamente", Toast.LENGTH_SHORT).show();
        }
        ProgressMarkers(false);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (rota!=null){
//            outState.putSerializable(ROTA_PARCEL, rota);
//            outState.putParcelable("position", myLocation);
//            outState.putParcelable("destination", destination);
//            outState.putSerializable("transport", transportType);
//
//        }
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        rota = (Rota) savedInstanceState.getSerializable(ROTA_PARCEL);
//        myLocation = savedInstanceState.getParcelable("position");
//        destination = savedInstanceState.getParcelable("destination");
//        transportType = (String) savedInstanceState.getSerializable("transport");
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ERRO_PLAY_SERVICES && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
        if (requestCode == REQUEST_LATLNG_LOJA) {
            if (resultCode == RESULT_LATLNG_LOJA) {
                try {
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
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Falha ao criar Rota, tente novamente.", Toast.LENGTH_SHORT).show();
                }

            }
        }
        if (requestCode == REQUEST_FILTER_SEARCH){
            if (resultCode == RESULT_SEARCH_FILTER){
                String ramo = data.getStringExtra(RAMO_SEARCH);
                Double raio = Double.parseDouble(data.getStringExtra(RADIUS_SEARCH));
                CreateMarkersFilter(listLoja, ramo, raio*1000);
                FilterEnabled(true);
            }
            if (resultCode == RESULT_RESET_SEARCH){
                CreateMarkers(listLoja);
                mResetFiltro.setVisibility(View.GONE);
                mResetFiltro.setEnabled(false);
            }
        }
        if (requestCode == REQUEST_CONFIG){
            if (sizeIcon != preferences.GetIntShared()){
                sizeIcon = preferences.GetIntShared();
                CreateMarkers(listLoja);
            }
            if (resultCode == RESULT_OK){
                hideItens(navigationView);
                getUserName(mUserName, mUserImage);
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
    public void onInfoWindowClick(Marker marker) {
        if (marker != null){
            FloatButtonResetRota();
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    private void getUserName(TextView textView, ImageView imageView){
        if (mUser != null){
            if (!mUser.isAnonymous()){
                if (textView != null && imageView != null){
                    textView.setText(mUser.getDisplayName());
                    Picasso.with(this)
                            .load(mUser.getPhotoUrl())
                            .fit()
                            .into(imageView);
                }
            }
        }
    }
    private void ShareAppGo(){
        try {
            new ShareImage(
                    this,
                    "Baixe Já o App Go!",
                    "https://firebasestorage.googleapis.com/v0/b/appgo-1517155420414.appspot.com/o/documents%2Flogo_appgo.png?alt=media&token=f73de093-c3c6-4afc-b3e1-9ad9cb20c6db",
                    "Baixe o já o AppGo!\nhttps://play.google.com/store/apps/details?id=br.com.appgo.appgo"
                    );
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private LatLng getMyLocation(){
        LatLng latLng = new LatLng(
                googleMap.getMyLocation().getLatitude(),
                googleMap.getMyLocation().getLongitude()
        );
        if (latLng == null){
            latLng = myLocation;
        }
        return latLng;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_onmap:

                break;
            case R.id.floatteste:
                FloatButtonResetRota();
                break;
            case R.id.img_filtro:
                if (mFilterToken){
                    CreateMarkers(listLoja);
                    FilterEnabled(false);
                }
                else{
                    FilterProcedure();
                }
                break;
            case R.id.btn_taxi:
                try {
                    if (mTaxiBtnEnabled == false){
                        mTaxiBtnEnabled = true;
                        CreateMarkersFilter(listLoja, TAXI_STRING, 50000f);
                        mBtnTaxi.setImageDrawable(getResources().getDrawable(R.drawable.ic_btn_taxi_negativo));
                    }
                    else{
                        mTaxiBtnEnabled = false;
                        mBtnTaxi.setImageDrawable(getResources().getDrawable(R.drawable.ic_btn_taxi));
                        CreateMarkers(listLoja);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void FloatButtonResetRota() {
        if (destination!=null){
            rota.removePolyline();
            rota =null;
            destination = null;
            FloatButtonRota(false);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void CreateStore(){
        if (mUser != null){
            if (!mUser.isAnonymous()){
                Intent criarLojaIntent = new Intent(this, CriarAnuncioActivity.class);
                startActivity(criarLojaIntent);
            }
            else{
                Toast.makeText(this, "Você precisa estar Logado para criar um anúncio.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Você precisa estar Logado para criar um anúncio.", Toast.LENGTH_SHORT).show();
        }
    }
    private void Configuration(){
        Intent configApp = new Intent(this, ConfigurationActivity.class);
        configApp.setAction(LIST_LOJA_ACTION_CONFIG);
        byte[] data = SerializationUtils.serialize(listLoja);
        configApp.putExtra(LIST_LOJA_PACK_CONFIG, data);
        startActivityForResult(configApp, REQUEST_CONFIG);
    }
    private void LoginProcedure() {
        if (mUser != null){
            if (mUser.isAnonymous()){
                preferences.setAtividade(null);
                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(it);
                finish();
            }
            else {
                // User is signed in
                DialogFragment dialogFragment = new FragmentUserData();
                dialogFragment.show(dialogCall(TAG_FRAGMENT_USERDATA), TAG_FRAGMENT_USERDATA);
            }
        }
        else {
            finish();
        }
    }
    private void FilterProcedure() {
        Intent filtrarIntent = new Intent(this, FiltrarActivity.class);
        startActivityForResult(filtrarIntent, REQUEST_FILTER_SEARCH);
    }
    private void TutorialProcedure() {
        Intent tutorialIntent = new Intent(this, TutorialActivity.class);
        startActivity(tutorialIntent);
    }
    private void ExitProcedure() {
        if (mUser != null){
            if (!mUser.isAnonymous()){
                DialogFragment dialogFragment = new ConfirmLogout();
                dialogFragment.show(dialogCall(TAG_FRAGMENT_LOGOUT), TAG_FRAGMENT_LOGOUT);
            }
            else {
                Toast.makeText(this, "Não existe usuário Logado no sistema.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Não existe usuário Logado no sistema.", Toast.LENGTH_SHORT).show();
        }
    }
    private ListLoja RequestListLoja() {
        ListLoja listLoja = null;
        Intent intent = getIntent();
        if (intent.getAction().equals(LIST_LOJA_ACTION)){
            byte[] data = intent.getByteArrayExtra(LIST_LOJA_PACK);
            listLoja = SerializationUtils.deserialize(data);
        }
        return listLoja;
    }
    private void updateCameraBearing(GoogleMap googleMap, float bearing) {
        if ( googleMap == null) return;
        CameraPosition camPos = CameraPosition
                .builder(
                        googleMap.getCameraPosition() // current Camera
                )
                .bearing(bearing)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }
    private void ProgressMarkers(boolean token){
        if (token){
            mProgressMarkers.setVisibility(View.VISIBLE);
            mBtnTaxi.setEnabled(false);
        }
        else {
            mProgressMarkers.setVisibility(View.GONE);
            mBtnTaxi.setEnabled(true);
        }
    }
    private void FilterEnabled(boolean token){
        if (token){
            mFilterToken = true;
            mResetFiltro.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_neg));
        }
        else{
            mFilterToken = false;
            mResetFiltro.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_pos));
        }
    }
}