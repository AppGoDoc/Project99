package br.com.appgo.appgo.View;

import android.app.DialogFragment;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import br.com.appgo.appgo.Fragment.ForgetPasswd;
import br.com.appgo.appgo.Model.User;
import br.com.appgo.appgo.R;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LOGIN FACEBOOK -----> ";
    private static final String TAG_LOGIN_FAIL = "Atenção!!!\nConfira seu login e senha.";
    private EditText login, senha;
    private Button loginSubmit, register, forgetPass;
    LoginButton facebook;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    private static final String TAG_FRAGMENT = "dialogforgetpasswd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        facebook = (LoginButton)findViewById(R.id.login_facebook);
        facebook.setReadPermissions("public_profile","email");
        register = (Button) findViewById(R.id.btn_cadastrar);
        forgetPass = (Button) findViewById(R.id.btn_forgetpass);
        login = (EditText) findViewById(R.id.edt_login);
        senha = (EditText) findViewById(R.id.edt_senha);
        loginSubmit = (Button) findViewById(R.id.btn_login);


        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook: OnSucess: " + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook: OnCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Facebook: Error: ");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mAuth.signInWithEmailAndPassword(login.getText().toString(), senha.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, TAG_LOGIN_FAIL , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, TAG_LOGIN_FAIL, Toast.LENGTH_SHORT).show();
                }
                        }
            });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                if (fragment != null)
                    fragmentTransaction.remove(fragment);
                fragmentTransaction.addToBackStack(TAG_FRAGMENT);
                DialogFragment dialogFragment = new ForgetPasswd();
                dialogFragment.show(fragmentTransaction, TAG_FRAGMENT);
            }
        });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Anunciantes/"+ user.getUid());
                            User user1 = new User();
                            user1.name = user.getDisplayName();
                            user1.celular = user.getPhoneNumber();
                            user1.email = user.getEmail();
                            user1.userPhotoUri = user.getPhotoUrl().getPath();
                            Log.i("---++++---", user.getPhotoUrl().getPath());
                            database.setValue(user1);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, TAG_LOGIN_FAIL,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser currentUser){
        if (currentUser != null){
            finish();
        }
    }
}