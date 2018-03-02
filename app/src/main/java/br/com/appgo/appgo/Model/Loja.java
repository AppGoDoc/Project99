package br.com.appgo.appgo.Model;

/**
 * Created by hex on 27/02/18.
 */

public class Loja {
    public String userId, titulo, whatsapp, telefone, emailAnuncio, ramo, urlIcone, urlFoto1,
            urlFoto2, urlFoto3;
    public Local local;
    public Documento documento;

    public Loja(){
        local = new Local();
        documento = new Documento();

    }
}