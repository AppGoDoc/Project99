package br.com.appgo.appgo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.appgo.appgo.R;

public class ListaAnuncioFotoHolder extends RecyclerView.ViewHolder {
    public TextView txtData;
    public ImageView foto1, foto2, foto3, foto4,foto5, foto6, foto7;
    public List<ImageView> fotos;
    public ListaAnuncioFotoHolder(View itemView) {
        super(itemView);
        txtData = itemView.findViewById(R.id.txt_lista_item);
        foto1 = itemView.findViewById(R.id.image_anuncio1);
        foto2 = itemView.findViewById(R.id.image_anuncio2);
        foto3 = itemView.findViewById(R.id.image_anuncio3);
        foto4 = itemView.findViewById(R.id.image_anuncio4);
        foto5 = itemView.findViewById(R.id.image_anuncio5);
        foto6 = itemView.findViewById(R.id.image_anuncio6);
        foto7 = itemView.findViewById(R.id.image_anuncio7);
        fotos = new ArrayList<>();
        fotos.add(foto1);
        fotos.add(foto2);
        fotos.add(foto3);
        fotos.add(foto4);
        fotos.add(foto5);
        fotos.add(foto6);
        fotos.add(foto7);
    }
}
