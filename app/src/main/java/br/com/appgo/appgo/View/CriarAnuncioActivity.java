package br.com.appgo.appgo.View;

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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.Serializable;
import br.com.appgo.appgo.Controller.CheckDigit;
import br.com.appgo.appgo.Controller.CombineImages;
import br.com.appgo.appgo.Persistence.PhotoPicasso;
import br.com.appgo.appgo.Controller.ResizePhoto;
import br.com.appgo.appgo.Controller.SPreferences;
import br.com.appgo.appgo.Persistence.UploaFile;
import br.com.appgo.appgo.Fragment.DialogFragmentListRamo;
import br.com.appgo.appgo.Model.Local;
import br.com.appgo.appgo.Model.Loja;
import br.com.appgo.appgo.R;
import br.com.appgo.appgo.Services.LoadAnunciosData;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_DOCUMENTO;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_EMAIL;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_ENDERECO;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_LOCAL;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_NOME;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_RAMO;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_TELEFONE;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_TIPO_DOCUMENTO;
import static br.com.appgo.appgo.Constants.StringConstans.ANUNCIO_WHATSAPP;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_DOCUMENTO;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_EMAIL;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_FOTO1_URL;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_FOTO2_URL;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_FOTO3_URL;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_ICONE_URL;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_RAMO;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_RECEIVE_DATA;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_TELEFONE;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_TIPO_DOCUMENTO;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_TITULO;
import static br.com.appgo.appgo.Services.LoadAnunciosData.LOJA_WHATSAPP;
import static br.com.appgo.appgo.Services.LoadAnunciosData.RECEIVER_DATA_ANUNCIO;

/**
 * Created by hex on 21/02/18.
 */

public class CriarAnuncioActivity extends AppCompatActivity implements View.OnClickListener,
        DialogFragmentListRamo.ChooseRamoDialogListener, ServiceConnection {

    private static final String FRAGMENT_RAMO_ATIVIDADE = "fragment_ramo_atividade";
    public static final String ANUNCIOS = "Anuncios";
    public static final int RESULT_FIND_ADDRESS = 70;
    public static final int REQUEST_ICONE = 22;
    public static final int REQUEST_FOTO_1 = 10;
    public static final int REQUEST_FOTO_2 = 20;
    public static final int REQUEST_FOTO_3 = 30;
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
    private ImageView buttonLoadIco, mFoto1, mFoto2, mFoto3;
    private Button btnRamo, btnFindAddress, btnSalvar;
    private RadioGroup docChoose;
    private RadioButton radioCPF, radioCNPJ;
    private EditText nomeAnuncio, documento, whatsapp, telefone, email, addressName;
    ProgressBar progressBar1, progressBar2, progressBar3, progressBarIcone;
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
        buttonLoadIco = (ImageView) findViewById(R.id.image_icone);
        buttonLoadIco.setOnClickListener(this);
        progressBar1 = (ProgressBar) findViewById(R.id.progress_bar_1);
        progressBar1.setVisibility(View.GONE);
        progressBar2 = (ProgressBar) findViewById(R.id.progress_bar_2);
        progressBar2.setVisibility(View.GONE);
        progressBar3 = (ProgressBar)findViewById(R.id.progress_bar_3);
        progressBar3.setVisibility(View.GONE);
        progressBarIcone = (ProgressBar) findViewById(R.id.progress_bar_icone);
        progressBarIcone.setVisibility(View.GONE);
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
        whatsapp.addTextChangedListener(maskWhats);
        resultFoto1 = resultFoto2 = resultFoto3 = resultIcone = false;

        intentFilterDataAnuncio = new IntentFilter();
        intentFilterDataAnuncio.addAction(RECEIVER_DATA_ANUNCIO);
        serviceDataReceiveAnuncio = new Intent(this, LoadAnunciosData.class);

        resultFoto1 = resultFoto2 = resultFoto3 = resultIcone = false;
        telefone = (EditText)findViewById(R.id.telefone);
        maskTel = new MaskEditTextChangedListener("(##)####-#####", telefone);
        telefone.addTextChangedListener(maskTel);
        email = (EditText) findViewById(R.id.email_comercial);
        docChoose = (RadioGroup) findViewById(R.id.radiogrup_escolhadocumento);
        radioCPF = (RadioButton)findViewById(R.id.cpf_enable);
        radioCNPJ = (RadioButton) findViewById(R.id.cnpj_enable);
        uploaFile = new UploaFile(this.getApplicationContext());
        startService(serviceDataReceiveAnuncio);
        registerReceiver(mReceiverDataAnuncio, intentFilterDataAnuncio);

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
    protected void onResume() {
        super.onResume();
//        startService(serviceDataReceiveAnuncio);
//        registerReceiver(mReceiverDataAnuncio, intentFilterDataAnuncio);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mReceiverDataAnuncio);
//        stopService(serviceDataReceiveAnuncio);
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
            case R.id.image_icone:
                Intent intentIco = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentIco, REQUEST_ICONE);
                break;
            case R.id.foto1:
                Intent intentFoto1 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentFoto1, REQUEST_FOTO_1);
                break;
            case R.id.foto2:
                Intent intentFoto2 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentFoto2, REQUEST_FOTO_2);
                break;
            case R.id.foto3:
                Intent intentFoto3 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentFoto3, REQUEST_FOTO_3);
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
                    startActivityForResult(intentFindAddress, REQUEST_FIND_ADDRESS);
                }
                break;
            case R.id.btn_salvar:
                //Salva Anuncio Criado ou Edita.
                if (CriarLoja()){
                    database.child(ANUNCIOS).child(auth.getUid()).setValue(loja);
                    Intent intent = new Intent(this, SplashScreen.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ICONE:
                if (resultCode == RESULT_OK) {
                    Uri targetUri = data.getData();
                    try {
                        bitmapIcone = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                        ResizePhoto resizePhoto = new ResizePhoto(bitmapIcone.getWidth(), bitmapIcone.getHeight(), 96);
                        bitmapIcone = resizePhoto.resizeBitmapSquare(bitmapIcone);
                        bitmapIcone = new CombineImages(getApplication()).createIco(bitmapIcone);
                        buttonLoadIco.setImageBitmap(bitmapIcone);
                        resultIcone = true;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_FOTO_1:
                if (resultCode == RESULT_OK) {
                    Uri targetUriFoto1 = data.getData();
                    try {
                        bitmapFoto1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto1));
                        ResizePhoto resizePhoto = new ResizePhoto(bitmapFoto1.getWidth(),bitmapFoto1.getHeight(),512);
                        bitmapFoto1 = resizePhoto.resizeBitmap(bitmapFoto1);
                        mFoto1.setImageBitmap(bitmapFoto1);
                        resultFoto1 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_FOTO_2:
                if (resultCode == RESULT_OK) {
                    Uri targetUriFoto2 = data.getData();
                    try {
                        bitmapFoto2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto2));
                        ResizePhoto resizePhoto = new ResizePhoto(bitmapFoto2.getWidth(),bitmapFoto2.getHeight(),512);
                        bitmapFoto2 = resizePhoto.resizeBitmap(bitmapFoto2);
                        mFoto2.setImageBitmap(bitmapFoto2);
                        resultFoto2 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_FOTO_3:
                if (resultCode == RESULT_OK) {
                    Uri targetUriFoto3 = data.getData();
                    try {
                        bitmapFoto3 = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto3));
                        ResizePhoto resizePhoto = new ResizePhoto(bitmapFoto3.getWidth(),bitmapFoto3.getHeight(),512);
                        bitmapFoto3 = resizePhoto.resizeBitmap(bitmapFoto3);
                        mFoto3.setImageBitmap(bitmapFoto3);
                        resultFoto3 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_FIND_ADDRESS:
                if (resultCode == RESULT_FIND_ADDRESS){
                    loja.local = new Local(data.getStringExtra(ADRESS_NAME),
                                    data.getStringExtra(ADRESS_OBS),
                                    data.getDoubleExtra(ADRESS_LATITUDE, 1),
                                    data.getDoubleExtra(ADRESS_LONGITUDE, 0));
                    addressName.setText(loja.local.endereco);
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
        // Pegando o Documento ligado ao anuncio
        if (documento.getText().toString().isEmpty()){
            message += "Digite um Documento válido para seu Anúncio\n";
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
                            loja.documento.documento = documento.getText().toString();
                            documento.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                            loja.documento.tipoDocumento = "CPF";
                        }
                        else {
                            message += "CPF inválido\n";
                            documento.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                            token = false;
                        }
                        break;
                    case "CNPJ":
                        if (checkDigit.eValidoCNPJ(documento.getText().toString())){
                            loja.documento.documento = documento.getText().toString();
                            documento.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                            loja.documento.tipoDocumento = "CNPJ";
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
            loja.ramo = btnRamo.getText().toString();
            loja.telefone = telefone.getText().toString();
            loja.whatsapp = whatsapp.getText().toString();
            loja.anunciante = auth.getCurrentUser().getUid();
            loja.urlIcone = uploaFile.UploadPhoto(bitmapIcone, "icone.png", reference, auth.getUid());
            loja.urlFoto1 = uploaFile.UploadPhoto(bitmapFoto1, "foto_1", reference, auth.getUid());
            loja.urlFoto2 = uploaFile.UploadPhoto(bitmapFoto2, "foto_2", reference, auth.getUid());
            loja.urlFoto3 = uploaFile.UploadPhoto(bitmapFoto3, "foto_3", reference, auth.getUid());
       }
        else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        return token;
    }

    private BroadcastReceiver mReceiverDataAnuncio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == RECEIVER_DATA_ANUNCIO) {
                loja = (Loja) intent.getSerializableExtra(LOJA_RECEIVE_DATA);
                if (loja != null){
                    SetLoja(loja);
                }
            }
        }
    };

    private void setPhoto(String url_icone, String url_foto1, String url_foto2, String url_foto3) {
        PhotoPicasso picasso = new PhotoPicasso(getApplicationContext());
        picasso.PhotoOrigSize(url_icone, buttonLoadIco, !resultIcone);
        picasso.Photo600x400(url_foto1, mFoto1, !resultFoto1);
        picasso.Photo600x400(url_foto2, mFoto2, !resultFoto2);
        picasso.Photo600x400(url_foto3, mFoto3, !resultFoto3);
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
    /*    outState.putString(ANUNCIO_NOME, nomeAnuncio.getText().toString());
        outState.putString(ANUNCIO_DOCUMENTO, documento.getText().toString());
        outState.putString(ANUNCIO_TIPO_DOCUMENTO, docType);
        outState.putString(ANUNCIO_WHATSAPP, whatsapp.getText().toString());
        outState.putString(ANUNCIO_TELEFONE, telefone.getText().toString());
        outState.putString(ANUNCIO_EMAIL, email.getText().toString());
        outState.putString(ANUNCIO_ENDERECO, addressName.getText().toString());
        outState.putString(ANUNCIO_RAMO, btnRamo.getText().toString()); */
        outState.putSerializable(ANUNCIO_LOCAL, (Serializable) loja);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loja = (Loja) savedInstanceState.getSerializable(ANUNCIO_LOCAL);
        if (loja != null){
            SetLoja(loja);
        }
        /*nomeAnuncio.setText(loja.titulo);
        documento.setText(loja.documento.documento);
        if (loja.documento.tipoDocumento.equals("CPF"))
            radioCPF.setChecked(true);
        if (loja.documento.tipoDocumento.equals("CNPJ"))
            radioCNPJ.setChecked(true);
        addressName.setText(loja.local.endereco);
        whatsapp.setText(loja.whatsapp);
        telefone.setText(loja.telefone);
        btnRamo.setText(loja.ramo);
        email.setText(loja.emailAnuncio); */
    }
    void SetLoja(Loja loja){
        nomeAnuncio.setText(loja.titulo);
        if (loja.documento.tipoDocumento.equals("CPF"))
            radioCPF.setChecked(true);
        if (loja.documento.tipoDocumento.equals("CNPJ"))
            radioCNPJ.setChecked(true);
        documento.setText(loja.documento.documento);
        btnRamo.setText(loja.ramo);
        addressName.setText(loja.local.endereco);
        whatsapp.setText(loja.whatsapp);
        telefone.setText(loja.telefone);
        email.setText(loja.emailAnuncio);
        String url_icone = loja.urlIcone;
        String url_foto1 = loja.urlFoto1;
        String url_foto2 = loja.urlFoto2;
        String url_foto3 = loja.urlFoto3;
        setPhoto(url_icone, url_foto1, url_foto2, url_foto3);
    }
}