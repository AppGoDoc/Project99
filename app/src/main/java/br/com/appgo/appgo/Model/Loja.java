package br.com.appgo.appgo.Model;

/**
 * Created by hex on 27/02/18.
 */

public class Loja {
    public String userId, titulo, cpf, cnpj, whatsapp, telefone, emailAnuncio,
    ramo, urlIcone, urlFoto1, urlFoto2, urlFoto3;
    public Local local = new Local();
    public Loja(){
        titulo = cpf = cnpj = whatsapp = telefone = emailAnuncio = userId =
                ramo = urlIcone = urlFoto1 = urlFoto2 = urlFoto3 = null;
    }
}