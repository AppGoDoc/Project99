package br.com.appgo.appgo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Arrays;
import br.com.appgo.appgo.model.User;
import br.com.appgo.appgo.R;

public class LoginActivity extends AppCompatActivity{
    private static final int RC_SIGN_IN = 123;
    FirebaseUser user;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// ...
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setLogo(R.drawable.shop_icon)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                 user = FirebaseAuth.getInstance().getCurrentUser();
                 User mUser = new User(user.getPhotoUrl().toString(), user.getDisplayName(), user.getEmail());
                 reference.child("Users").child(user.getUid()).setValue(mUser);
            } else {
                Toast.makeText(this, "Falha ao realizar Login...", Toast.LENGTH_SHORT).show();
            }
            if (user != null){
                finish();
            }
        }
    }
}
