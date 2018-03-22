package br.com.appgo.appgo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import br.com.appgo.appgo.Persistence.PhotoPicasso;
import br.com.appgo.appgo.R;

/**
 * Created by hex on 11/03/18.
 */

public class FotosAdapter extends PagerAdapter {
    List<String> urlFotos;
    Context context;
    LayoutInflater inflater;
    PhotoPicasso photoPicasso;
    public FotosAdapter(List<String> urlFotos, Context context) {
        this.urlFotos = urlFotos;
        this.context = context;
        inflater = LayoutInflater.from(context);
        photoPicasso = new PhotoPicasso(context);
    }

    @Override
    public int getCount() {
        return urlFotos.size();
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
        View view = inflater.inflate(R.layout.foto_carroussel_item, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.carroussel_item);
        photoPicasso.Photo(urlFotos.get(position), imageView, 740, 500, true);
        container.addView(view);
        return view;
    }
}
