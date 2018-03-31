package br.com.appgo.appgo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.apache.commons.lang3.SerializationUtils;
import java.io.Serializable;
import br.com.appgo.appgo.Model.ListLoja;
import br.com.appgo.appgo.Model.Loja;
import static br.com.appgo.appgo.Constants.StringConstans.ACTION_RECEIVE_MARKER;
import static br.com.appgo.appgo.Constants.StringConstans.LOJAS_LIST_RECEIVE;
import static br.com.appgo.appgo.View.CriarAnuncioActivity.ANUNCIOS;

/**
 * Created by hex on 05/03/18.
 */

public class LoadMarkers extends Service implements Serializable{
    public static final String LOJAS_LIST_BUNDLE = "lojas_list_bundle";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ANUNCIOS);


    private class ReceiveData implements ValueEventListener{

        protected ReceiveData(){}

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ListLoja listLoja = new ListLoja();
            for (DataSnapshot listaSnapshot : dataSnapshot.getChildren()){
                listLoja.lojas.add(listaSnapshot.getValue(Loja.class));
            }
            SendLojas(listLoja);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "Falha ao carregar dados do servidor.\n",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void SendLojas(ListLoja listLoja){
        Intent intent = new Intent();
        intent.setAction(ACTION_RECEIVE_MARKER);
        byte[] data = SerializationUtils.serialize(listLoja);
        intent.putExtra(LOJAS_LIST_RECEIVE, data);
        sendBroadcast(intent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LoadMarkers.ReceiveData receiveData = new ReceiveData();
        reference.addValueEventListener(receiveData);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}