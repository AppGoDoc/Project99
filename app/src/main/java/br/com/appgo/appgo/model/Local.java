package br.com.appgo.appgo.model;

import java.io.Serializable;

/**
 * Created by hex on 09/02/18.
 */

public class Local implements Serializable{
    public String endereco;
    public String enderecoObservacao;
    public double latitude;
    public double longitude;

    public Local(){
        endereco = "";
        enderecoObservacao = "";
        latitude = 0.0f;
        longitude = 0.0f;
    }

    public Local(String endereco, String enderecoObservacao, double latitude, double longitude) {
        this.endereco = endereco;
        this.enderecoObservacao = enderecoObservacao;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
