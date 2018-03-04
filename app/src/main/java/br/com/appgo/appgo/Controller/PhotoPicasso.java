package br.com.appgo.appgo.Controller;

import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
/**
 * Created by hex on 03/03/18.
 */

public class PhotoPicasso {
    Context context;
    public PhotoPicasso(Context context){
        this.context = context;
    }
    public void Photo96x96(String url, ImageView imageView){
        Picasso.with(context)
                .load(url)
                .resize(96,96)
                .centerCrop()
                .into(imageView);
    }
    public void Photo24x24(String url, ImageView imageView){
        Picasso.with(context)
                .load(url)
                .resize(50,50)
                .centerCrop()
                .into(imageView);
    }
    public void Photo(String url, ImageView imageView, int width, int height){
        Picasso.with(context)
                .load(url)
                .resize(width,height)
                .centerCrop()
                .into(imageView);
    }
    public void Photo256x128(String url, ImageView imageView){
        Picasso.with(context)
                .load(url)
                .resize(600,400)
                .centerCrop()
                .into(imageView);
    }

}
