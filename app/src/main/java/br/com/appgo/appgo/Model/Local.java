package br.com.appgo.appgo.Model;

import android.util.Log;

/**
 * Created by hex on 09/02/18.
 */

public class Local {
    public String endereco;
    public String enderecoObservacao;
    public double latitude;
    public double longitude;

    public Local(){ }

    public Local(String endereco, String enderecoObservacao, double latitude, double longitude) {
        this.endereco = endereco;
        this.enderecoObservacao = enderecoObservacao;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
