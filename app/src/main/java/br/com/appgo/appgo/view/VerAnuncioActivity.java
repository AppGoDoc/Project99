package br.com.appgo.appgo.view;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.SerializationUtils;

import java.net.URLEncoder;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.adapter.AdapterFotoAnuncio;
import br.com.appgo.appgo.controller.UnmaskPhoneNumber;
import br.com.appgo.appgo.model.Loja;

import static br.com.appgo.appgo.view.ActivityAnuncio.USER_LOJA;
import static br.com.appgo.appgo.view.ActivityAnuncio.USER_LOJA_ACTION;

public class VerAnuncioActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerAnuncio;
    AdapterFotoAnuncio adapterFotoAnuncio;
    RecyclerView.LayoutManager layoutManager;
    ImageButton mWhatsapp, mPhone, mEmail;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    Loja loja;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_anuncio);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        loja = RequestLoja();
        mWhatsapp = findViewById(R.id.btn_whatsapp);
        mWhatsapp.setOnClickListener(this);
        mEmail = findViewById(R.id.btn_email);
        mEmail.setOnClickListener(this);
        mPhone = findViewById(R.id.btn_phone);
        mPhone.setOnClickListener(this);
//        Chama o metodo que cria o recycler
        CreateRecyclerAnuncio(loja);
    }

    private void CreateRecyclerAnuncio(Loja loja){
        adapterFotoAnuncio = new AdapterFotoAnuncio(getApplicationContext(), loja);

        recyclerAnuncio = findViewById(R.id.recycler_anuncios);
        recyclerAnuncio.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerAnuncio.setLayoutManager(layoutManager);
        recyclerAnuncio.setAdapter(adapterFotoAnuncio);
//        recyclerAnuncio.addItemDecoration(
//                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

        private Loja RequestLoja() {
        Loja loja = null;
        Intent intent = getIntent();
        if (intent.getAction().equals(USER_LOJA_ACTION)){
            byte[] data = intent.getByteArrayExtra(USER_LOJA);
            loja = SerializationUtils.deserialize(data);
        }
        return loja;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_whatsapp:
                WhatsappCall();
                break;
            case R.id.btn_phone:
                TelephoneCall();
                break;
            case R.id.btn_email:
                EmailCall();
                break;
        }
    }

    private void EmailCall() {
        Intent it = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        it.setType("text/plain");
        it.putExtra(Intent.EXTRA_SUBJECT, "Cliente AppGo! " + firebaseUser.getDisplayName());
        //it.putExtra(Intent.EXTRA_TEXT, "Body of email");
        it.setData(Uri.parse("mailto:" + loja.emailAnuncio)); // or just "mailto:" for blank
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(it);
    }

    private void TelephoneCall() {
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

    private void WhatsappCall() {
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
}
