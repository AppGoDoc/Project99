package br.com.appgo.appgo.Model;

import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hex on 27/02/18.
 */

public class Loja implements Serializable {
    public String userId, titulo, whatsapp, telefone, emailAnuncio, ramo, urlIcone, urlFoto1,
            urlFoto2, urlFoto3, anunciante;
    public Local local;
    public Documento documento;
    public List<String> curtidas;

    public Loja(){
        local = new Local();
        documento = new Documento();
        curtidas = new ArrayList<>();
    }
}