package br.com.appgo.appgo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import br.com.appgo.appgo.adapter.AdapterComments;
import br.com.appgo.appgo.model.Comentario;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.FireBase;
import br.com.appgo.appgo.R;
import static br.com.appgo.appgo.view.ActivityAnuncio.LOJA_COMENT;
import static br.com.appgo.appgo.view.CriarAnuncioActivity.ANUNCIOS;

public class ComentActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private AdapterComments adapterComments;
    private RecyclerView.LayoutManager layoutManager;
    private Button mPublicar;
    private ImageView imageUser;
    private EditText edtComment;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Loja loja;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ANUNCIOS);
    FireBase fireBase = new FireBase(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Comentários");     //Titulo para ser exibido na sua Action Bar em frente à seta
        Intent intent = getIntent();
        loja = (Loja) intent.getSerializableExtra(LOJA_COMENT);
        mPublicar = (Button)findViewById(R.id.btn_publicar);
        edtComment = (EditText)findViewById(R.id.edit_comment);
        imageUser = (ImageView)findViewById(R.id.image_user);
        Picasso.with(this)
                .load(user.getPhotoUrl())
                .into(imageUser);
        loja.Comentario = fireBase.GetListComentario(
                reference.child(loja.anunciante).child("Comentario"));
        adapterComments = new AdapterComments(loja.Comentario, getApplicationContext());

        recyclerView = (RecyclerView)findViewById(R.id.recycler_comments);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterComments);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        mPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtComment.getText().toString().isEmpty()){
                    Comentario comentario = new Comentario();
                    comentario.coment = edtComment.getText().toString();
                    comentario.name = user.getDisplayName();
                    comentario.urlPhoto = user.getPhotoUrl().toString();
                    loja.Comentario.add(comentario);
                    reference.child(loja.anunciante).child("Comentario").setValue(loja.Comentario);
                    edtComment.setText("");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}

