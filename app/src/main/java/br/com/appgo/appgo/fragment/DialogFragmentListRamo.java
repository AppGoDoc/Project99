package br.com.appgo.appgo.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import br.com.appgo.appgo.adapter.RamoListAdapter;
import br.com.appgo.appgo.R;

/**
 * Created by hex on 23/02/18.
 */

public class DialogFragmentListRamo extends DialogFragment {
    RamoListAdapter ramoListAdapter;
    public DialogFragmentListRamo(){}

    public interface ChooseRamoDialogListener{
        void onFinishDialogFragment(String opcaoEscolhida);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_ramo, container, false);
        final ListView listRamo = view.findViewById(R.id.list_ramo);

        ramoListAdapter= new RamoListAdapter(getActivity().getApplicationContext());
        listRamo.setAdapter(ramoListAdapter);

        listRamo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ChooseRamoDialogListener listener = (ChooseRamoDialogListener) getActivity();
                    listener.onFinishDialogFragment((String) ramoListAdapter.getItem(position));
                    dismiss();
                } catch (Exception e ){
                    e.printStackTrace();
                }
            }
        });
        return view;

    }
}
