package br.com.appgo.appgo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import br.com.appgo.appgo.R;

public class PostarActivity extends AppCompatActivity implements View.OnClickListener {
    private Button fotoAnuncio, videoAnuncio, sejaPremium;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.postar_activity);
        super.onCreate(savedInstanceState);
        fotoAnuncio = (Button)findViewById(R.id.btn_foto);
        videoAnuncio = (Button)findViewById(R.id.btn_video);
        sejaPremium = (Button)findViewById(R.id.btn_premium);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_foto:
                Intent intent = new Intent(getApplicationContext(), CriarAnuncioFoto.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_video:
                break;
            case R.id.btn_premium:
                break;
        }

    }
}
