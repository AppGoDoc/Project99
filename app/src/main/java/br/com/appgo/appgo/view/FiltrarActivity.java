package br.com.appgo.appgo.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.fragment.DialogFragmentListRamo;

import static br.com.appgo.appgo.view.CriarAnuncioActivity.FRAGMENT_RAMO_ATIVIDADE;

public class FiltrarActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, DialogFragmentListRamo.ChooseRamoDialogListener {
    public static final String RADIUS_SEARCH = "radius_search";
    public static final String RAMO_SEARCH = "ramo_search";
    public static final int RESULT_SEARCH_FILTER = 1515;
    public static final int RESULT_RESET_SEARCH = 1516;
    private Button mFiltrar, mRamo;
    private SeekBar mKilometers;
    private TextView mKiloVisio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar);

        mFiltrar = findViewById(R.id.btn_filter);
        mFiltrar.setOnClickListener(this);
        mRamo = findViewById(R.id.btn_choose_ramo);
        mRamo.setOnClickListener(this);
        mKiloVisio = findViewById(R.id.txt_kilometers);
        mKilometers = findViewById(R.id.seekbar_kilometers);
        mKilometers.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_filter:
                Intent filtrarIntent = new Intent();
                filtrarIntent.putExtra(RAMO_SEARCH, mRamo.getText().toString());
                filtrarIntent.putExtra(RADIUS_SEARCH, String.format("%s",mKilometers.getProgress()));
                setResult(RESULT_SEARCH_FILTER, filtrarIntent);
                finish();
                break;
            case R.id.btn_choose_ramo:
                DialogFragment dialogFragment = new DialogFragmentListRamo();
                dialogFragment.show(dialogCall(FRAGMENT_RAMO_ATIVIDADE), FRAGMENT_RAMO_ATIVIDADE);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mKiloVisio.setText(String.format("%sKm", progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onFinishDialogFragment(String opcaoEscolhida) {
        mRamo.setText(opcaoEscolhida);
    }

    public FragmentTransaction dialogCall(String Tag){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(Tag);
        if (fragment != null)
            fragmentTransaction.remove(fragment);
        fragmentTransaction.addToBackStack(Tag);
        return fragmentTransaction;
    }
}
