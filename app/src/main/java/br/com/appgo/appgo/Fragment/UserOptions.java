package br.com.appgo.appgo.Fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.View.AnuncianteDados;
import br.com.appgo.appgo.View.SearchOnMapActivity;

/**
 * Created by hex on 12/02/18.
 */

public class UserOptions extends DialogFragment implements View.OnClickListener{
    private static final String LOG_EVENT = "UserOptions Fragment";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public UserOptions(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_EVENT, "onCreate forgetPasswd");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_useroptions, container, false);
        Button minhaLocalizacao = view.findViewById(R.id.btn_minhalocal);
        Button meusDados = view.findViewById(R.id.btn_meusdados);
        Button anuncioPremium = view.findViewById(R.id.btn_anunciopremium);
        Button sair = view.findViewById(R.id.btn_sair);

        minhaLocalizacao.setOnClickListener(this);
        meusDados.setOnClickListener(this);
        anuncioPremium.setOnClickListener(this);
        sair.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_minhalocal:
                Intent it = new Intent(getActivity(), SearchOnMapActivity.class);
                startActivity(it);
                break;
            case R.id.btn_meusdados:
                Intent intent = new Intent(getActivity(), AnuncianteDados.class);
                startActivity(intent);
                break;
            case R.id.btn_anunciopremium:
                break;
            case R.id.btn_sair:
                auth.signOut();
                getActivity().getFragmentManager().popBackStack();
                break;
        }
    }
}
