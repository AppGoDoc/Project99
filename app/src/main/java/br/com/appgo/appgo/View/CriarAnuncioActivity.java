package br.com.appgo.appgo.View;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.method.TextKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import br.com.appgo.appgo.Controller.SPreferences;
import br.com.appgo.appgo.Fragment.ConfirmLogout;
import br.com.appgo.appgo.Fragment.DialogFragmentListRamo;
import br.com.appgo.appgo.R;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by hex on 21/02/18.
 */

public class CriarAnuncioActivity extends AppCompatActivity implements View.OnClickListener,
        DialogFragmentListRamo.ChooseRamoDialogListener {
    private static final String FRAGMENT_RAMO_ATIVIDADE = "fragment_ramo_atividade";
    private static final int RESULT_FIND_ADDRESS = 7;
    public static final String ADRESS_NAME = "adress_name";
    private String docType = null;
    private ImageView buttonLoadIco;
    private Button btnRamo, btnFindAddress;
    private RadioGroup docChoose;
    private EditText documento, whatsapp, telefone, email, addressName;
    SPreferences preferences;
    MaskEditTextChangedListener maskWhats, maskTel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_anuncio);

        preferences = new SPreferences(getApplicationContext());
        buttonLoadIco = (ImageView) findViewById(R.id.image_icone);
        buttonLoadIco.setOnClickListener(this);
        documento = (EditText) findViewById(R.id.documento_anuncio);
        addressName = (EditText) findViewById(R.id.adress_name);
        btnRamo = (Button)findViewById(R.id.btn_ramo);
        btnRamo.setOnClickListener(this);
        btnFindAddress = (Button) findViewById(R.id.btn_find_address);
        btnFindAddress.setOnClickListener(this);
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
                startActivityForResult(intentIco, 0);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                //Picasso.with(this).load(targetUri).into(buttonLoadIco);
                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.3), (int)(bitmap.getHeight()*0.3), true);
                buttonLoadIco.setImageBitmap(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (requestCode == RESULT_FIND_ADDRESS){
            Bundle bundle = new Bundle();
            addressName.setText((CharSequence) bundle.get(ADRESS_NAME));
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

}
