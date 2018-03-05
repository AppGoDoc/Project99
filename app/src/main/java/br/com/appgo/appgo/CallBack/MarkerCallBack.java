package br.com.appgo.appgo.CallBack;

import android.content.Context;
import android.widget.ImageView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.appgo.appgo.Controller.PhotoPicasso;

/**
 * Created by hex on 05/03/18.
 */

public class MarkerCallBack implements Callback {
    Marker marker = null;
    String url;
    ImageView imageView;
    Context context;

    public MarkerCallBack(Marker marker, String url, ImageView imageView, Context context) {
        this.marker = marker;
        this.url = url;
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    public void onSuccess() {
        if (marker != null) {
            PhotoPicasso picasso = new PhotoPicasso(context);
            picasso.Photo24x24(url, imageView, true);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(imageView.getDrawingCache()));
        }
    }

    @Override
    public void onError() {

    }
}
