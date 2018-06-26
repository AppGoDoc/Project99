package br.com.appgo.appgo.controller;

import android.graphics.Bitmap;
import android.widget.ImageView;

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
    public ResizePhoto() {
        widht = 0;
        height = 0;
        resize = 0;
    }

    public int widhtResize(){
        int result = (int)resize;
        return result;
    }
    public int heightSize(){
        int result = (int)(height/(widht/resize));
        return result;
    }
    public Bitmap MinimizeBitmap(Bitmap bitmap, int scale){
        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/scale ,
                bitmap.getHeight()/scale, true);
        return bitmap;
    }
    public Bitmap resizeBitmap(Bitmap bitmap, int size){
        bitmap = Bitmap.createScaledBitmap(bitmap, size,
                size, true);
        return bitmap;
    }
    public Bitmap resizeBitmapSquare(Bitmap bitmap){
        bitmap = Bitmap.createScaledBitmap(bitmap, 96, 96, true);
        return bitmap;
    }
    public Bitmap ResizeToImage(ImageView imageView, Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap, (int) widht,
                (int) height, true);
    }

}
