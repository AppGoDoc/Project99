package br.com.appgo.appgo.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.google.firebase.database.DatabaseReference;

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
}
