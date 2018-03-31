package br.com.appgo.appgo.Fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import br.com.appgo.appgo.R;
import br.com.appgo.appgo.View.RegisterActivity;

/**
 * Created by hex on 16/03/18.
 */

public class DialogFragmentLogin extends DialogFragment implements View.OnClickListener{
    public DialogFragmentLogin() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_login, container, false);
        EditText email = (EditText)view.findViewById(R.id.edt_email);
        EditText passwd = (EditText)view.findViewById(R.id.edt_passwd);
        Button login = (Button) view.findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        Button forgetPass = (Button)view.findViewById(R.id.btn_forgetpasswd);
        forgetPass.setOnClickListener(this);
        Button register = (Button)view.findViewById(R.id.btn_registrar);
        register.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                break;
            case R.id.btn_forgetpasswd:
                break;
            case R.id.btn_registrar:
                Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }
}
