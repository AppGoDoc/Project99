package br.com.appgo.appgo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.apache.commons.lang3.SerializationUtils;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.adapter.AdapterFotoAnuncio;
import br.com.appgo.appgo.model.Loja;

import static br.com.appgo.appgo.view.ActivityAnuncio.USER_LOJA;
import static br.com.appgo.appgo.view.ActivityAnuncio.USER_LOJA_ACTION;

public class VerAnuncioActivity extends AppCompatActivity {
    RecyclerView recyclerAnuncio;
    AdapterFotoAnuncio adapterFotoAnuncio;
    RecyclerView.LayoutManager layoutManager;
    Loja loja;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_anuncio);

        loja = RequestLoja();
//        Chama o metodo que cria o recycler
        CreateRecyclerAnuncio(loja);
    }

    private void CreateRecyclerAnuncio(Loja loja){
        adapterFotoAnuncio = new AdapterFotoAnuncio(getApplicationContext(), loja);

        recyclerAnuncio = findViewById(R.id.recycler_anuncios);
        recyclerAnuncio.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerAnuncio.setLayoutManager(layoutManager);
        recyclerAnuncio.setAdapter(adapterFotoAnuncio);
        recyclerAnuncio.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

        private Loja RequestLoja() {
        Loja loja = null;
        Intent intent = getIntent();
        if (intent.getAction().equals(USER_LOJA_ACTION)){
            byte[] data = intent.getByteArrayExtra(USER_LOJA);
            loja = SerializationUtils.deserialize(data);
        }
        return loja;
    }
}
