package br.com.appgo.appgo.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import br.com.appgo.appgo.R;

/**
 * Created by hex on 03/03/18.
 */

public class PhotoPicasso {
    Context context;
    private Bitmap bitmap = null;

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
    public void PhotoDownload(String url){
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        try {
            final File file = File.createTempFile("icone","png");
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}