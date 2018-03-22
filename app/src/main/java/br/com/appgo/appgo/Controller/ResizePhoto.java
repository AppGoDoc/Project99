package br.com.appgo.appgo.Controller;

import android.graphics.Bitmap;

import com.google.zxing.client.result.ParsedResult;

import static java.lang.Double.valueOf;

/**
 * Created by hex on 21/02/18.
 */

public class ResizePhoto {
    double widht, height, resize;
    public ResizePhoto(int widht, int height, int resize){
        this.widht = (double) widht;
        this.height = (double) height;
        this.resize = (double) resize;
    }
    public int widhtResize(){
        int result = (int)resize;
        return result;
    }
    public int heightSize(){
        int result = (int)(height/(widht/resize));
        return result;
    }
    public Bitmap resizeBitmap(Bitmap bitmap){
        bitmap = Bitmap.createScaledBitmap(bitmap, widhtResize(),
                heightSize(), true);
        return bitmap;
    }
    public Bitmap resizeBitmapSquare(Bitmap bitmap){
        bitmap = Bitmap.createScaledBitmap(bitmap, 96, 96, true);
        return bitmap;
    }
}
