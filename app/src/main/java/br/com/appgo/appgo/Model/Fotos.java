package br.com.appgo.appgo.Model;

/**
 * Created by hex on 09/02/18.
 */

class Fotos {
    private String uri;
    private String legenda;
    private String descricao;

    public Fotos(String uri, String legenda, String descricao) {
        this.uri = uri;
        this.legenda = legenda;
        this.descricao = descricao;
    }

    public Fotos() {}

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
