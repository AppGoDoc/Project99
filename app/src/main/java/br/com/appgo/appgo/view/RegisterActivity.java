package br.com.appgo.appgo.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.bitmap.RotateImage;
import br.com.appgo.appgo.controller.ResizePhoto;
import br.com.appgo.appgo.controller.TextFildCheck;
import br.com.appgo.appgo.persistence.UploaFile;

/**
 * Created by hex on 16/03/18.
 */

public class RegisterActivity extends AppCompatActivity
                              implements View.OnClickListener {
    public static final String USERS_REFERENCE = "Users";
    public static final int REQUEST_USER_AVATAR = 2225;
    public static final int RESULT_LOGIN_SUCESS = 2226;
    public static final int RESULT_LOGIN_FAIL = 2227;
    private ProgressBar mProgressRegister;
    private ImageView mUserImage;
    private Button mSaveButton, mSelectAvatar, mEraseAvatar, mRotateimage;
    EditText mUserName, mUserMail, mUserPasswd, mUserPasswdConfirm;
    DatabaseReference database;
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    Bitmap mUserAvatar;
    boolean tokenUserPhoto;
    boolean tryAgainLogin;
    private Float angle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        angle = 0f;
        mUserAvatar = null;
        //tokens de verificação;
        tokenUserPhoto = false;
        tryAgainLogin = true;
        //XML elements
        mProgressRegister = findViewById(R.id.progressbar_register);
        mRotateimage = findViewById(R.id.rotate_img);
        mRotateimage.setOnClickListener(this);
        mUserImage = findViewById(R.id.img_user_edit);
        mSaveButton = findViewById(R.id.btn_save_register);
        mSelectAvatar = findViewById(R.id.btn_select_avatar_edit);
        mEraseAvatar = findViewById(R.id.btn_apagar);
        mUserName = findViewById(R.id.name_user);
        mUserMail = findViewById(R.id.email_user);
        mUserPasswd = findViewById(R.id.password);
        mUserPasswdConfirm = findViewById(R.id.password_confirm);
        //Call all listeners
        mSaveButton.setOnClickListener(this);
        mSelectAvatar.setOnClickListener(this);
        mEraseAvatar.setOnClickListener(this);
        //Database reference call and Firebase classes
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        ProgressRegister(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save_register:
                CheckUserRegister();
                break;
            case R.id.btn_select_avatar_edit:
                SelectUserAvatar(REQUEST_USER_AVATAR);
                break;
            case R.id.btn_apagar:
                EraseUserAvatar();
                break;
            case R.id.rotate_img:
                RotateImageView();
                break;
        }
    }

    private void RotateImageView() {
        if (tokenUserPhoto){
            mUserAvatar = new RotateImage(this).rotateBitmapFromImageView(mUserImage);
            mUserImage.setImageBitmap(mUserAvatar);
        }
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
    private void CheckUserRegister() {
        ProgressRegister(false);
        String userName = mUserName.getText().toString();
        String userEmail = mUserMail.getText().toString();
        String userPasswd = mUserPasswd.getText().toString();
        String userPasswdConfirm = mUserPasswdConfirm.getText().toString();
        TextFildCheck testField = new TextFildCheck();
        if (testField.CheckName(userName)){
            mUserName.setBackgroundColor(getResources().getColor(R.color.green_confirm));
            if (testField.validEmail(userEmail)){
                mUserMail.setBackgroundColor(getResources().getColor(R.color.green_confirm));
                if (userPasswd.equals(userPasswdConfirm)){
                    SaveUserRegister(userName, userEmail, userPasswd);
                    mUserPasswd.setBackgroundColor(getResources().getColor(R.color.green_confirm));
                    mUserPasswdConfirm.setBackgroundColor(getResources().getColor(R.color.green_confirm));
                }
                else {
                    Toast.makeText(this, "A senha e a confirmação da senha estão diferentes" +
                            ", confira os campos destacados.", Toast.LENGTH_SHORT).show();
                    mUserPasswd.setBackgroundColor(getResources().getColor(R.color.error));
                    mUserPasswdConfirm.setBackgroundColor(getResources().getColor(R.color.error));
                    ProgressRegister(true);
                }
            }
            else{
                Toast.makeText(this, "Endereço de e-mail aparentemente inválido, confira" +
                        "o campo destacado.", Toast.LENGTH_SHORT).show();
                mUserMail.setBackgroundColor(getResources().getColor(R.color.error));
                ProgressRegister(true);
            }
        }
        else {
            Toast.makeText(this, "Você precisa digitar seu nome corretamente, " +
                    "confira o campo destacado.", Toast.LENGTH_SHORT).show();
            mUserName.setBackgroundColor(getResources().getColor(R.color.error));
            ProgressRegister(true);
        }
    }

    private void SaveUserRegister(final String userName, final String userEmail, final String userPasswd) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPasswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(mAuth.getCurrentUser(), userName, userEmail, userPasswd);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Falha na autenticação do registro," +
                                            "Aguarde Segunda tentativa.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null, userName, userEmail, userPasswd);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user, final String userName, String userMail, String userPasswd) {
//        if (user!=null){
        if (tokenUserPhoto){
            UploaFile uploaFile = new UploaFile(this);
            boolean request = uploaFile.UploadUserData(
                    mUserAvatar, user.getUid(), storageReference, userName, user);
            if (request){
                Toast.makeText(RegisterActivity.this,
                        "Usuário: " + userName + " registrado com sucesso", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }
        else {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,
                                        "Usuário: " + userName + " registrado com sucesso", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    });
        }
//        else {
//            if (tryAgainLogin){
//                tryAgainLogin = false;
//                SaveUserRegister(userName, userMail, userPasswd);
//            }
//            else {
//                setResult(RESULT_CANCELED);
//                finish();
//            }
//        }
    }
    private void ProgressRegister(boolean token){
        if (token){
            mProgressRegister.setVisibility(View.GONE);
        }
        else {
            mProgressRegister.setVisibility(View.VISIBLE);
        }
        mUserName.setEnabled(token);
        mRotateimage.setEnabled(token);
        mRotateimage.setEnabled(token);
        mSaveButton.setEnabled(token);
        mSelectAvatar.setEnabled(token);
        mEraseAvatar.setEnabled(token);
        mUserMail.setEnabled(token);
        mUserPasswd.setEnabled(token);
        mUserPasswdConfirm.setEnabled(token);
    }
}