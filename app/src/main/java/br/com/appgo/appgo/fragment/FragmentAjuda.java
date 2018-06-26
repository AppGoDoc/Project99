package br.com.appgo.appgo.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.appgo.appgo.R;

public class FragmentAjuda extends DialogFragment{
    String message = "Para criar um anúncio com fotos clique no botão 'FOTO'.\n\n" +
                     "Para criar um anúncio com videos clique no botão 'VIDEO'.\n\n" +
                     "Para apagar um anúncio, escolha um anúncio da lista e confirme para excluir permanentemente o anúncio.\n\n" +
                     "Para ter acesso a todas as funcionalidades de um usuário premium, clique no botão \"PREMIUM\" e contrate um plano.";
    public FragmentAjuda() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_ajuda, container, false);

        TextView txtAjuda = (TextView)view.findViewById(R.id.txt_ajuda);
        txtAjuda.setText(message);

        Button button = (Button)view.findViewById(R.id.btn_ajuda);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
