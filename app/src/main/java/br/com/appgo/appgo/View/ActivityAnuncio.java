package br.com.appgo.appgo.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.apache.commons.lang3.SerializationUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import br.com.appgo.appgo.Adapter.FotosAdapter;
import br.com.appgo.appgo.Controller.PermissionControl;
import br.com.appgo.appgo.Controller.UnmaskPhoneNumber;
import br.com.appgo.appgo.Model.Local;
import br.com.appgo.appgo.Model.Loja;
import br.com.appgo.appgo.R;
import static br.com.appgo.appgo.View.MainActivity.LOJA_ESCOLHIDA;
import static br.com.appgo.appgo.View.MainActivity.LOJA_ESCOLHIDA_ACTION;
import static br.com.appgo.appgo.View.MainActivity.RESULT_LATLNG_LOJA;

/**
 * Created by hex on 09/03/18.
 */

public class ActivityAnuncio extends AppCompatActivity implements View.OnClickListener {
    public static final String LATITUDE_LOJA = "latitude_loja";
    public static final String LONGITUDE_LOJA = "longitude_loja";
    public static final String LOJA_COMENT = "loja_comentada";
    public static final String EMAIL_APP_DENUNCIA = "appgo.website@gmail.com";
    Loja loja = null;
    List<String> urlFotos;
    TextView mAnuncioTitulo, curtidas, comentarios;
    Button btnEndereco, btnRamo, btnDenunciar;
    ImageView btnWhatsapp, btnTelefone, btnEmail;
    ImageButton curtir, comentar, compartilhar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    boolean curtidaToken = false;
    String fileShare;
    Uri bitmapUri;
    Bitmap bitmap = null;
    private PermissionControl control;

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
        btnDenunciar = (Button)findViewById(R.id.denuncia_button);
        btnDenunciar.setOnClickListener(this);
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
        btnTelefone = (ImageView) findViewById(R.id.image_telefone);
        btnTelefone.setOnClickListener(this);
        btnWhatsapp = (ImageView) findViewById(R.id.image_whatsapp);
        btnWhatsapp.setOnClickListener(this);
        btnEmail = (ImageView) findViewById(R.id.image_email);
        btnEmail.setOnClickListener(this);
        btnRamo = (Button)findViewById(R.id.anuncio_ramo);
        btnRamo.setText(loja.ramo);
        fileShare = null;
        bitmapUri = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        curtidas.setText(String.valueOf(getCurtidas()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.denuncia_button:
                Intent denunciaMail = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                denunciaMail.setType("text/plain");
                denunciaMail.putExtra(Intent.EXTRA_SUBJECT, "Denuncia de abuso de " + user.getDisplayName());
                //it.putExtra(Intent.EXTRA_TEXT, "Body of email");
                denunciaMail.setData(Uri.parse("mailto:" + EMAIL_APP_DENUNCIA)); // or just "mailto:" for blank
                denunciaMail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(denunciaMail);
                break;
            case R.id.comentar_anuncio:
                if (user != null){
                    Intent comentIntent = new Intent(this, ComentActivity.class);
                    comentIntent.putExtra(LOJA_COMENT, loja);
                    startActivity(comentIntent);
                }
                else {
                    Toast.makeText(this, "É preciso estar logado\n" +
                            "para fazer um comentário.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.anuncio_endereco:
                Intent intentRota = new Intent();
                intentRota.putExtra(LATITUDE_LOJA, loja.local);
                intentRota.putExtra(LONGITUDE_LOJA, loja.local.longitude);
                setResult(RESULT_LATLNG_LOJA, intentRota);
                finish();
                break;
            case R.id.image_telefone:
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
            case R.id.image_whatsapp:
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
                break;
            case R.id.image_email:
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
                shareImage(loja.urlFoto1);
                break;
        }
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
    public void shareImage(String url) {
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        try {
            final File file = File.createTempFile("foto",".png");
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                    fileShare = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
                    bitmapUri = Uri.parse(fileShare);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*, text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, loja.titulo + " esta no AppGo!");
        i.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        startActivity(Intent.createChooser(i, "Share Image"));

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

