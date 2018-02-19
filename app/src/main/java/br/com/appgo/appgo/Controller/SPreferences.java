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
    String SP_LOCALIZACAO = "qual_sua_atividade";
    SharedPreferences mapType;
    SharedPreferences localizacaoType;
    public SPreferences(Context context){
        this.context = context;
        mapType = context.getSharedPreferences(SP_MAP_TYPE, Context.MODE_PRIVATE);
        localizacaoType = context.getSharedPreferences(SP_LOCALIZACAO, Context.MODE_PRIVATE);
    }

    public void setLocalizacaoType(String atividade){
        SharedPreferences.Editor editor = localizacaoType.edit();
        editor.putString(SP_LOCALIZACAO, atividade);
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

    public String getLocalizacao(){
        return localizacaoType.getString(SP_LOCALIZACAO, "Qual a sua atividade?");
    }
}
