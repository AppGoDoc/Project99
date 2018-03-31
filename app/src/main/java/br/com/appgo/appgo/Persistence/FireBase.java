package br.com.appgo.appgo.Persistence;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.appgo.appgo.Model.Comentario;
import br.com.appgo.appgo.Model.Loja;

/**
 * Created by hex on 11/02/18.
 */

public class FireBase {
    Context context;
    public FireBase(Context context){
        this.context = context;
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
}
