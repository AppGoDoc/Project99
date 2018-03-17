package br.com.appgo.appgo.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.SerializationUtils;

import java.util.LinkedList;
import java.util.List;

import br.com.appgo.appgo.Adapter.FotosAdapter;
import br.com.appgo.appgo.Controller.UnmaskPhoneNumber;
import br.com.appgo.appgo.Model.Loja;
import br.com.appgo.appgo.R;

import static br.com.appgo.appgo.View.MainActivity.LOJA_ESCOLHIDA;
import static br.com.appgo.appgo.View.MainActivity.LOJA_ESCOLHIDA_ACTION;

/**
 * Created by hex on 09/03/18.
 */

public class ActivityAnuncio extends AppCompatActivity implements View.OnClickListener {
    Loja loja = null;
    List<String> urlFotos;
    TextView mAnuncioTitulo, curtidas;
    Button btnEndereco, btnWhatsapp, btnTelefone, btnEmail, btnRamo;
    ImageButton curtir;
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
        curtidas = (TextView)findViewById(R.id.contagem_curtir);
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

    private int getCurtidas() {
        if (user == null){
            curtidas.setEnabled(false);
        }
        else{
            curtidas.setEnabled(true);
            for (int i = 0; i<=loja.curtidas.size()-1;i++){
                if (loja.curtidas.get(i) == user.getUid()){
                    Log.d("Curtida", loja.curtidas.get(i));
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
            for (int i = 0; i<=loja.curtidas.size()-1; i++){
                if (loja.curtidas.get(i) == user.getUid()){
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
        curtidas.setText(String.valueOf(loja.curtidas.size()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.anuncio_endereco:
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
                try{
                    UnmaskPhoneNumber unmask = new UnmaskPhoneNumber();
                    Uri uri = Uri.parse("smsto:" + unmask.whatsNumber(loja.whatsapp));
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
                catch (Exception e){
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
                getCurtidas();
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
        finish();
    }
}
