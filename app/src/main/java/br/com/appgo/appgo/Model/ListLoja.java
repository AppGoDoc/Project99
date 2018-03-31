package br.com.appgo.appgo.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hex on 05/03/18.
 */

public class ListLoja implements Serializable {
    public List<Loja> lojas;

    public ListLoja() {
        lojas = new ArrayList<Loja>();
    }
}
