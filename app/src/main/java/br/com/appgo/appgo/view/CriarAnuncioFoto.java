package br.com.appgo.appgo.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.controller.ResizePhoto;
import br.com.appgo.appgo.model.AnuncioFoto;
import br.com.appgo.appgo.model.Foto;
import br.com.appgo.appgo.model.FotoView;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.UploaFile;

import static br.com.appgo.appgo.view.CriarAnuncioActivity.ANUNCIOS;
import static br.com.appgo.appgo.view.PostarActivity.RESULT_FOTO_ANUNCIO;
import static br.com.appgo.appgo.view.PostarActivity.USER_EDIT_POSITION;
import static br.com.appgo.appgo.view.PostarActivity.USER_LOJA_ACTION_TO_POST;
import static br.com.appgo.appgo.view.PostarActivity.USER_LOJA_EDIT_POST;
import static br.com.appgo.appgo.view.PostarActivity.USER_LOJA_TO_POST;

public class CriarAnuncioFoto extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_FOTO_ANUNCIO_1 = 0;
    public static final int REQUEST_FOTO_ANUNCIO_2 = 1;
    public static final int REQUEST_FOTO_ANUNCIO_3 = 2;
    public static final int REQUEST_FOTO_ANUNCIO_4 = 3;
    public static final int REQUEST_FOTO_ANUNCIO_5 = 4;
    public static final int REQUEST_FOTO_ANUNCIO_6 = 5;
    public static final int REQUEST_FOTO_ANUNCIO_7 = 6;
    public static final String PREFIX_ARCHIVE = "foto_anuncio_";
    private int editPosition = -1;
    private Button mSave;
    private ImageView mFoto1, mFoto2, mFoto3, mFoto4, mFoto5, mFoto6, mFoto7,
    btnFoto1, btnFoto2, btnFoto3, btnFoto4, btnFoto5, btnFoto6, btnFoto7;
    private EditText edtFoto1, edtFoto2, edtFoto3, edtFoto4, edtFoto5, edtFoto6, edtFoto7;
    List<FotoView> fotoViews;
    List<Bitmap> bitmapAnuncio;
    boolean[] tokenBitmap;
    FirebaseAuth auth;
    FirebaseUser user;
    Loja loja;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference;
    DatabaseReference database;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_criar_anuncio_foto);
        super.onCreate(savedInstanceState);

        mFoto1 = (ImageView)findViewById(R.id.anuncio1_foto);
        mFoto2 = (ImageView)findViewById(R.id.anuncio2_foto);
        mFoto3 = (ImageView)findViewById(R.id.anuncio3_foto);
        mFoto4 = (ImageView)findViewById(R.id.anuncio4_foto);
        mFoto5 = (ImageView)findViewById(R.id.anuncio5_foto);
        mFoto6 = (ImageView)findViewById(R.id.anuncio6_foto);
        mFoto7 = (ImageView)findViewById(R.id.anuncio7_foto);
        btnFoto1 = (ImageView)findViewById(R.id.anuncio1_button);
        btnFoto2 = (ImageView)findViewById(R.id.anuncio2_button);
        btnFoto3 = (ImageView)findViewById(R.id.anuncio3_button);
        btnFoto4 = (ImageView)findViewById(R.id.anuncio4_button);
        btnFoto5 = (ImageView)findViewById(R.id.anuncio5_button);
        btnFoto6 = (ImageView)findViewById(R.id.anuncio6_button);
        btnFoto7 = (ImageView)findViewById(R.id.anuncio7_button);
        btnFoto1.setOnClickListener(this);
        btnFoto2.setOnClickListener(this);
        btnFoto3.setOnClickListener(this);
        btnFoto4.setOnClickListener(this);
        btnFoto5.setOnClickListener(this);
        btnFoto6.setOnClickListener(this);
        btnFoto7.setOnClickListener(this);
        edtFoto1 = (EditText)findViewById(R.id.anuncio1_text);
        edtFoto2 = (EditText)findViewById(R.id.anuncio2_text);
        edtFoto3 = (EditText)findViewById(R.id.anuncio3_text);
        edtFoto4 = (EditText)findViewById(R.id.anuncio4_text);
        edtFoto5 = (EditText)findViewById(R.id.anuncio5_text);
        edtFoto6 = (EditText)findViewById(R.id.anuncio6_text);
        edtFoto7 = (EditText)findViewById(R.id.anuncio7_text);
        fotoViews = RequestImageView();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storageReference = firebaseStorage.getReference();
        database = FirebaseDatabase.getInstance().getReference();
        bitmapAnuncio = new ArrayList<>();
        mSave = (Button)findViewById(R.id.btn_salvar);
        mSave.setOnClickListener(this);
        mSave.setEnabled(true);
        loja = RequestLoja();
        editPosition = loja.AnuncioFotografico.size();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.anuncio1_button:
                GetFoto(REQUEST_FOTO_ANUNCIO_1);
                break;
            case R.id.anuncio2_button:
                GetFoto(REQUEST_FOTO_ANUNCIO_2);
                break;
            case R.id.anuncio3_button:
                GetFoto(REQUEST_FOTO_ANUNCIO_3);
                break;
            case R.id.anuncio4_button:
                GetFoto(REQUEST_FOTO_ANUNCIO_4);
                break;
            case R.id.anuncio5_button:
                GetFoto(REQUEST_FOTO_ANUNCIO_5);
                break;
            case R.id.anuncio6_button:
                GetFoto(REQUEST_FOTO_ANUNCIO_6);
                break;
            case R.id.anuncio7_button:
                GetFoto(REQUEST_FOTO_ANUNCIO_7);
                break;
            case R.id.btn_salvar:
                mSave.setEnabled(false);
                try {
                    loja.AnuncioFotografico.add(UpdateAnuncio());
                    database.child(ANUNCIOS).child(auth.getCurrentUser().getUid()).setValue(loja);
                    Intent sucessIntent = new Intent();
                    setResult(RESULT_FOTO_ANUNCIO, sucessIntent);
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                    Intent failIntent = new Intent();
                    setResult(-1, failIntent);
                    finish();
                }
                break;
        }
    }

    private AnuncioFoto UpdateAnuncio() {
        AnuncioFoto anuncioFoto = new AnuncioFoto();
        anuncioFoto.data_do_anuncio = GetData();
        int i = 0;
        for (FotoView fotoView: fotoViews){
            if (fotoView.bitmap!=null){
                anuncioFoto.fotos.add(
                        CreateAnuncio(
                                fotoView.bitmap,
                                PREFIX_ARCHIVE+""+ i,
                                fotoViews.get(i).descricao.getText().toString(),
                                anuncioFoto.data_do_anuncio
                        )
                );
                i++;
            }
        }
        return anuncioFoto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >-1 && requestCode < 7){
            if(resultCode == RESULT_OK){
                fotoViews.get(requestCode).bitmap = SetFoto(data, fotoViews.get(requestCode).imageView);
            }
        }
    }

    private void GetFoto(int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    private Bitmap SetFoto(Intent data, ImageView imageView){
        Bitmap bitmap = null;
        Uri targetUriFoto1 = data.getData();
        try {
            ResizePhoto resize = new ResizePhoto();
            bitmap = resize.MinimizeBitmap(
                    BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto1)),
                    4);
            imageView.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Loja RequestLoja() {
        Loja loja = null;
        Intent intent = getIntent();
        if (intent.getAction().equals(USER_LOJA_ACTION_TO_POST)){
            byte[] data = intent.getByteArrayExtra(USER_LOJA_TO_POST);
            loja = SerializationUtils.deserialize(data);
        }
        if (intent.getAction().equals(USER_LOJA_EDIT_POST)){
            byte[] data = intent.getByteArrayExtra(USER_LOJA_TO_POST);
            loja = SerializationUtils.deserialize(data);
            editPosition = intent.getIntExtra(USER_EDIT_POSITION, -1);
        }
        return loja;
    }

    private Foto CreateAnuncio(Bitmap bitmap, String fileName, String descricao, String dataAnuncio){
        Foto tmpFoto = new Foto();
        if (bitmap != null){
            UploaFile uploaFile = new UploaFile(getApplicationContext());
            tmpFoto.urlFoto = uploaFile.UploadPhotoUrl(
                                        bitmap,
                                        fileName,
                                        storageReference,
                                        loja.anunciante,
                                        dataAnuncio
                                );
            tmpFoto.descrFoto = descricao;
        }
        return tmpFoto;
    }
    @SuppressLint("DefaultLocale")
    private String GetData(){
        Calendar calendar = Calendar.getInstance();
        return String.format("%d%d%d_h%dm%d_",calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR),calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }

    private List<FotoView> RequestImageView(){
        List<FotoView> fotoViews = new ArrayList<>();
        fotoViews.add(new FotoView(mFoto1, edtFoto1, null));
        fotoViews.add(new FotoView(mFoto2, edtFoto2, null));
        fotoViews.add(new FotoView(mFoto3, edtFoto3, null));
        fotoViews.add(new FotoView(mFoto4, edtFoto4, null));
        fotoViews.add(new FotoView(mFoto5, edtFoto5, null));
        fotoViews.add(new FotoView(mFoto6, edtFoto6, null));
        fotoViews.add(new FotoView(mFoto7, edtFoto7, null));
        return fotoViews;
    }
}