package br.com.appgo.appgo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import br.com.appgo.appgo.Model.Loja;
import br.com.appgo.appgo.View.CriarAnuncioActivity;

/**
 * Created by hex on 03/03/18.
 */

public class LoadAnunciosData extends Service {

    public final static String TAGZ = "BOMBOMTESTE";
    public final static String LOJA_TITULO = "titulo_loja";
    public final static String LOJA_ENDERECO = "endereco_loja";
    public final static String LOJA_WHATSAPP = "whatsapp_loja";
    public final static String LOJA_TELEFONE = "telefone_loja";
    public final static String LOJA_EMAIL = "email_loja;";
    public final static String LOJA_ICONE_URL = "icone_url_loja";
    public final static String LOJA_FOTO1_URL = "foto1_url_loja";
    public final static String LOJA_FOTO2_URL = "foto2_url_loja";
    public final static String LOJA_FOTO3_URL = "foto3_url_loja";
    public final static String LOJA_RAMO = "ramo_loja";
    public final static String LOJA_TIPO_DOCUMENTO = "tipo_documento_loja";
    public final static String LOJA_DOCUMENTO = "documento_loja";
    public static final String RECEIVER_DATA_ANUNCIO = "receive_data_anuncio";
    private Loja loja = new Loja();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(CriarAnuncioActivity.ANUNCIOS).child(auth.getUid());

    private class DataListener implements ValueEventListener{
        public DataListener(){

        }
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            loja = dataSnapshot.getValue(Loja.class);
            DataReceive(loja);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private void DataReceive(Loja loja) {
        try {
            Intent mIntent = new Intent();
            mIntent.setAction(RECEIVER_DATA_ANUNCIO);
            mIntent.putExtra(LOJA_TITULO, loja.titulo);
            mIntent.putExtra(LOJA_ENDERECO, loja.local.endereco);
            mIntent.putExtra(LOJA_TIPO_DOCUMENTO, loja.documento.tipoDocumento);
            mIntent.putExtra(LOJA_DOCUMENTO, loja.documento.documento);
            mIntent.putExtra(LOJA_WHATSAPP, loja.whatsapp);
            mIntent.putExtra(LOJA_TELEFONE, loja.telefone);
            mIntent.putExtra(LOJA_EMAIL, loja.emailAnuncio);
            mIntent.putExtra(LOJA_RAMO, loja.ramo);
            mIntent.putExtra(LOJA_ICONE_URL, loja.urlIcone);
            mIntent.putExtra(LOJA_FOTO1_URL, loja.urlFoto1);
            mIntent.putExtra(LOJA_FOTO2_URL, loja.urlFoto2);
            mIntent.putExtra(LOJA_FOTO3_URL, loja.urlFoto3);
            sendBroadcast(mIntent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataListener dataListener = new DataListener();
        reference.addValueEventListener(dataListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
