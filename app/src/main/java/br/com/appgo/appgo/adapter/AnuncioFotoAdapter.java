package br.com.appgo.appgo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.model.Foto;
import br.com.appgo.appgo.persistence.PhotoPicasso;

public class AnuncioFotoAdapter extends PagerAdapter {
    List<Foto> fotos;
    String urlPhotoAnnuncer, adviserName;
    Context context;
    LayoutInflater layoutInflater;
    PhotoPicasso photoPicasso;

    public AnuncioFotoAdapter(List<Foto> fotos, Context context, String UserPhoto, String adviserName) {
        this.fotos = fotos;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        photoPicasso = new PhotoPicasso(context);
        this.urlPhotoAnnuncer = UserPhoto;
        this.adviserName = adviserName;
    }

    @Override
    public int getCount() {
        return fotos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.ver_anuncio_item, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.img_anuncio);
        ImageView imageAnuncer = view.findViewById(R.id.image_anuncer);
        photoPicasso.Photo2(urlPhotoAnnuncer, imageAnuncer, 100, 100, true);
        photoPicasso.Photo2(fotos.get(position).urlFoto, imageView, 500, 700, true);
        TextView textView = view.findViewById(R.id.text_desc);
        textView.setText(fotos.get(position).descrFoto);
        TextView textView1 = view.findViewById(R.id.text_adviser);
        textView1.setText(adviserName);
        container.addView(view);
        return view;
    }
}
