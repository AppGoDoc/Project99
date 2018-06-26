package br.com.appgo.appgo.adapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rd.PageIndicatorView;

import br.com.appgo.appgo.R;

public class FotoAnuncioHolder extends RecyclerView.ViewHolder {
    ViewPager viewPager;
    PageIndicatorView indicatorView;
    TextView mTxtDesc;
    public FotoAnuncioHolder(View itemView) {
        super(itemView);
        viewPager = itemView.findViewById(R.id.carroussel_fotos_anuncio);
        indicatorView = itemView.findViewById(R.id.indicator_page);
    }
}
