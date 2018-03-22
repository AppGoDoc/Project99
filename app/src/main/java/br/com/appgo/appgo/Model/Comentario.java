package br.com.appgo.appgo.Model;

import java.io.Serializable;

/**
 * Created by hex on 18/03/18.
 */

public class Comentario implements Serializable {
    public String name;
    public String coment;
    public String urlPhoto;

    public Comentario(){

    }

    public Comentario(String name, String coment, String urlPhoto) {
        this.name = name;
        this.coment = coment;
        this.urlPhoto = urlPhoto;
    }
}
