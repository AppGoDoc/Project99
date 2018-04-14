package br.com.appgo.appgo.fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import br.com.appgo.appgo.model.User;
import br.com.appgo.appgo.R;

public class FragmentUserData extends DialogFragment{
    public FragmentUserData(){}

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference userReference = null;
    private TextView edtName, edtMail, edtCell;
    private Button btnSalvar, btnCancelar, btnAlterarSenha;
    private ImageView photoUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dados_usuario, container, false);

        GetReferenceDatabase(userReference);
        edtName = (TextView) view.findViewById(R.id.nome_usuario);
        edtMail = (TextView) view.findViewById(R.id.email_usuario);
        photoUser = (ImageView) view.findViewById(R.id.imageview_photourl);

        String facebookUserId = null;
        for(UserInfo profile : user.getProviderData()) {
            facebookUserId = profile.getUid();
        }
        String urlFoto = "https://graph.facebook.com/" + facebookUserId + "/picture?height=100";
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.LTGRAY)
                .borderWidthDp(3)
                .cornerRadiusDp(80)
                .oval(false)
                .build();
        Picasso.with(getActivity().getApplicationContext())
                .load(urlFoto)
                .fit()
                .transform(transformation)
                .into(photoUser);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        edtName.setText(user.getDisplayName());
        edtMail.setText(user.getEmail());

        return view;
    }
    private void GetReferenceDatabase(DatabaseReference reference){
        try {
            reference = FirebaseDatabase.getInstance().getReference("Anunciantes/"+ user.getUid());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }







}