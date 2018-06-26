package br.com.appgo.appgo.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.controller.CheckDigit;
import br.com.appgo.appgo.controller.CombineImages;
import br.com.appgo.appgo.controller.ResizePhoto;
import br.com.appgo.appgo.controller.SPreferences;
import br.com.appgo.appgo.fragment.DialogFragmentListRamo;
import br.com.appgo.appgo.model.Local;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.PhotoPicasso;
import br.com.appgo.appgo.persistence.UploaFile;
import br.com.appgo.appgo.services.LoadAnunciosData;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

import static br.com.appgo.appgo.constants.StringConstans.ANUNCIO_LOCAL;
import static br.com.appgo.appgo.services.LoadAnunciosData.LOJA_RECEIVE_DATA;
import static br.com.appgo.appgo.services.LoadAnunciosData.RECEIVER_DATA_ANUNCIO;

/**
 * Created by hex on 21/02/18.
 */

public class CriarAnuncioActivity extends AppCompatActivity implements View.OnClickListener,
        DialogFragmentListRamo.ChooseRamoDialogListener, ServiceConnection {

    public static final String FRAGMENT_RAMO_ATIVIDADE = "fragment_ramo_atividade";
    public static final String ANUNCIOS = "Anuncios";
    public static final int RESULT_FIND_ADDRESS = 70;
    public static final int REQUEST_ICONE = 22;
    public static final int REQUEST_FOTO_1 = 10;
//    public static final int REQUEST_FOTO_2 = 20;
//    public static final int REQUEST_FOTO_3 = 30;
    public static final String ADRESS_NAME = "adress_name";
    public static final String ADRESS_OBS = "adress_observation";
    public static final String ADRESS_LATITUDE = "adress_latitude";
    public static final String ADRESS_LONGITUDE = "adress_longitude";
    private static final int ICONE_URL = 100;
    private static final int FOTO1_URL = 101;
    private static final int FOTO2_URL = 102;
    private static final int FOTO3_URL = 103;
    private static final String FALHA_DOWNLOAD = "falha";
    public static final String TAG = "BOOMBOOMTESTE";
    private static final int REQUEST_FIND_ADDRESS = 101;
    private String docType = null;
    private ImageView /*buttonLoadIco,*/ mFoto1/*, mFoto2, mFoto3*/;
    private Button btnRamo, btnSalvar, addressName;
    private FloatingActionButton fltFoto1, fltFoto2, fltFoto3;
//    private RadioGroup docChoose;
//    private RadioButton radioCPF, radioCNPJ;
    private EditText nomeAnuncio, documento, whatsapp, telefone, email;
    SPreferences preferences;
    MaskEditTextChangedListener maskWhats, maskTel;
    Bitmap bitmapIcone, bitmapFoto1, bitmapFoto2, bitmapFoto3;
    boolean resultFoto1, resultFoto2, resultFoto3, resultIcone;

    Loja loja;
    Local local;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference reference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private IntentFilter intentFilterDataAnuncio;
    private Intent serviceDataReceiveAnuncio;
    PhotoPicasso picasso;
    UploaFile uploaFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_anuncio);

        picasso = new PhotoPicasso(getApplicationContext());
        reference = firebaseStorage.getReference();
        loja = new Loja();
        local = new Local();
        preferences = new SPreferences(getApplicationContext());
//        buttonLoadIco = findViewById(R.id.image_icone);
//        buttonLoadIco.setOnClickListener(this);
        fltFoto1 = findViewById(R.id.float_foto1);
        fltFoto1.setOnClickListener(this);
//        fltFoto2 = findViewById(R.id.float_foto2);
//        fltFoto2.setOnClickListener(this);
//        fltFoto3 = findViewById(R.id.float_foto3);
//        fltFoto3.setOnClickListener(this);
        mFoto1 = findViewById(R.id.foto1);
//        mFoto2 = findViewById(R.id.foto2);
//        mFoto3 = findViewById(R.id.foto3);
        nomeAnuncio = findViewById(R.id.nome_anuncio);
//        documento = findViewById(R.id.documento_anuncio);
        addressName = findViewById(R.id.adress_name);
        addressName.setOnClickListener(this);
        btnRamo = findViewById(R.id.btn_ramo);
        btnRamo.setOnClickListener(this);
        btnSalvar = findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(this);
        whatsapp = findViewById(R.id.whatsapp);
        maskWhats = new MaskEditTextChangedListener("(##)####-#####", whatsapp);
        whatsapp.addTextChangedListener(maskWhats);
        resultFoto1 = resultFoto2 = resultFoto3 = resultIcone = false;
        intentFilterDataAnuncio = new IntentFilter();
        intentFilterDataAnuncio.addAction(RECEIVER_DATA_ANUNCIO);
        serviceDataReceiveAnuncio = new Intent(this, LoadAnunciosData.class);

        resultFoto1 = resultFoto2 = resultFoto3 = resultIcone = false;
        telefone = findViewById(R.id.telefone);
        maskTel = new MaskEditTextChangedListener("(##)####-#####", telefone);
        telefone.addTextChangedListener(maskTel);
        email = findViewById(R.id.email_comercial);
//        docChoose = findViewById(R.id.radiogrup_escolhadocumento);
//        radioCPF = findViewById(R.id.cpf_enable);
//        radioCNPJ = findViewById(R.id.cnpj_enable);
        uploaFile = new UploaFile(this.getApplicationContext());
        startService(serviceDataReceiveAnuncio);
        registerReceiver(mReceiverDataAnuncio, intentFilterDataAnuncio);

//        docChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton checkedRadioButton = group.findViewById(checkedId);
//                boolean isChecked = checkedRadioButton.isChecked();
//                if (isChecked)
//                    docType = checkedRadioButton.getText().toString();
//            }
//        });

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiverDataAnuncio);
        stopService(serviceDataReceiveAnuncio);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.image_icone:
//                Intent intentIco = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intentIco, REQUEST_ICONE);
//                break;
            case R.id.adress_name:
                Intent intentFindAddress = new Intent(this, SearchOnMapActivity.class);
                startActivityForResult(intentFindAddress, REQUEST_FIND_ADDRESS);

                break;
            case R.id.float_foto1:
                Intent intentFoto1 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentFoto1, REQUEST_FOTO_1);
                break;
//            case R.id.float_foto2:
//                Intent intentFoto2 = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intentFoto2, REQUEST_FOTO_2);
//                break;
//            case R.id.float_foto3:
//                Intent intentFoto3 = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intentFoto3, REQUEST_FOTO_3);
//                break;
            case R.id.btn_ramo:
                DialogFragment dialogFragment = new DialogFragmentListRamo();
                dialogFragment.show(dialogCall(FRAGMENT_RAMO_ATIVIDADE), FRAGMENT_RAMO_ATIVIDADE);
                break;
            case R.id.btn_salvar:
                //Salva Anuncio Criado ou Edita.
                if (CriarLoja()){
                    database.child(ANUNCIOS).child(auth.getCurrentUser().getUid()).setValue(loja);
                    Toast.makeText(this, "Sua loja foi salva, encontre seu anuncio no mapa!"
                            , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, SplashScreen.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(this, "Problemas com conexão a internet ou ao banco de dados," +
                            "tente novamente.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void CreateStoreImages(Bitmap bitmapResouce){
        bitmapIcone = bitmapResouce;
        ResizePhoto resizePhoto = new ResizePhoto(bitmapIcone.getWidth(), bitmapIcone.getHeight(), 96);
        bitmapIcone = resizePhoto.resizeBitmapSquare(bitmapIcone);
        bitmapIcone = new CombineImages(getApplication()).createIco(bitmapIcone);
        resultIcone = true;
        bitmapFoto1 = bitmapResouce;
        ResizePhoto resize = new ResizePhoto(mFoto1.getWidth(), mFoto1.getHeight(), 0);
        mFoto1.setImageBitmap(resize.ResizeToImage(mFoto1, bitmapFoto1));
        resultFoto1 = true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case REQUEST_ICONE:
//                if (resultCode == RESULT_OK) {
//                    Uri targetUri = data.getData();
//                    try {
//                        bitmapIcone = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
//                        ResizePhoto resizePhoto = new ResizePhoto(bitmapIcone.getWidth(), bitmapIcone.getHeight(), 96);
//                        bitmapIcone = resizePhoto.resizeBitmapSquare(bitmapIcone);
//                        bitmapIcone = new CombineImages(getApplication()).createIco(bitmapIcone);
//                        buttonLoadIco.setImageBitmap(bitmapIcone);
//                        resultIcone = true;
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//                break;
            case REQUEST_FOTO_1:
                if (resultCode == RESULT_OK) {
                    Uri targetUriFoto1 = data.getData();
                    try {
                        CreateStoreImages(BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto1)));
//                        bitmapFoto1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto1));
//                        ResizePhoto resize = new ResizePhoto(mFoto1.getWidth(), mFoto1.getHeight(), 0);
//                        mFoto1.setImageBitmap(resize.ResizeToImage(mFoto1, bitmapFoto1));
//                        resultFoto1 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
//            case REQUEST_FOTO_2:
//                if (resultCode == RESULT_OK) {
//                    Uri targetUriFoto2 = data.getData();
//                    try {
//                        bitmapFoto2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto2));
//                        ResizePhoto resize = new ResizePhoto(mFoto2.getWidth(), mFoto2.getHeight(), 0);
//                        mFoto2.setImageBitmap(resize.ResizeToImage(mFoto2, bitmapFoto2));
//                        resultFoto2 = true;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            case REQUEST_FOTO_3:
//                if (resultCode == RESULT_OK) {
//                    Uri targetUriFoto3 = data.getData();
//                    try {
//                        bitmapFoto3 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto3));
//                        ResizePhoto resize = new ResizePhoto(mFoto3.getWidth(), mFoto3.getHeight(), 0);
//                        mFoto3.setImageBitmap(resize.ResizeToImage(mFoto3, bitmapFoto3));
//                        resultFoto3 = true;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
            case REQUEST_FIND_ADDRESS:
                if (resultCode == RESULT_FIND_ADDRESS){
                    try {
                        Local tmpLocal = new Local(
                                data.getStringExtra(ADRESS_NAME),
                                data.getStringExtra(ADRESS_OBS),
                                data.getDoubleExtra(ADRESS_LATITUDE, 0.0f),
                                data.getDoubleExtra(ADRESS_LONGITUDE, 0.0f)
                                );
                        loja.local = tmpLocal;
                        addressName.setText(tmpLocal.endereco);
                        addressName.setBackgroundColor(getResources().getColor(R.color.green_confirm));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        String errorText = "Endereço inválido!";
                        addressName.setText(String.format("%s", errorText));
                        addressName.setBackgroundColor(getResources().getColor(R.color.error));
                    }

                }
                break;
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

    private boolean CriarLoja(){
        boolean requestToken = false;
        String message = "ATENÇÂO!!!\n";
        CheckDigit checkDigit = new CheckDigit();
        //Pegando o nome do Anuncio
        if (nomeAnuncio.getText().toString().isEmpty()
                ||nomeAnuncio.getText().toString().equals("")){
            message += "Você precisa de um título para seu anúncio!\n";
            nomeAnuncio.setBackgroundColor(getResources().getColor(R.color.error));
            requestToken = false;
        }
        else{
            loja.titulo = nomeAnuncio.getText().toString();
            nomeAnuncio.setBackgroundColor(getResources().getColor(R.color.colorCalmBlue));
            requestToken = true;
        }
        if (loja.local.endereco.equals("")){
            message += "Clique no endereço BUSCAR ENDEREÇO para selecinar o endereço do seu anúncio.\n";
            addressName.setBackgroundColor(getResources().getColor(R.color.error));
            requestToken = false;
        }
        else{
            addressName.setBackgroundColor(getResources().getColor(R.color.colorCalmBlue));
            requestToken = true;
        }
        // Pegando o Documento ligado ao anuncio
//        if (documento.getText().toString().isEmpty()){
//            message += "Digite um Documento válido para seu Anúncio\n";
//            documento.setBackgroundColor(getResources().getColor(R.color.error));
//            token=false;
//        }
//        else{
//            if (docType == null){
//                message += "Escolha um tipo de Documento\n";
//                docChoose.setBackgroundColor(getResources().getColor(R.color.error));
//                token = false;
//            }
//            else {
//                docChoose.setBackgroundColor(getResources().getColor(R.color.colorCalmBlue));
//                switch (docType){
//                    case "CPF":
//                        if (CheckDigit.eValidoCPF(documento.getText().toString())){
//                            loja.documento.documento = documento.getText().toString();
//                            documento.setBackgroundColor(getResources().getColor(R.color.colorCalmBlue));
//                            loja.documento.tipoDocumento = "CPF";
//                        }
//                        else {
//                            message += "CPF inválido\n";
//                            documento.setBackgroundColor(getResources().getColor(R.color.colorCalmBlue));
//                            token = false;
//                        }
//                        break;
//                    case "CNPJ":
//                        if (eValidoCNPJ(documento.getText().toString())){
//                            loja.documento.documento = documento.getText().toString();
//                            documento.setBackgroundColor(getResources().getColor(R.color.colorCalmBlue));
//                            loja.documento.tipoDocumento = "CNPJ";
//                        }
//                        else {
//                            message += "CNPJ inválido\n";
//                            documento.setBackgroundColor(getResources().getColor(R.color.colorCalmBlue));
//                            token = false;
//                        }
//                        break;
//                }
//            }
//        }
        //Grava Anuncio Na Base de Dados;
        if (requestToken){
            FirebaseUser user = auth.getCurrentUser();
            loja.titulo = nomeAnuncio.getText().toString();
            loja.emailAnuncio = email.getText().toString();
            loja.ramo = btnRamo.getText().toString();
            loja.telefone = telefone.getText().toString();
            loja.whatsapp = whatsapp.getText().toString();
            loja.anunciante = user.getUid();
            loja.urlIcone = uploaFile.UploadPhoto(bitmapIcone, "icone.png", reference, user.getUid());
            loja.urlFoto1 = uploaFile.UploadPhoto(bitmapFoto1, "foto_1", reference, user.getUid());
//            loja.urlFoto2 = uploaFile.UploadPhoto(bitmapFoto2, "foto_2", reference, user.getUid());
//            loja.urlFoto3 = uploaFile.UploadPhoto(bitmapFoto3, "foto_3", reference, user.getUid());
       }
        else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        return requestToken;
    }

    private BroadcastReceiver mReceiverDataAnuncio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == RECEIVER_DATA_ANUNCIO) {
                loja = (Loja) intent.getSerializableExtra(LOJA_RECEIVE_DATA);
                if (loja != null){
                    SetLoja(loja);
                }
                else {
                    loja = new Loja();
                }
            }
        }
    };

    private void setPhoto(/*String url_icone,*/ String url_foto1) {
        PhotoPicasso picasso = new PhotoPicasso(getApplicationContext());
//        picasso.PhotoOrigSize(url_icone, buttonLoadIco, !resultIcone);
        picasso.PhotoBorderless(url_foto1, mFoto1, !resultFoto1);
//        picasso.PhotoBorderless(url_foto2, mFoto2, !resultFoto2);
//        picasso.PhotoBorderless(url_foto3, mFoto3, !resultFoto3);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ANUNCIO_LOCAL, loja);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loja = (Loja) savedInstanceState.getSerializable(ANUNCIO_LOCAL);
        if (loja != null){
            SetLoja(loja);
        }
        else {
            loja = new Loja();
        }
    }
    void SetLoja(Loja loja){
        nomeAnuncio.setText(loja.titulo);
//        if (loja.documento.tipoDocumento.equals("CPF"))
//            radioCPF.setChecked(true);
//        if (loja.documento.tipoDocumento.equals("CNPJ"))
//            radioCNPJ.setChecked(true);
//        documento.setText(loja.documento.documento);
        btnRamo.setText(loja.ramo);
        addressName.setText(loja.local.endereco);
        whatsapp.setText(loja.whatsapp);
        telefone.setText(loja.telefone);
        email.setText(loja.emailAnuncio);
        String url_icone = loja.urlIcone;
        String url_foto1 = loja.urlFoto1;
//        String url_foto2 = loja.urlFoto2;
//        String url_foto3 = loja.urlFoto3;
        setPhoto(/*url_icone,*/ url_foto1);
    }
}