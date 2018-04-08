package br.com.appgo.appgo.persistence;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.com.appgo.appgo.model.Comentario;

/**
 * Created by hex on 11/02/18.
 */

public class FireBase implements Serializable{
    DatabaseReference database;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Context context;
    public FireBase(Context context){
        this.context = context;
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    public List<Comentario> GetListComentario(DatabaseReference reference){
        final List<Comentario> comentarios = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot comentario: dataSnapshot.getChildren()){
                    comentarios.add(comentario.getValue(Comentario.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return comentarios;
    }

    public String GetToken(){
        String tokenFirebase = null;
        try {
            tokenFirebase = FirebaseInstanceId.getInstance().getToken();
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return tokenFirebase;
    }
    public boolean FirebaseConect(){
        if (GetToken() != null){
            return true;
        }
        else
            return false;
    }
}
