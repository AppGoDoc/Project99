package br.com.appgo.appgo.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.fragment.DialogFragmentForgetPasswd;

import static br.com.appgo.appgo.view.LoginActivity.RESULT_SIGN_IN_EMAIL;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_REGISTER_NEWUSER = 1254;
    public static final String TAG_DIALOG_FORGET_PASSWD = "forget_passwd";
    EditText email, password;
    Button login, forgetPassword, register;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        email = (EditText)findViewById(R.id.edt_email);
        password = (EditText)findViewById(R.id.edt_passwd);
        login = (Button)findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        forgetPassword = (Button)findViewById(R.id.btn_forgetpasswd);
        forgetPassword.setOnClickListener(this);
        register = (Button)findViewById(R.id.btn_registrar);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                SignInEmail(email.getText().toString(), password.getText().toString());
                break;
            case R.id.btn_forgetpasswd:
                ForgetPasswd();
                break;
            case R.id.btn_registrar:
                RegisterNewUser();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_REGISTER_NEWUSER:
                if (resultCode == RESULT_OK){
                    setResult(RESULT_SIGN_IN_EMAIL);
                    finish();
                }
                break;
        }
    }
    private void ForgetPasswd() {
        DialogFragment dialogFragment = new DialogFragmentForgetPasswd();
        dialogFragment.show(dialogCall(TAG_DIALOG_FORGET_PASSWD), TAG_DIALOG_FORGET_PASSWD);
    }

    private void RegisterNewUser() {
        Intent intentRegister = new Intent(this, RegisterActivity.class);
        startActivityForResult(intentRegister, REQUEST_REGISTER_NEWUSER);
    }

    private void SignInEmail(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intentLoginResult = new Intent();
                            setResult(RESULT_OK, intentLoginResult);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Intent intentLoginResult = new Intent();
                            setResult(RESULT_CANCELED, intentLoginResult);
                            finish();
                        }

                        // ...
                    }
                });
        }
    public FragmentTransaction dialogCall(String Tag){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(Tag);
        if (fragment != null)
            fragmentTransaction.remove(fragment);
        fragmentTransaction.addToBackStack(Tag);
        return fragmentTransaction;
    }
}
