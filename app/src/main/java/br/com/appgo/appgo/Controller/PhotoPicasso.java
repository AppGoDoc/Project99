package br.com.appgo.appgo.Controller;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import br.com.appgo.appgo.R;

/**
 * Created by hex on 03/03/18.
 */

public class PhotoPicasso {
    Context context;

    public PhotoPicasso(Context context){this.context = context;}

    public void PhotoOrigSize(String url, final ImageView imageView, boolean token) {
        if (token) {
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri.toString())
                            .placeholder(R.drawable.ic_bar)
                            .error(R.drawable.error_image)
                            .into(imageView);
                }
            });
        }
    }
    public void Photo24x24(String url, final ImageView imageView, boolean token) {
        if (token) {
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri.toString())
                            .placeholder(R.drawable.ic_bar)
                            .error(R.drawable.error_image)
                            .resize(24, 24)
                            .centerCrop()
                            .into(imageView);
                }
            });
        }
    }

    public void Photo(String url, final ImageView imageView, final int width, final int height, boolean token){
        if (token){
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri.toString())
                            .placeholder(R.drawable.ic_bar)
                            .error(R.drawable.error_image)
                            .resize(width, height)
                            .centerCrop()
                            .into(imageView);
                }
            });
        }
    }
    public void Photo600x400(String url, final ImageView imageView, boolean tokenA){
        if (tokenA){
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri.toString())
                            .placeholder(R.drawable.ic_bar)
                            .error(R.drawable.error_image)
                            .resize(600, 400)
                            .centerCrop()
                            .into(imageView);
                }
            });
        }
    }
}