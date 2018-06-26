package br.com.appgo.appgo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.model.Foto;
import br.com.appgo.appgo.model.Loja;

public class AdapterFotoAnuncio extends RecyclerView.Adapter<FotoAnuncioHolder> {
    Context context;
    Loja loja;
    List<Foto> fotos;
    public AdapterFotoAnuncio(Context context, Loja loja) {
        this.context = context;
        this.loja = loja;
        fotos = new ArrayList<>();
    }

    @NonNull
    @Override
    public FotoAnuncioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FotoAnuncioHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ver_anuncio_carroussel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FotoAnuncioHolder holder, int position) {
        fotos = loja.AnuncioFotografico.get(position).fotos;
        AnuncioFotoAdapter fotosAdapter = new AnuncioFotoAdapter(fotos, context, loja.urlFoto1, loja.titulo);
        holder.viewPager.setAdapter(fotosAdapter);
        holder.indicatorView.setCount(fotos.size());
        holder.indicatorView.setAnimationType(AnimationType.DROP);
        holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                holder.indicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return loja.AnuncioFotografico != null ? loja.AnuncioFotografico.size() : 0;
    }
}
