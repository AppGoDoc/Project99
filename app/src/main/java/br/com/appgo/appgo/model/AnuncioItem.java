package br.com.appgo.appgo.model;

import android.graphics.Bitmap;

public class AnuncioItem {
    public Bitmap foto;
    public String descricao;

    public AnuncioItem(Bitmap foto, String descricao) {
        this.foto = foto;
        this.descricao = descricao;
    }

    public AnuncioItem() {
    }
}
