package br.com.appgo.appgo.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.googledirection.constant.TransportMode;

import br.com.appgo.appgo.R;

public class DialogFragmentTransportType extends DialogFragment {
    CardView walk, bike, bus, car;
    ChooseTransportTypeDialogListener listener;
    public DialogFragmentTransportType() {
    }
    public interface ChooseTransportTypeDialogListener {
        void onFinishDialogFragment(String transportType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_select_route, container, false);
        listener = (ChooseTransportTypeDialogListener) getActivity();
        walk = (CardView)view.findViewById(R.id.card_ape);
        car = (CardView)view.findViewById(R.id.card_car);
        bike = (CardView)view.findViewById(R.id.card_bike);
        bus = (CardView)view.findViewById(R.id.card_bus);
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinishDialogFragment(TransportMode.WALKING);
                dismiss();
            }
        });
        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinishDialogFragment(TransportMode.BICYCLING);
                dismiss();
            }
        });
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinishDialogFragment(TransportMode.TRANSIT);
                dismiss();
            }
        });
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinishDialogFragment(TransportMode.DRIVING);
                dismiss();
            }
        });
        return view;
    }
}
