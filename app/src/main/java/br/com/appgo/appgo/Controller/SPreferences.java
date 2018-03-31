package br.com.appgo.appgo.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by hex on 30/01/18.
 */

public class SPreferences {
    Context context;
    String SP_MAP_TYPE = "map_mode_type" ;
    String SP_ATIVIDADE = "qual_sua_atividade";
    SharedPreferences mapType;
    SharedPreferences atividadeType;
    public SPreferences(Context context){
        this.context = context;
        mapType = context.getSharedPreferences(SP_MAP_TYPE, Context.MODE_PRIVATE);
        atividadeType = context.getSharedPreferences(SP_ATIVIDADE, Context.MODE_PRIVATE);

    }

    public void setAtividade(String atividade){
        SharedPreferences.Editor editor = atividadeType.edit();
        editor.putString(SP_ATIVIDADE, atividade);
        editor.commit();
    }

    public void setMapType(int sharedKeyMap){
        SharedPreferences.Editor editor = mapType.edit();
        editor.putInt(SP_MAP_TYPE, sharedKeyMap);
        editor.commit();
    }
    public int getMapType(){
        return mapType.getInt(SP_MAP_TYPE, GoogleMap.MAP_TYPE_NONE);
    }

    public String getAtividade(){
        return atividadeType.getString(SP_ATIVIDADE, "Qual a sua atividade?");
    }
}
