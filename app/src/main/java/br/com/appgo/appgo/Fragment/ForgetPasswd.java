package br.com.appgo.appgo.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import br.com.appgo.appgo.R;

/**
 * Created by hex on 11/02/18.
 */

public class ForgetPasswd extends DialogFragment {
    private static final String LOG_EVENT = "ForgetPasswd Fragment";

    public ForgetPasswd(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_EVENT, "onCreate forgetPasswd");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

//        Task task = (Task) getArguments().getSerializable(OBJECT_SERIALIZABLE);
        View view = inflater.inflate(R.layout.activity_forgetpasswd, container, false);

        final EditText email = view.findViewById(R.id.edt_login);
        Button submit = view.findViewById(R.id.submit_email);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Digite um endereço de e-mail", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "e-mail enviado!", Toast.LENGTH_SHORT).show();
                                        getActivity().getFragmentManager().popBackStack();
                                    } else {
                                        Toast.makeText(getActivity(),"Erro: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }


}
