package br.com.appgo.appgo.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.SerializationUtils;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.controller.PermissionControl;
import br.com.appgo.appgo.controller.UnmaskPhoneNumber;
import br.com.appgo.appgo.model.Comentario;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.FireBase;
import br.com.appgo.appgo.persistence.PhotoPicasso;
import br.com.appgo.appgo.persistence.ShareImage;

import static br.com.appgo.appgo.constants.StringConstans.CRIAR_ANUNCIO;
import static br.com.appgo.appgo.constants.StringConstans.VER_ANUNCIO;
import static br.com.appgo.appgo.view.CriarAnuncioActivity.ANUNCIOS;
import static br.com.appgo.appgo.view.MainActivity.LOJA_ESCOLHIDA;
import static br.com.appgo.appgo.view.MainActivity.LOJA_ESCOLHIDA_ACTION;
import static br.com.appgo.appgo.view.MainActivity.RESULT_LATLNG_LOJA;

/**
 * Created by hex on 09/03/18.
 */

public class ActivityAnuncio extends AppCompatActivity implements View.OnClickListener {
    public static final String USER_LOJA = "loja_do_usuario";
    public static final String USER_LOJA_ACTION = "action user loja";
    public static final String LATITUDE_LOJA = "latitude_loja";
    public static final String LONGITUDE_LOJA = "longitude_loja";
    public static final String LOJA_COMENT = "loja_comentada";
    public static final String EMAIL_APP_DENUNCIA = "appgo.website@gmail.com";
    public static final int COMENT_REQUEST = 1231;
    private static final String COMENT_COUNTER = "counter_comments";
//    private RecyclerView recyclerAnuncio;
//    private AdapterFotoAnuncio adapterFotoAnuncio;
//    private RecyclerView.LayoutManager layoutManager;
    Loja loja = null;
    List<String> urlFotos;
    TextView mAnuncioTitulo, curtidas, comentarios, txtEndereco, mCompartilhamentos;
    Button btnRamo, btnDenunciar, btnAnuncio, btnCriarRota;
    ImageView btnWhatsapp, btnTelefone, btnEmail;
    ImageButton curtir, comentar, compartilhar;
    FirebaseAuth auth;
    FirebaseUser user;
    boolean curtidaToken = false;
    String fileShare;
    Uri bitmapUri;
    Bitmap bitmap = null;
    private PermissionControl control;
    FireBase fireBase = new FireBase(this);
//    PermissionControl permissionControl;
//    HorizontalInfiniteCycleViewPager carroussel;
    ImageView mImageStore;
//    FotosAdapter fotosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        loja = RequestLoja();
        urlFotos = GetListFotos(loja);
//        carroussel =  findViewById(R.id.carroussel_fotos_loja);
//        fotosAdapter = new FotosAdapter(urlFotos, getApplicationContext());
//        carroussel.setAdapter(fotosAdapter);
//        permissionControl = new PermissionControl(getApplicationContext(), getParent());
        btnAnuncio = findViewById(R.id.button_ver_anuncio);
        btnAnuncio.setOnClickListener(this);
//        btnAnuncio.setText(IsAnuncer(GetUserUid(user)));
        btnAnuncio.setText(VER_ANUNCIO);
        btnDenunciar = findViewById(R.id.denuncia_button);
        btnDenunciar.setOnClickListener(this);
        compartilhar = findViewById(R.id.sharing_anuncio);
        compartilhar.setOnClickListener(this);
        comentar = findViewById(R.id.comentar_anuncio);
        comentar.setOnClickListener(this);
        comentarios = findViewById(R.id.cont_comment);
        comentarios.setText(getComments());
        curtidas = findViewById(R.id.contagem_curtir);
        curtidas.setText(String.valueOf(getCurtidas()));
        curtir = findViewById(R.id.curtir_anuncio);
        curtir.setOnClickListener(this);
        mCompartilhamentos = findViewById(R.id.cont_share);
        mAnuncioTitulo = findViewById(R.id.titulo_anuncio);
        mAnuncioTitulo.setText(loja.titulo);
        txtEndereco = findViewById(R.id.anuncio_endereco);
        txtEndereco.setText(String.format("%s", loja.local.endereco));
        btnTelefone = findViewById(R.id.image_telefone);
        btnTelefone.setOnClickListener(this);
        btnWhatsapp = findViewById(R.id.image_whatsapp);
        btnWhatsapp.setOnClickListener(this);
        btnEmail = findViewById(R.id.image_email);
        btnEmail.setOnClickListener(this);
        btnRamo = findViewById(R.id.anuncio_ramo);
        btnRamo.setText(loja.ramo);
        btnCriarRota = findViewById(R.id.create_route);
        btnCriarRota.setOnClickListener(this);
        fileShare = null;
        bitmapUri = null;
        mImageStore = findViewById(R.id.carroussel_fotos_loja);
        PhotoPicasso picasso = new PhotoPicasso(this);
        picasso.Photo2fit(loja.urlFoto1, mImageStore, 1200, 800, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DenunciarEnable(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        curtidas.setText(String.valueOf(getCurtidas()));
        comentarios.setText(String.valueOf(getComments()));
        mCompartilhamentos.setText(String.valueOf(getSharing()));
//        CreateRecyclerAnuncio(loja);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.denuncia_button:
                SendDenuncia();
                break;
            case R.id.comentar_anuncio:
                SendComent();
                break;
            case R.id.create_route:
                CreateRota();
                break;
            case R.id.image_telefone:
                TelephoneContact();
                break;
            case R.id.image_whatsapp:
                WhatsAppContact();
                break;
            case R.id.image_email:
                SendEmail();
                break;
            case R.id.curtir_anuncio:
                if (user!=null) {
                    if (!user.isAnonymous()) {
                        setCurtidas();
                    }
                    else
                        Toast.makeText(this, "Faça Login para ter acesso a todas " +
                                "as funcionalidades do AppGo!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sharing_anuncio:
                shareImage(loja);
                break;
            case R.id.button_ver_anuncio:
                VerAnuncio();
                break;
        }
    }

    private void SendComent() {
        if (user == null || user.isAnonymous()) {
            Toast.makeText(this, "É preciso estar logado\n" +
                    "para fazer um comentário.", Toast.LENGTH_SHORT).show();
        } else {
            Intent comentIntent = new Intent(this, ComentActivity.class);
            comentIntent.putExtra(LOJA_COMENT, loja);
            startActivityForResult(comentIntent, COMENT_REQUEST);
        }
    }

    private void SendDenuncia() {
        Intent denunciaMail = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        denunciaMail.setType("text/plain");
        denunciaMail.putExtra(Intent.EXTRA_SUBJECT, "Denuncia de abuso de " + user.getDisplayName());
        //it.putExtra(Intent.EXTRA_TEXT, "Body of email");
        denunciaMail.setData(Uri.parse("mailto:" + EMAIL_APP_DENUNCIA)); // or just "mailto:" for blank
        denunciaMail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(denunciaMail);
    }

    private void CreateRota() {
        Intent intentRota = new Intent();
        intentRota.putExtra(LATITUDE_LOJA, loja.local);
        intentRota.putExtra(LONGITUDE_LOJA, loja.local.longitude);
        setResult(RESULT_LATLNG_LOJA, intentRota);
        finish();
    }

    private void TelephoneContact() {
        final int REQUEST_PHONE_CALL = 1;
        UnmaskPhoneNumber unmaskPhoneNumber = new UnmaskPhoneNumber();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + unmaskPhoneNumber.whatsNumber(loja.telefone)));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
            else{
                startActivity(intent);
            }
        }
        else {
            startActivity(intent);
        }
    }

    private void WhatsAppContact() {
        UnmaskPhoneNumber unmask = new UnmaskPhoneNumber();
        String number = unmask.whatsNumber(loja.whatsapp);
        PackageManager packageManager = this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone="+ number +"&text=" +
                    URLEncoder.encode("Olá,\nsou cliente AppGo!", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            }
            else {
                Toast.makeText(this, "Você precisa instalar o WhatsApp no seu celular.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SendEmail() {
        Intent it = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        it.setType("text/plain");
        it.putExtra(Intent.EXTRA_SUBJECT, "Cliente AppGo! " + user.getDisplayName());
        //it.putExtra(Intent.EXTRA_TEXT, "Body of email");
        it.setData(Uri.parse("mailto:" + loja.emailAnuncio)); // or just "mailto:" for blank
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(it);
    }

    private void VerAnuncio() {
        Intent verAnuncio = new Intent(getApplicationContext(), VerAnuncioActivity.class);
        verAnuncio.setAction(USER_LOJA_ACTION);
        byte[] data = SerializationUtils.serialize(loja);
        verAnuncio.putExtra(USER_LOJA, data);
        startActivity(verAnuncio);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == COMENT_REQUEST){
//            if (resultCode == RESULT_OK){
//                int counterComents = data.getIntExtra(COMENT_COUNTER, -1);
//                if (counterComents>-1){
//                    comentarios.setText(counterComents);
//                }
//            }
//        }
    }

    private Loja RequestLoja() {
        Loja loja = null;
        Intent intent = getIntent();
        if (intent.getAction().equals(LOJA_ESCOLHIDA_ACTION)){
            byte[] data = intent.getByteArrayExtra(LOJA_ESCOLHIDA);
            loja = SerializationUtils.deserialize(data);
        }
        return loja;
    }
    private List<String> GetListFotos(Loja loja){
        List<String> urlFotos = new LinkedList<>();
        if (loja != null){
            urlFotos.add(loja.urlFoto1);
            urlFotos.add(loja.urlFoto2);
            urlFotos.add(loja.urlFoto3);
        }
        return urlFotos;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        finish();
    }
    private void shareImage(Loja loja) {
        try {
            ShareImage shareImage = new ShareImage(this);
            shareImage.shareItemFromReference(
                    loja.urlFoto1,
                    loja.titulo + " está no AppGo!\nBaixe o App e confira",
                    loja.titulo + " está no AppGo!\nBaixe o App e confira:\nhttps://play.google.com/store/apps/details?id=br.com.appgo.appgo"
            );
            loja.sharing += 1;
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Anuncios/" + loja.anunciante);
            database.child("sharing").setValue(loja.sharing);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getComments(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ANUNCIOS);
        List<Comentario> comentario = fireBase.GetListComentario(
                reference.child(loja.anunciante).child("Comentario"));
        return String.format("%s",comentario.size());
    }
    private int getSharing(){
        return loja.sharing;
    }
    private int getCurtidas() {
        curtidas.setEnabled(false);
        if (user != null){
            if (!user.isAnonymous()){
                curtidas.setEnabled(true);
                for (int i = 0; i < loja.curtidas.size(); ++i) {
                    if (loja.curtidas.get(i).equals(user.getUid())) {
                        userAlreadyLike();
                    }
                }
            }
        }
        return loja.curtidas.size();
    }

    private void userAlreadyLike() {
        curtidas.setTextColor(getResources().getColor(R.color.colorPrimary));
        curtidaToken = true;
    }

    private void setCurtidas(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Anuncios/" + loja.anunciante);
        if (curtidaToken){
            for (int i = 0; i<loja.curtidas.size(); i++){
                if (loja.curtidas.get(i).equals(user.getUid())){
                    loja.curtidas.remove(i);
                }
                curtidas.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                curtidaToken = false;
                database.child("curtidas").setValue(loja.curtidas);
            }
        }
        else{
            loja.curtidas.add(user.getUid());
            database.child("curtidas").setValue(loja.curtidas);
        }
        curtidas.setText(String.valueOf(getCurtidas()));
    }
    private String IsAnuncer(String uid){
        if(loja.anunciante.equals(uid))
            return CRIAR_ANUNCIO;
        else{
            return VER_ANUNCIO;
        }
    }
    private String GetUserUid(FirebaseUser user){
        try{
            return user.getUid();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private void DenunciarEnable(FirebaseUser user){
        if (user != null){
            if (user.isAnonymous()){
                btnDenunciar.setEnabled(false);
                btnDenunciar.setVisibility(View.GONE);
            }
        }
    }
//    private void CreateRecyclerAnuncio(Loja loja){
//        adapterFotoAnuncio = new AdapterFotoAnuncio(getApplicationContext(), loja);
//
//        recyclerAnuncio = findViewById(R.id.recycler_anuncios);
//        recyclerAnuncio.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(this);
//        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
//        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
//
//        recyclerAnuncio.setLayoutManager(layoutManager);
//        recyclerAnuncio.setAdapter(adapterFotoAnuncio);
//        recyclerAnuncio.addItemDecoration(
//                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//    }
}

