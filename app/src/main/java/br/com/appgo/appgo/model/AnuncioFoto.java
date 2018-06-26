package br.com.appgo.appgo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnuncioFoto implements Serializable {
    public List<Foto> fotos;
    public String data_do_anuncio;
    public String urlVideo;
    public String descricaoVideo;

    public AnuncioFoto(){
        fotos = new ArrayList<>();
        data_do_anuncio = null;
        urlVideo = null;
        descricaoVideo = null;
    }
}
