package br.com.appgo.appgo.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.commons.lang3.SerializationUtils;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.adapter.AdapterAnuncios;
import br.com.appgo.appgo.controller.RecyclerItemClickListerner;
import br.com.appgo.appgo.fragment.DialogFragmentConfirmDeleteAnuncio;
import br.com.appgo.appgo.fragment.FragmentAjuda;
import br.com.appgo.appgo.model.AnuncioFoto;
import br.com.appgo.appgo.model.Foto;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.services.LoadAnunciosData;

import static br.com.appgo.appgo.services.LoadAnunciosData.LOJA_RECEIVE_DATA;
import static br.com.appgo.appgo.services.LoadAnunciosData.RECEIVER_DATA_ANUNCIO;
import static br.com.appgo.appgo.view.CriarAnuncioActivity.ANUNCIOS;

public class PostarActivity extends AppCompatActivity implements View.OnClickListener,
        DialogFragmentConfirmDeleteAnuncio.ConfirmDeleteAnuncioListener{
    private static final String TAG_FRAGMENT_HELP = "tag_fragment_help";
    public static final String USER_LOJA_EDIT_POST = "user_edit_post";
    public static final String USER_EDIT_POSITION = "user_edit_position";
    public static final String TAG_FRAGMENT_CONFIRM = "tag_confirm_delete";
    private Button fotoAnuncio, videoAnuncio, sejaPremium;
    private RecyclerView recyclerView;
    private FloatingActionButton mHelp;
    private AdapterAnuncios adapterAnuncios;
    private RecyclerView.LayoutManager layoutManager;
    public static final String USER_LOJA_ACTION_TO_POST = "loja usuario to post";
    public static final String USER_LOJA_TO_POST = "user loja post";
    public static final int REQUEST_FOTO_ANUNCIO = 1611;
    public static final int RESULT_FOTO_ANUNCIO = 1612;
    public static final int REQUEST_VIDEO_ANUNCIO = 1613;
    public static final int RESULT_VIDEO_ANUNCIO = 1614;
    public static final int REQUEST_PREMIUM_ANUNCIO = 1615;
    public static final int RESULT_PREMIUM_ANUNCIO = 1616;
    public static final int REQUEST_EDIT_FOTO_ANUNCIO = 1617;
    public static final int RESULT_EDIT_FOTO_ANUNCIO = 1618;
    private IntentFilter intentFilterDataAnuncio;
    private Intent serviceDataReceiveAnuncio;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    DatabaseReference database;
    int getPosition;
    Loja loja;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.postar_activity);
        super.onCreate(savedInstanceState);

        mHelp = (FloatingActionButton)findViewById(R.id.help_button);
        mHelp.setOnClickListener(this);
        fotoAnuncio = (Button)findViewById(R.id.btn_foto);
        fotoAnuncio.setOnClickListener(this);
        videoAnuncio = (Button)findViewById(R.id.btn_video);
        videoAnuncio.setOnClickListener(this);
        sejaPremium = (Button)findViewById(R.id.btn_premium);
        sejaPremium.setOnClickListener(this);
        intentFilterDataAnuncio = new IntentFilter();
        intentFilterDataAnuncio.addAction(RECEIVER_DATA_ANUNCIO);
        serviceDataReceiveAnuncio = new Intent(this, LoadAnunciosData.class);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getPosition = -1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_foto:
//                if (loja.AnuncioFotografico.size() <= NUMERO_MAX_ANUNCIOS_FOTOS){
                    Intent intent = new Intent(getApplicationContext(), CriarAnuncioFoto.class);
                    intent.setAction(USER_LOJA_ACTION_TO_POST);
                    byte[] data = SerializationUtils.serialize(loja);
                    intent.putExtra(USER_LOJA_TO_POST, data);
                    startActivityForResult(intent, REQUEST_FOTO_ANUNCIO);
//                }
//                else {
//                    Toast.makeText(PostarActivity.this,
//                            "Você atingiu o número máximo de anúncios.", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.btn_video:
                Intent intentVideo = new Intent(getApplicationContext(), CriarAnuncioVideo.class);
                intentVideo.setAction(USER_LOJA_ACTION_TO_POST);
                byte[] dataVideo = SerializationUtils.serialize(loja);
                intentVideo.putExtra(USER_LOJA_TO_POST, dataVideo);
                startActivityForResult(intentVideo, REQUEST_VIDEO_ANUNCIO);
                break;
            case R.id.btn_premium:

                break;
            case R.id.help_button:
                DialogFragment dialogFragment = new FragmentAjuda();
                dialogFragment.show(dialogCall(TAG_FRAGMENT_HELP), TAG_FRAGMENT_HELP);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_FOTO_ANUNCIO:
                if (resultCode == RESULT_FOTO_ANUNCIO){
                    Toast.makeText(this, "Anuncio de fotos realizado com sucesso", Toast.LENGTH_SHORT).show();
                }
                if (resultCode == -1){
                    Toast.makeText(this, "Falha ao realizar o anúncio, tente novamente.", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_VIDEO_ANUNCIO:
                break;
            case REQUEST_PREMIUM_ANUNCIO:
                break;
            case REQUEST_EDIT_FOTO_ANUNCIO:
                    CreateRecycleView(loja);
                    Toast.makeText(this, "Anúncio Editado com sucesso!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(serviceDataReceiveAnuncio);
        registerReceiver(mReceiverDataAnuncio, intentFilterDataAnuncio);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiverDataAnuncio);
        stopService(serviceDataReceiveAnuncio);
    }

    private BroadcastReceiver mReceiverDataAnuncio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == RECEIVER_DATA_ANUNCIO) {
                    loja = (Loja) intent.getSerializableExtra(LOJA_RECEIVE_DATA);
                    CreateRecycleView(loja);
            }
        }
    };

    private void CreateRecycleView(Loja loja){
        adapterAnuncios = new AdapterAnuncios(loja, getApplicationContext());

        recyclerView = (RecyclerView)findViewById(R.id.recycler_lista_anuncios);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterAnuncios);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        RecyclerViewTouched(recyclerView);

    }
    private void RecyclerViewTouched(final RecyclerView recyclerView){
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListerner(getApplicationContext(), recyclerView, new RecyclerItemClickListerner.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        getPosition = position;
                        DialogFragment dialogFragment = new DialogFragmentConfirmDeleteAnuncio();
                        dialogFragment.show(dialogCall(TAG_FRAGMENT_CONFIRM), TAG_FRAGMENT_CONFIRM);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    private void RemoveAnuncio(AnuncioFoto anuncioFoto, int position) {
        for (final Foto foto: anuncioFoto.fotos){
            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(foto.urlFoto);
            reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(PostarActivity.this, "Falha ao apagar o anúncio.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        try{
            StorageReference videoReference = FirebaseStorage.getInstance().getReferenceFromUrl(anuncioFoto.urlVideo);
            videoReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(PostarActivity.this, "Falha ao apagar o anúncio.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        loja.AnuncioFotografico.remove(position);
        database.child(ANUNCIOS).child(auth.getCurrentUser().getUid()).setValue(loja);
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
    public void onFinishDialogFragment(boolean token) {
        if (token && getPosition > -1){
            RemoveAnuncio(loja.AnuncioFotografico.get(getPosition), getPosition);
            CreateRecycleView(loja);
        }
        else {
            getPosition = -1;
        }
    }
}