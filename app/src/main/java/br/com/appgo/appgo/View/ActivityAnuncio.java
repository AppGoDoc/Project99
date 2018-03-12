package br.com.appgo.appgo.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import org.apache.commons.lang3.SerializationUtils;

import java.util.LinkedList;
import java.util.List;

import br.com.appgo.appgo.Adapter.FotosAdapter;
import br.com.appgo.appgo.Model.ListLoja;
import br.com.appgo.appgo.Model.Loja;
import br.com.appgo.appgo.R;

import static br.com.appgo.appgo.View.MainActivity.LOJA_ESCOLHIDA;
import static br.com.appgo.appgo.View.MainActivity.LOJA_ESCOLHIDA_ACTION;

/**
 * Created by hex on 09/03/18.
 */

public class ActivityAnuncio extends AppCompatActivity implements View.OnClickListener {
    Loja loja = null;
    RecyclerView carrousselFotos;
    List<String> urlFotos;
    TextView mAnuncioTitulo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);

        loja = RequestLoja();
        urlFotos = GetListFotos(loja);
        HorizontalInfiniteCycleViewPager carroussel =
                (HorizontalInfiniteCycleViewPager) findViewById(R.id.carroussel_fotos);
        FotosAdapter fotosAdapter = new FotosAdapter(urlFotos, getApplicationContext());
        carroussel.setAdapter(fotosAdapter);
        mAnuncioTitulo = (TextView)findViewById(R.id.titulo_anuncio);
        mAnuncioTitulo.setText(loja.titulo);
    }

    @Override
    public void onClick(View v) {

    }

    private Loja RequestLoja() {
        Loja loja = null;
        Intent intent = getIntent();
        if (intent.getAction() == LOJA_ESCOLHIDA_ACTION){
            byte[] data = intent.getByteArrayExtra(LOJA_ESCOLHIDA);
            loja = SerializationUtils.deserialize(data);
        }
        return loja;
    }
    private List<String> GetListFotos(Loja loja){
        List<String> urlFotos = new LinkedList<>();
        if (loja != null){
            urlFotos.add(loja.urlFoto1);
            urlFotos.add(loja.urlFoto2);
            urlFotos.add(loja.urlFoto3);
        }
        return urlFotos;
    }
}
