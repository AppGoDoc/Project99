package br.com.appgo.appgo.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.view.CriarAnuncioActivity;

/**
 * Created by hex on 03/03/18.
 */

public class LoadAnunciosData extends Service {

    public static final String RECEIVER_DATA_ANUNCIO = "receive_data_anuncio";
    public static final String LOJA_RECEIVE_DATA = "loja_receive_data";
    private Loja loja = new Loja();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(CriarAnuncioActivity.ANUNCIOS).child(user.getUid());

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
            mIntent.putExtra(LOJA_RECEIVE_DATA, loja);
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
