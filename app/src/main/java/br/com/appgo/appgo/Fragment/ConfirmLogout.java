package br.com.appgo.appgo.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.appgo.appgo.R;

/**
 * Created by hex on 21/02/18.
 */

public class ConfirmLogout extends DialogFragment implements View.OnClickListener {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    public ConfirmLogout() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dfragment_confirm_logout, container, false);
        TextView txtConfirmLogout = (TextView)view.findViewById(R.id.txt_confirm_logout);
        Button btnCancelar = (Button)view.findViewById(R.id.btn_cancelar);
        Button btnOk = (Button)view.findViewById(R.id.btn_ok);

        txtConfirmLogout.setText("Deseja realizar logout de\n"+ user.getDisplayName() + "?");
        btnCancelar.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancelar:
                getActivity().getFragmentManager().popBackStack();
                break;
            case R.id.btn_ok:
                auth.signOut();
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }
}
