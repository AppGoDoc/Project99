package br.com.appgo.appgo.model;

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
    public List<Comentario> comentarios;

    public Loja(){
        local = new Local();
        documento = new Documento();
        curtidas = new ArrayList<>();
        comentarios = new ArrayList<>();
    }
}