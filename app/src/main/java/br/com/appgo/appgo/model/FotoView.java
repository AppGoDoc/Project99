package br.com.appgo.appgo.model;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;

public class FotoView {
    public ImageView imageView;
    public EditText descricao;
    public Bitmap bitmap;

    public FotoView(ImageView imageView, EditText descricao, Bitmap bitmap) {
        this.imageView = imageView;
        this.descricao = descricao;
        this.bitmap = bitmap;
    }
}
