package br.com.appgo.appgo.View;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.com.appgo.appgo.Controller.CheckDigit;
import br.com.appgo.appgo.Controller.ResizePhoto;
import br.com.appgo.appgo.Controller.SPreferences;
import br.com.appgo.appgo.Fragment.DialogFragmentListRamo;
import br.com.appgo.appgo.Model.Local;
import br.com.appgo.appgo.Model.Loja;
import br.com.appgo.appgo.Persistence.FireBase;
import br.com.appgo.appgo.R;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

/**
 * Created by hex on 21/02/18.
 */

public class CriarAnuncioActivity extends AppCompatActivity implements View.OnClickListener,
        DialogFragmentListRamo.ChooseRamoDialogListener {

    private static final String FRAGMENT_RAMO_ATIVIDADE = "fragment_ramo_atividade";
    public static final String ANUNCIOS = "Anuncios";
    public static final int RESULT_FIND_ADDRESS = 70;
    public static final int RESULT_ICONE = 22;
    public static final int RESULT_FOTO_1 = 10;
    public static final int RESULT_FOTO_2 = 20;
    public static final int RESULT_FOTO_3 = 30;
    public static final String ADRESS_NAME = "adress_name";
    public static final String ADRESS_OBS = "adress_observation";
    public static final String ADRESS_LATITUDE = "adress_latitude";
    public static final String ADRESS_LONGITUDE = "adress_longitude";
    private String docType = null;
    private ImageView buttonLoadIco, mFoto1, mFoto2, mFoto3;
    private Button btnRamo, btnFindAddress, btnSalvar;
    private RadioGroup docChoose;
    private EditText nomeAnuncio, documento, whatsapp, telefone, email, addressName;
    private ProgressBar progressBar;
    SPreferences preferences;
    MaskEditTextChangedListener maskWhats, maskTel;
    Bitmap bitmapIcone, bitmapFoto1, bitmapFoto2, bitmapFoto3;
    Loja loja;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_anuncio);

        reference = firebaseStorage.getReference();
        loja = new Loja();
        preferences = new SPreferences(getApplicationContext());
        buttonLoadIco = (ImageView) findViewById(R.id.image_icone);
        buttonLoadIco.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_upload);
        progressBar.setVisibility(View.GONE);
        mFoto1 = (ImageView) findViewById(R.id.foto1);
        mFoto1.setOnClickListener(this);
        mFoto2 = (ImageView) findViewById(R.id.foto2);
        mFoto2.setOnClickListener(this);
        mFoto3 = (ImageView) findViewById(R.id.foto3);
        mFoto3.setOnClickListener(this);
        nomeAnuncio = (EditText) findViewById(R.id.nome_anuncio);
        documento = (EditText) findViewById(R.id.documento_anuncio);
        addressName = (EditText) findViewById(R.id.adress_name);
        btnRamo = (Button)findViewById(R.id.btn_ramo);
        btnRamo.setOnClickListener(this);
        btnFindAddress = (Button) findViewById(R.id.btn_find_address);
        btnFindAddress.setOnClickListener(this);
        btnSalvar = (Button) findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(this);
        whatsapp = (EditText)findViewById(R.id.whatsapp);
        maskWhats = new MaskEditTextChangedListener("(##)####-#####", whatsapp);

        telefone = (EditText)findViewById(R.id.telefone);
        maskTel = new MaskEditTextChangedListener("(##)####-#####", telefone);
        email = (EditText) findViewById(R.id.email_comercial);
        docChoose = (RadioGroup) findViewById(R.id.radiogrup_escolhadocumento);

        docChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                    docType = checkedRadioButton.getText().toString();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_icone:
                Intent intentIco = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentIco, RESULT_ICONE);
                break;
            case R.id.foto1:
                Intent intentFoto1 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentFoto1, RESULT_FOTO_1);
                break;
            case R.id.foto2:
                Intent intentFoto2 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentFoto2, RESULT_FOTO_2);
                break;
            case R.id.foto3:
                Intent intentFoto3 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentFoto3, RESULT_FOTO_3);
                break;
            case R.id.btn_ramo:
                DialogFragment dialogFragment = new DialogFragmentListRamo();
                dialogFragment.show(dialogCall(FRAGMENT_RAMO_ATIVIDADE), FRAGMENT_RAMO_ATIVIDADE);
                break;
            case R.id.btn_find_address:
                if (addressName.getText().toString().isEmpty()){
                    Toast.makeText(this, "Digite um endereço para pesquisa.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentFindAddress = new Intent(this, SearchOnMapActivity.class);
                    intentFindAddress.putExtra(ADRESS_NAME, addressName.getText().toString());
                    startActivityForResult(intentFindAddress, RESULT_FIND_ADDRESS);
                }
                break;
            case R.id.btn_salvar:
                //Salva Anuncio Criado ou Edita.
                if (CriarLoja()){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    if (bitmapIcone != null)
                    UploadPhoto(bitmapIcone, "icone carregado com sucesso", auth, "icone.jpg");

                    database.child(ANUNCIOS).child(auth.getUid()).setValue(loja);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_ICONE:
                if (resultCode == RESULT_OK) {
                    Uri targetUri = data.getData();
                    try {
                        Bitmap bitMoldura = BitmapFactory.decodeResource(
                                getApplicationContext().getResources(),
                                R.drawable.marker_appgo);
                        bitmapIcone = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                        ResizePhoto resizePhoto = new ResizePhoto(bitmapIcone.getWidth(), bitmapIcone.getHeight(), 96);
                        bitmapIcone = Bitmap.createScaledBitmap(bitmapIcone, resizePhoto.widhtResize(),
                                resizePhoto.widhtResize(), true);
                        bitmapIcone = combineImages(bitmapIcone, bitMoldura);
                        buttonLoadIco.setImageBitmap(bitmapIcone);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case RESULT_FOTO_1:
                if (resultCode == RESULT_OK) {
                    Uri targetUriFoto1 = data.getData();
                    try {
                        bitmapFoto1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto1));
                        mFoto1.setImageBitmap(bitmapFoto1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case RESULT_FOTO_2:
                if (resultCode == RESULT_OK) {
                    Uri targetUriFoto2 = data.getData();
                    try {
                        bitmapFoto2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto2));
                        mFoto2.setImageBitmap(bitmapFoto2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case RESULT_FOTO_3:
                if (resultCode == RESULT_OK) {
                    Uri targetUriFoto3 = data.getData();
                    try {
                        bitmapFoto3 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto3));
                        mFoto3.setImageBitmap(bitmapFoto3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        if (resultCode == RESULT_FIND_ADDRESS){
            Local tmpLocal = new Local(data.getStringExtra(ADRESS_NAME),
                                    data.getStringExtra(ADRESS_OBS),
                                    data.getDoubleExtra(ADRESS_LATITUDE, 1),
                                    data.getDoubleExtra(ADRESS_LONGITUDE, 0));
            loja.local = tmpLocal;
            addressName.setText(loja.local.latitude + " ");
        }

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
    public void onFinishDialogFragment(String opcaoEscolhida) {
        btnRamo.setText(opcaoEscolhida);
    }

    public Bitmap combineImages(Bitmap bitmap1, Bitmap bitmap2) {

        Bitmap bit1 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(),
                                            null, true);
        Bitmap cs = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(bit1, 0.0f, 0.0f, null);
        comboImage.drawBitmap(bitmap1, 15.0f, 12.5f, null);
        return cs;
    }

    private boolean CriarLoja(){
        boolean token = true;
        String message = "ATENÇÂO!!!\n";
        CheckDigit checkDigit = new CheckDigit();
        //Pegando o nome do Anuncio
        if (nomeAnuncio.getText().toString().isEmpty()){
            message += "Digite de um título para seu anúncio!\n";
            nomeAnuncio.setBackgroundColor(getResources().getColor(R.color.error));
            token = false;
        }
        else{
            loja.titulo = nomeAnuncio.getText().toString();
            nomeAnuncio.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
        }
        // Pegando o documento ligado ao anuncio
        if (documento.getText().toString().isEmpty()){
            message += "Digite um documento válido para seu Anúncio\n";
            documento.setBackgroundColor(getResources().getColor(R.color.error));
            token=false;
        }
        else{
            if (docType == null){
                message += "Escolha um tipo de Documento\n";
                docChoose.setBackgroundColor(getResources().getColor(R.color.error));
                token = false;
            }
            else {
                docChoose.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                switch (docType){
                    case "CPF":
                        if (checkDigit.eValidoCPF(documento.getText().toString())){
                            loja.cpf = documento.getText().toString();
                            documento.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                        }
                        else {
                            message += "CPF inválido\n";
                            documento.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                            token = false;
                        }
                        break;
                    case "CNPJ":
                        if (checkDigit.eValidoCNPJ(documento.getText().toString())){
                            loja.cnpj = documento.getText().toString();
                            documento.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                        }
                        else {
                            message += "CNPJ inválido\n";
                            documento.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                            token = false;
                        }
                        break;
                }
            }
        }
        //Grava Anuncio Na Base de Dados;
        if (token){
            loja.emailAnuncio = email.getText().toString();
            if (btnRamo.getText().toString() != "Qual a sua atividade?"){
                loja.ramo = btnRamo.getText().toString();
            }
            loja.telefone = telefone.getText().toString();
            loja.whatsapp = whatsapp.getText().toString();
        }
        else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        return token;
    }
    public void UploadPhoto(Bitmap bitmap, final String message, FirebaseAuth auth, String fileName){
        progressBar.setVisibility(View.VISIBLE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] dataByte = stream.toByteArray();

        StorageReference referenceIcone = reference.child(auth.getUid()).child("fotos").child(fileName);
        UploadTask uploadTask = referenceIcone.putBytes(dataByte);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CriarAnuncioActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}