package br.com.appgo.appgo.Persistence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import br.com.appgo.appgo.R;

/**
 * Created by hex on 03/03/18.
 */

public class PhotoPicasso {
    Context context;
    Bitmap bitmap = null;
    File fileTmp;
    public PhotoPicasso(Context context){
        this.context = context;
        fileTmp = null;
    }

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

    public void PhotoMarkerDownload(String url, final Marker marker){
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        try {
            final File file = File.createTempFile("icone",".png");
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Photo24x24(String url, final Marker marker, boolean token) {
        if (token) {
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    ImageView imageView = new ImageView(context);
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
    public Uri PhotoDownload(String url, Uri uri){
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        try {
            final File file = File.createTempFile("foto",".png");
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                }
            });
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

}