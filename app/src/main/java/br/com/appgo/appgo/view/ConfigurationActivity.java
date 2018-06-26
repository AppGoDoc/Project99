package br.com.appgo.appgo.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.SerializationUtils;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.bitmap.RotateImage;
import br.com.appgo.appgo.controller.ResizePhoto;
import br.com.appgo.appgo.controller.SPreferences;
import br.com.appgo.appgo.controller.TextFildCheck;
import br.com.appgo.appgo.model.ListLoja;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.PhotoPicasso;
import br.com.appgo.appgo.persistence.UploaFile;

import static br.com.appgo.appgo.view.MainActivity.LIST_LOJA_ACTION_CONFIG;
import static br.com.appgo.appgo.view.MainActivity.LIST_LOJA_PACK_CONFIG;
import static br.com.appgo.appgo.view.RegisterActivity.REQUEST_USER_AVATAR;

public class ConfigurationActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    public static final int PORTUGUES_BRASILEIRO = 0;
    public static final int INGLES = 1;
    public static final int ESPANHOL = 2;
    private ListLoja listLoja;
    private Loja loja;
    private Button mSaveEdit, mEraseAvatar, mChooseAvatar, mExcluirLoja, mRotateImg;
    private ImageView mUserImage, mUserStore;
    private EditText mUserName;
    private SeekBar seekBar;
    private Toolbar toolbar;
    private SPreferences sPreferences;
    private TextView txtSize;
    private RadioGroup radioGroup;
    private RadioButton radio1, radio2, radio3;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    boolean tokenUserPhoto;
    private Bitmap mUserAvatar;
    private Float angle;
    PhotoPicasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        angle = 0f;
        picasso = new PhotoPicasso(this);
        mUserAvatar = null;
        tokenUserPhoto = false;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        sPreferences = new SPreferences(this);
        mExcluirLoja = findViewById(R.id.btn_cancel_store);
        mExcluirLoja.setOnClickListener(this);
        mRotateImg = findViewById(R.id.rotate_img);
        mRotateImg.setOnClickListener(this);
        mUserStore = findViewById(R.id.img_store);
        txtSize = findViewById(R.id.txt_size_choose);
        seekBar = findViewById(R.id.seekbar_icon_size);
        toolbar = findViewById(R.id.toolbar2);
        radioGroup = findViewById(R.id.radio_idioma);
        radioGroup.setOnCheckedChangeListener(this);
        radio1 = findViewById(R.id.radiobtn1);
        radio2 = findViewById(R.id.radiobtn2);
        radio3 = findViewById(R.id.radiobtn3);
        mSaveEdit = findViewById(R.id.save_edit_user);
        mSaveEdit.setOnClickListener(this);
        mEraseAvatar = findViewById(R.id.btn_apagar_edit);
        mEraseAvatar.setOnClickListener(this);
        mChooseAvatar = findViewById(R.id.btn_select_avatar_edit);
        mChooseAvatar.setOnClickListener(this);
        mUserName = findViewById(R.id.user_name_edited);
        mUserImage = findViewById(R.id.img_user_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(sPreferences.GetIntShared()/50);
        getIdioma();
        getUserData();
        listLoja = RequestListLoja();
        loja = ConfirmUserStore();
        if (loja!=null){
            picasso.Photo2(loja.urlIcone, mUserStore, 600, 600, true);
        }
    }

    private void getUserData() {
        try {
            mUserName.setText(mUser.getDisplayName());
            Picasso.with(this)
                    .load(mUser.getPhotoUrl())
                    .fit()
                    .into(mUserImage);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        txtSize.setText(String.format("%s",progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Toast.makeText(this, "Selecione o tamanho de icone desejado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (txtSize.getText().toString().equals("0")){
            txtSize.setText("1");
            seekBar.setProgress(1);
        }
        sPreferences.SetIntShared(
                Integer.parseInt(txtSize.getText().toString())*50
        );
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radiobtn1:
                sPreferences.setIdioma(PORTUGUES_BRASILEIRO);
                break;
            case R.id.radiobtn2:
                sPreferences.setIdioma(INGLES);
                break;
            case R.id.radiobtn3:
                sPreferences.setIdioma(ESPANHOL);
                break;
        }
    }

    private void getIdioma() {
        switch (sPreferences.getIdioma()){
            case 0:
                radio1.setChecked(true);
                break;
            case 1:
                radio2.setChecked(true);
                break;
            case 2:
                radio3.setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_avatar_edit:
                SelectUserAvatar(REQUEST_USER_AVATAR);
                break;
            case R.id.btn_apagar_edit:
                EraseUserAvatar();
                break;
            case R.id.save_edit_user:
                UpdateUserData();
            case R.id.btn_cancel_store:
                CancelStore();
                break;
            case R.id.rotate_img:
                RotateImage();
                break;
        }
    }

    private void RotateImage() {
//        angle += 90f;
//        if (angle == 360f){
//            angle = 0f;
//        }
//        mUserImage.setRotation(angle);
        mUserAvatar = new RotateImage(this).rotateBitmapFromImageView(mUserImage);
        mUserImage.setImageBitmap(mUserAvatar);
        tokenUserPhoto = true;
    }

    private void CancelStore() {
        if (loja==null){
            Toast.makeText(this, "Você não possui loja cadastrada em nosso banco de dados."
                    , Toast.LENGTH_SHORT).show();
        }
        else {
            if (loja.AnuncioFotografico.size()>0){
                Toast.makeText(this, "Você possui " + loja.AnuncioFotografico.size()
                        + " anúncios salvos no perfil da sua loja, apague os anúncios e reinicie a aplicação.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Algum problema impede o AppGo! de excluir o perfil de sua loja" +
                        ", contate o suporte.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Loja ConfirmUserStore(){
        Loja tmpLoja = null;
        for (Loja loja: listLoja.lojas){
            if (loja.anunciante.equals(mUser.getUid())){
                tmpLoja = loja;
            }
        }
        return tmpLoja;
    }

    private void UpdateUserData() {
        TextFildCheck text = new TextFildCheck();
        if (text.CheckName(mUserName.getText().toString()) &&
                tokenUserPhoto){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            UploaFile uploaFile = new UploaFile(this);
            uploaFile.UploadUserData(mUserAvatar, mUser.getUid(), storageReference,
                    mUserName.getText().toString(), mUser);
            setResult(RESULT_OK);
            finish();
        }
        else {
            if (text.CheckName(mUserName.getText().toString())){
                UploaFile uploaFile = new UploaFile(this);
                uploaFile.UploadUserData(mUserName.getText().toString(), mUser);
                setResult(RESULT_OK);
                finish();
            }
            else {
                Toast.makeText(this,
                        "Você precisa digitar um nome válido para alterar as configurações do usuário",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SelectUserAvatar(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }
    private Bitmap SetUserAvatar(Intent data, ImageView imageView){
        Bitmap bitmap = null;
        Uri targetUriFoto1 = data.getData();
        try {
            ResizePhoto resize = new ResizePhoto();
            bitmap = resize.MinimizeBitmap(
                    BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUriFoto1)),
                    4);
            imageView.setImageBitmap(bitmap);
            tokenUserPhoto = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private void EraseUserAvatar() {
        mUserImage.setImageDrawable(null);
        tokenUserPhoto = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_USER_AVATAR){
            if (resultCode == RESULT_OK){
                mUserAvatar = SetUserAvatar(data, mUserImage);

            }
        }
    }
    private ListLoja RequestListLoja() {
        ListLoja listLoja = null;
        Intent intent = getIntent();
        if (intent.getAction().equals(LIST_LOJA_ACTION_CONFIG)){
            byte[] data = intent.getByteArrayExtra(LIST_LOJA_PACK_CONFIG);
            listLoja = SerializationUtils.deserialize(data);
        }
        return listLoja;
    }
//    private Bitmap RotateBitmap(ImageView imageView){
//        try {
//            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//            Matrix matrix = new Matrix();
//            matrix.postRotate(90);
//            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
//            tokenUserPhoto = true;
//            return Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
//        } catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//
//    }
}