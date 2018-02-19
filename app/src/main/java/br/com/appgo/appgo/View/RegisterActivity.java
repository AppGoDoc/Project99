package br.com.appgo.appgo.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.FirebaseDatabase;

import br.com.appgo.appgo.Controller.CheckCpf;
import br.com.appgo.appgo.Controller.SPreferences;
import br.com.appgo.appgo.Controller.TextFildCheck;
import br.com.appgo.appgo.Persistence.FireBase;
import br.com.appgo.appgo.Model.User;
import br.com.appgo.appgo.R;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userName, userMail, userMailConfirm, userCell, userCompany, userCpf,
            userPasswd, userPasswdConfirm;
    private Button serviceType, saveRegister;
    private ProgressBar progressBar;
    String TAG = "FIREBASE::: ";
    MaskEditTextChangedListener maskCPF, maskCel;
    CheckCpf checkCpf;
    TextFildCheck fildCheck;
    String service, email, celular, cpf, nome, company, passwd;
//--Firebase Class for user authentication and sign in...
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;
//==SPreferences====================================================================================
    SPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        preferences = new SPreferences(getApplicationContext());
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        userName = (EditText)findViewById(R.id.username);
        userCpf = (EditText)findViewById(R.id.usercpf);
        userMail = (EditText)findViewById(R.id.usermail);
        userMailConfirm = (EditText)findViewById(R.id.usermailconfirm);
        userCell = (EditText)findViewById(R.id.usercelphone);
        userCompany = (EditText)findViewById(R.id.usercompanyname);
        userPasswd = (EditText)findViewById(R.id.userpasswd);
        userPasswdConfirm = (EditText)findViewById(R.id.userpasswdconfirm);
        serviceType = (Button)findViewById(R.id.service_type);
        saveRegister = (Button) findViewById(R.id.save_register);
        maskCPF = new MaskEditTextChangedListener("###.###.###-##", userCpf);
        maskCel = new MaskEditTextChangedListener("(##)####.#####", userCell);
        userCell.addTextChangedListener(maskCel);
        userCpf.addTextChangedListener(maskCPF);
        checkCpf = new CheckCpf();
        fildCheck = new TextFildCheck();
        service = email = celular = cpf = nome = company = null;
//------Autênticação do Firebase, checa se usuário está logado.
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
//------Screen Buttons listeners
        serviceType.setOnClickListener(this);
        saveRegister.setOnClickListener(this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    //User is signed in...
                    Log.d("FIREBASE:: ", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    //No user is Signed in...
                    Log.d("FIREBASE:: ", "onAuthStateChanged:signed_out");
                }
            }
        };
        if (user!=null){
            //User is signed in...
            Log.d("FIREBASE:: ", "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            //No user is Signed in...
            Log.d("FIREBASE:: ", "onAuthStateChanged:signed_out");
        }
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.service_type:
                final CharSequence[] services = getResources().getStringArray(R.array.service_types);

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Qual a sua atividade?");
                builder.setItems(services, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        serviceType.setText(services[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
//            case R.id.Address:
//                Intent intent = new Intent(getApplicationContext(), SearchOnMapActivity.class);
//                startActivity(intent);
//                break;
            case R.id.save_register:
                String message = "ATENÇÂO!!!\n";
                int token = 0;
//--------------Checa se o nome foi digitado e tem mais que 4 caracteres
                if (fildCheck.CheckName(userName.getText().toString())){
                    userName.setBackgroundColor(getResources().getColor(R.color.white));
                    nome = userName.getText().toString();
                } else {
                    userName.setBackgroundColor(getResources().getColor(R.color.error));
                    message += "Você precisa digitar o seu nome.\n";
                    token++;
                }
//--------------Checa se o cpf é válido
                if (checkCpf.isCPF(userCpf.getText().toString())){
                    userCpf.setBackgroundColor(getResources().getColor(R.color.white));
                    cpf = userCpf.getText().toString();
                } else {
                    userCpf.setBackgroundColor(getResources().getColor(R.color.error));
                    message += "CPF inválido. Confira seu nome.\n";
                    token++;
                }
//--------------Checa se o email é válido
                if (fildCheck.validEmail(userMail.getText().toString())){
                    if (userMailConfirm.getText().toString().equals(userMail.getText().toString())){
                        email = userMail.getText().toString();
                        userMail.setBackgroundColor(getResources().getColor(R.color.white));
                        userMailConfirm.setBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        message += "confirmação de e-mail não confere.\n";
                        token++;
                        userMailConfirm.setBackgroundColor(getResources().getColor(R.color.error));
                    }
                } else {
                    userMail.setBackgroundColor(getResources().getColor(R.color.error));
                    message += "e-mail inválido. cheque o seu e-mail.\n";
                    token++;
                }
//--------------Checa se o telefone é válido
                if (fildCheck.isTelefone(userCell.getText().toString())){
                    celular = userCell.getText().toString();
                    userCell.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    userCell.setBackgroundColor(getResources().getColor(R.color.error));
                    message += "Número de telefone inválido.\n";
                    token++;
                }
//--------------Checa validade do nome da empresa
                if (fildCheck.CheckName(userCompany.getText().toString())){
                    company = userCompany.getText().toString();
                    userCompany.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    userCompany.setBackgroundColor(getResources().getColor(R.color.error));
                    message += "Digite o nome da sua empresa\n";
                    token++;
                }
//--------------Checa se o usuário escolheu alguma atividade.
                if (!serviceType.getText().toString().equals("Qual a sua atividade?")){
                    service = serviceType.getText().toString();
                    serviceType.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    serviceType.setBackgroundColor(getResources().getColor(R.color.error));
                    message += "Escolha o seu tipo de atividade\n";
                    token++;
                }
//--------------Checa se o usuário digitou uma senha e se ela tem mais de 6 dígitos.
                if (userPasswd.getText().toString().isEmpty() || userPasswd.getText().toString().length()< 6){
                    userPasswd.setBackgroundColor(getResources().getColor(R.color.error));
                    message += "Você precisa de uma senha com mais de 6 dígitos.\n";
                    token++;
                } else {
                    if (userPasswdConfirm.getText().toString().equals(userPasswd.getText().toString())){
                        userPasswd.setBackgroundColor(getResources().getColor(R.color.white));
                        userPasswdConfirm.setBackgroundColor(getResources().getColor(R.color.white));
                        passwd = userPasswd.getText().toString();
                    } else {
                        message += "Senha e confirmação de senha não conferem.\n";
                        userPasswd.setBackgroundColor(getResources().getColor(R.color.error));
                        userPasswdConfirm.setBackgroundColor(getResources().getColor(R.color.error));
                        token++;
                    }
                }
                if (token > 0){
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    saveRegister.setEnabled(false);
                    //Dados de usuário estão corretos, realiza o login.
                    mAuth.createUserWithEmailAndPassword(email, passwd)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    if (task.isSuccessful()){
                                        user = mAuth.getCurrentUser();

                                        User createUser = new User(
                                                nome, email, cpf, celular);
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        FireBase fireBase = new FireBase(firebaseDatabase, mAuth);
                                        fireBase.AddUser(createUser);
                                        saveRegister.setEnabled(false);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),
                                                "Cadastro Realizado com sucesso.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplication(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "Falha no Cadastro.\nTente realizar seu" +
                                                        "cadastro novamente.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

}
