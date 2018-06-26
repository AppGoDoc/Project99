package br.com.appgo.appgo.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.appgo.appgo.R;

public class DialogFragmentConfirmDeleteAnuncio extends DialogFragment {
    Button mOk, mCancel;
    ConfirmDeleteAnuncioListener listener;
    public DialogFragmentConfirmDeleteAnuncio() {
    }
    public interface ConfirmDeleteAnuncioListener{
        void onFinishDialogFragment(boolean token);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_confirm_delete, container, false);

        listener = (ConfirmDeleteAnuncioListener) getActivity();
        mOk = view.findViewById(R.id.btn_ok_delete);
        mCancel = view.findViewById(R.id.btn_cancelar);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinishDialogFragment(false);
                dismiss();
            }
        });
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinishDialogFragment(true);
                dismiss();
            }
        });
        return view;
    }
}
