package br.com.appgo.appgo.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import br.com.appgo.appgo.Adapter.FotosAdapter;
import br.com.appgo.appgo.Controller.UnmaskPhoneNumber;
import br.com.appgo.appgo.Model.Local;
import br.com.appgo.appgo.Model.Loja;
import br.com.appgo.appgo.R;

import static br.com.appgo.appgo.View.MainActivity.LATLNG_LOJA;
import static br.com.appgo.appgo.View.MainActivity.LOJA_ESCOLHIDA;
import static br.com.appgo.appgo.View.MainActivity.LOJA_ESCOLHIDA_ACTION;
import static br.com.appgo.appgo.View.MainActivity.RESULT_LATLNG_LOJA;

/**
 * Created by hex on 09/03/18.
 */

public class ActivityAnuncio extends AppCompatActivity implements View.OnClickListener {
    public static final String LATITUDE_LOJA = "latitude_loja";
    public static final String LONGITUDE_LOJA = "longitude_loja";
    Loja loja = null;
    List<String> urlFotos;
    TextView mAnuncioTitulo, curtidas, comentarios;
    Button btnEndereco, btnWhatsapp, btnTelefone, btnEmail, btnRamo;
    ImageButton curtir, comentar, compartilhar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    boolean curtidaToken = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);

        loja = RequestLoja();
        urlFotos = GetListFotos(loja);
        HorizontalInfiniteCycleViewPager carroussel =
                (HorizontalInfiniteCycleViewPager) findViewById(R.id.carroussel_fotos);
        FotosAdapter fotosAdapter = new FotosAdapter(urlFotos, getApplicationContext());
        carroussel.setAdapter(fotosAdapter);
        compartilhar = (ImageButton)findViewById(R.id.compartilhar_anuncio);
        compartilhar.setOnClickListener(this);
        comentarios = (TextView)findViewById(R.id.contagem_comentar);
        comentar = (ImageButton)findViewById(R.id.comentar_anuncio);
        comentar.setOnClickListener(this);
        curtidas = (TextView)findViewById(R.id.contagem_curtir);
        curtidas.setText(String.valueOf(getCurtidas()));
        curtir = (ImageButton)findViewById(R.id.curtir_anuncio);
        curtir.setOnClickListener(this);
        mAnuncioTitulo = (TextView)findViewById(R.id.titulo_anuncio);
        mAnuncioTitulo.setText(loja.titulo);
        btnEndereco = (Button)findViewById(R.id.anuncio_endereco);
        btnEndereco.setText(loja.local.endereco);
        btnEndereco.setOnClickListener(this);
        btnTelefone = (Button)findViewById(R.id.anuncio_telefone);
        btnTelefone.setText(loja.telefone);
        btnTelefone.setOnClickListener(this);
        btnWhatsapp = (Button)findViewById(R.id.anuncio_whatsapp);
        btnWhatsapp.setText(loja.whatsapp);
        btnWhatsapp.setOnClickListener(this);
        btnEmail = (Button) findViewById(R.id.anuncio_email);
        btnEmail.setText(loja.emailAnuncio);
        btnEmail.setOnClickListener(this);
        btnRamo = (Button) findViewById(R.id.anuncio_ramo);
        btnRamo.setText(loja.ramo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        curtidas.setText(String.valueOf(getCurtidas()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comentar_anuncio:

                break;
            case R.id.anuncio_endereco:
                Intent intentRota = new Intent();
                intentRota.putExtra(LATITUDE_LOJA, loja.local.latitude);
                intentRota.putExtra(LONGITUDE_LOJA, loja.local.longitude);
                setResult(RESULT_LATLNG_LOJA, intentRota);
                finish();
                break;
            case R.id.anuncio_telefone:
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
                break;
            case R.id.anuncio_whatsapp:
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
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.anuncio_email:
                Intent it = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                it.setType("text/plain");
                it.putExtra(Intent.EXTRA_SUBJECT, "Cliente AppGo! " + user.getDisplayName());
                //it.putExtra(Intent.EXTRA_TEXT, "Body of email");
                it.setData(Uri.parse("mailto:" + loja.emailAnuncio)); // or just "mailto:" for blank
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(it);
                break;
            case R.id.curtir_anuncio:
                setCurtidas();
                break;
            case R.id.compartilhar_anuncio:
                shareImage("https://content.linkedin.com/content/dam/me/learning/blog/2017/Junepics/Money.jpg");
                break;
        }
    }

    private Loja RequestLoja() {
        Loja loja = null;
        Intent intent = getIntent();
        if (intent.getAction() == LOJA_ESCOLHIDA_ACTION){
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void shareImage(String url) {
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*, text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, loja.titulo + " esta no AppGo!");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    private int getCurtidas() {
        if (user == null){
            curtidas.setEnabled(false);
        }
        else {
            curtidas.setEnabled(true);
            for (int i = 0; i < loja.curtidas.size(); ++i) {
                if (loja.curtidas.get(i).equals(user.getUid())) {
                    userAlreadyLike();
                }
            }
        }
        return loja.curtidas.size();
    }

    private void userAlreadyLike() {
        curtidas.setTextColor(getResources().getColor(R.color.com_facebook_messenger_blue));
        curtidaToken = true;
    }

    private void setCurtidas(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Anuncios/" + loja.anunciante);
        if (curtidaToken){
            for (int i = 0; i<loja.curtidas.size(); i++){
                if (loja.curtidas.get(i).equals(user.getUid())){
                    loja.curtidas.remove(i);
                }
                curtidas.setTextColor(getResources().getColor(R.color.black));
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

}

