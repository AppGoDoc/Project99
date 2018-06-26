package br.com.appgo.appgo.controller;

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
    String SP_MARKER_SIZE = "icone_marker_size";
    String SP_IDIOMA_APP = "idioma_app";
    SharedPreferences mapType;
    SharedPreferences atividadeType;
    SharedPreferences intValueShared;
    SharedPreferences intIdioma;
    public SPreferences(Context context){
        this.context = context;
        mapType = context.getSharedPreferences(SP_MAP_TYPE, Context.MODE_PRIVATE);
        atividadeType = context.getSharedPreferences(SP_ATIVIDADE, Context.MODE_PRIVATE);
        intValueShared = context.getSharedPreferences(SP_MARKER_SIZE, Context.MODE_PRIVATE);
        intIdioma = context.getSharedPreferences(SP_IDIOMA_APP, Context.MODE_PRIVATE);
    }

    public void setIdioma(int value){
        SharedPreferences.Editor editor = intIdioma.edit();
        editor.putInt(SP_IDIOMA_APP, value);
        editor.commit();
    }
    public int getIdioma(){
        return intIdioma.getInt(SP_IDIOMA_APP, 0);
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
        return mapType.getInt(SP_MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL);
    }

    public String getAtividade(){
        return atividadeType.getString(SP_ATIVIDADE, "Qual a sua atividade?");
    }
    public void SetIntShared(int value){
        SharedPreferences.Editor editor = intValueShared.edit();
        editor.putInt(SP_MARKER_SIZE, value);
        editor.commit();
    }
    public int GetIntShared(){
        return intValueShared.getInt(SP_MARKER_SIZE, 150);
    }
}