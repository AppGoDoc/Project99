package br.com.appgo.appgo.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import br.com.appgo.appgo.R;

/**
 * Created by hex on 03/03/18.
 */

public class CombineImages {
    Context context;
    public CombineImages(Context context){
        this.context = context;
    }
    public Bitmap createIco(Bitmap bitmap1){
        Bitmap bitMoldura = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.marker_appgo);
        Bitmap bit1 = Bitmap.createBitmap(bitMoldura, 0, 0, bitMoldura.getWidth(), bitMoldura.getHeight(),
                null, true);
        Bitmap cs = Bitmap.createBitmap(bitMoldura.getWidth(), bitMoldura.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(bit1, 0.0f, 0.0f, null);
        comboImage.drawBitmap(bitmap1, 15.0f, 12.5f, null);
        return cs;
    }
    public Bitmap createMarkeAppGo(Bitmap bitmap1){
        Bitmap bitMoldura = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.logo_appgo);
        Bitmap cs = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(bitmap1, 0.0f, 0.0f, null);
        comboImage.drawBitmap(
                Bitmap.createScaledBitmap(bitMoldura,
                        (int)(bitmap1.getWidth()*0.2), (int)(bitmap1.getWidth()*0.2), true),
                4.0f, 4.0f, null);
        return cs;
    }
}
