package br.com.appgo.appgo.View;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import br.com.appgo.appgo.Controller.TextFildCheck;
import br.com.appgo.appgo.Fragment.ForgetPasswd;
import br.com.appgo.appgo.Model.User;
import br.com.appgo.appgo.Persistence.FireBase;
import br.com.appgo.appgo.R;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class AnuncianteDados extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG_FRAGMENT = "Fragment ForgetPasswd";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Anunciantes/"+mAuth.getUid());
    private EditText edtName, edtMail, edtCell, edtCPF;
    private Button btnSalvar, btnCancelar, btnAlterarSenha;
    private ImageView photoUser;
    MaskEditTextChangedListener maskCPF, maskCel;
    User user1 = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_usuario);

        edtName = (EditText) findViewById(R.id.nome_usuario);
        edtMail = (EditText) findViewById(R.id.email_usuario);
        edtCell = (EditText) findViewById(R.id.celular_usuario);
        edtCPF = (EditText) findViewById(R.id.cpf_usuario);
        btnSalvar = (Button) findViewById(R.id.btn_salvar);
        btnCancelar = (Button) findViewById(R.id.btn_cancelar);
        btnAlterarSenha = (Button) findViewById(R.id.btn_alterarsenha);
        maskCPF = new MaskEditTextChangedListener("###.###.###-##", edtCPF);
        maskCel = new MaskEditTextChangedListener("(##)#####-####", edtCell);
        photoUser = (ImageView) findViewById(R.id.imageview_photourl);

        btnSalvar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnAlterarSenha.setOnClickListener(this);
        String facebookUserId = null;
        for(UserInfo profile : user.getProviderData()) {
            facebookUserId = profile.getUid();
        }
        String urlFoto = "https://graph.facebook.com/" + facebookUserId + "/picture?height=100";
        Picasso.with(getApplicationContext()).load(urlFoto).into(photoUser);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user1 = dataSnapshot.getValue(User.class);
                Log.i("RECUPERATE ", "+++  " + user1.name);
                edtName.setText(user1.name);
                edtMail.setText(user1.email);
                edtCell.setText(user1.celular);
                edtCPF.setText(user1.cpf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_alterarsenha:
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                if (fragment != null)
                    fragmentTransaction.remove(fragment);
                fragmentTransaction.addToBackStack(TAG_FRAGMENT);
                DialogFragment dialogFragment = new ForgetPasswd();
                dialogFragment.show(fragmentTransaction, TAG_FRAGMENT);
                break;
            case R.id.btn_cancelar:
                finish();
                break;
            case R.id.btn_salvar:
                TextFildCheck checkText = new TextFildCheck();
                if (checkText.CheckName(edtName.getText().toString()) &&
                    checkText.validEmail(edtMail.getText().toString()) &&
                    checkText.isTelefone(edtCell.getText().toString())){
                    user1.name = edtName.getText().toString();
                    user1.email = edtMail.getText().toString();
                    user1.celular = edtCell.getText().toString();
                    user1.cpf = edtCPF.getText().toString();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FireBase fireBase = new FireBase(database, mAuth);
                    fireBase.AddUser(user1);
                }
                else {
                    Toast.makeText(this, "Impossível atualizar os dados, " +
                            "por favor, confira todos os dados.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void getPhotoUser(String url){
        try{
            Picasso.with(getApplicationContext()).load(url).into(photoUser);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}