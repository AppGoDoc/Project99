package br.com.appgo.appgo.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import br.com.appgo.appgo.controller.TextFildCheck;

/**
 * Created by hex on 16/03/18.
 */

public class DialogFragmentForgetPasswd extends DialogFragment {
    View view;
    EditText email;
    Button sendEmail;
    public DialogFragmentForgetPasswd() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_forget_passwd, container, false);
        email = view.findViewById(R.id.email_request_passwd);
        sendEmail = view.findViewById(R.id.btn_send_request);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestChangePasswd(email.getText().toString());
            }
        });
        return view;
    }

    private void RequestChangePasswd(String emailUser) {
        TextFildCheck fildCheck = new TextFildCheck();
        if (fildCheck.validEmail(emailUser)){
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(emailUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity().getApplicationContext(), "Verifique seu e-mail, " +
                                        "um e-mail foi enviado para recuperar sua senha.", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                            else{
                                Toast.makeText(getActivity().getApplicationContext(), "Problema no envio do e-mail de " +
                                        "recuperação, por favor cheque seu endereço de e-mail., ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else{
                Toast.makeText(getActivity().getApplicationContext(), "Digite o e-mail para recuperar a senha " +
                        "do usuário.", Toast.LENGTH_SHORT).show();
        }
    }
}
