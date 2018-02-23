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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.appgo.appgo.Controller.SPreferences;
import br.com.appgo.appgo.Fragment.ConfirmLogout;
import br.com.appgo.appgo.Fragment.DialogFragmentListRamo;
import br.com.appgo.appgo.R;

/**
 * Created by hex on 21/02/18.
 */

public class CriarAnuncioActivity extends AppCompatActivity implements View.OnClickListener, DialogFragmentListRamo.ChooseRamoDialogListener {
    private static final String FRAGMENT_RAMO_ATIVIDADE = "fragment_ramo_atividade";
    private ImageView buttonLoadIco;
    private Button btnRamo;
    SPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_anuncio);

        preferences = new SPreferences(getApplicationContext());
        buttonLoadIco = (ImageView) findViewById(R.id.image_icone);
        buttonLoadIco.setOnClickListener(this);
        btnRamo = (Button)findViewById(R.id.btn_ramo);
        btnRamo.setOnClickListener(this);

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
