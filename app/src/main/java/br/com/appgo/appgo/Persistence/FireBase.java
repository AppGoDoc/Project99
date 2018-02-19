package br.com.appgo.appgo.Persistence;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.appgo.appgo.Model.Local;
import br.com.appgo.appgo.Model.User;

/**
 * Created by hex on 11/02/18.
 */

public class FireBase {
    private static String ANUNCIANTES = "Anunciantes";
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference userData;
    public FireBase(FirebaseDatabase database, FirebaseAuth mAuth){
        this.mAuth = mAuth;
        this.database = database;
        userData = database.getReference();
    }
    public void AddUser(User user){
        userData.child(ANUNCIANTES).child(mAuth.getUid()).setValue(user);
    }
}
