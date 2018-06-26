package br.com.appgo.appgo.persistence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;

import br.com.appgo.appgo.R;

/**
 * Created by hex on 03/03/18.
 */

public class PhotoPicasso {
    public static final String FAIL_MESSAGE = "fail";
    Context context;
    Bitmap bitmap = null;
    File fileTmp;
    Uri urlSharePic;
    String photoUrl;
    public PhotoPicasso(Context context){
        this.context = context;
        fileTmp = null;
        urlSharePic = null;
        photoUrl = null;
    }

    public void PhotoOrigSize(String url, final ImageView imageView, boolean token) {
        final Bitmap bitmap = null;
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
    public void Photo18x18(String url, final ImageView imageView, boolean token) {
        if (token) {
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri.toString())
                            .placeholder(R.drawable.ic_bar)
                            .error(R.drawable.error_image)
                            .resize(18, 18)
                            .centerCrop()
                            .into(imageView);
                }
            });
        }
    }
    public void Photo18x18(String url, final ImageView imageView, final int size, boolean token) {
        if (token) {
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri.toString())
                            .placeholder(R.drawable.ic_bar)
                            .error(R.drawable.error_image)
                            .resize(size, size)
                            .centerInside()
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
//                            .resize(width, height)
//                            .centerInside()
//                            .fit()
                            .into(imageView);
                }
            });
        }
    }
    public void Photo2(String url, final ImageView imageView, final int width, final int height, boolean token){
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
                            .centerInside()
                            .into(imageView);
                }
            });
        }
    }
    public void Photo2fit(String url, final ImageView imageView, final int width, final int height, boolean token){
        if (token){
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri.toString())
                            .placeholder(R.drawable.ic_bar)
                            .error(R.drawable.error_image)
                            .fit()
                            .into(imageView);
                }
            });
        }
    }
    public void PhotoUser(String url, final ImageView imageView){
        final Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.LTGRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(15)
                .oval(false)
                .build();
//        StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
//        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(url)
                        .transform(transformation)
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .resize(90, 90)
                        .centerCrop()
                        .into(imageView);
//            }
//        });
    }
    public void PhotoBorderless(String url, final ImageView imageView, boolean tokenA){
        if (tokenA){
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri.toString())
                            .placeholder(R.drawable.ic_bar)
                            .error(R.drawable.error_image)
                            .resize(imageView.getWidth(), imageView.getHeight())
                            .centerCrop()
                            .into(imageView);
                }
            });
        }
    }

    public void PhotoMarkerDownload(String url, final Marker marker, final int size){
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        try {
            final File file = File.createTempFile("icone",".png");
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public Bitmap PhotoMarkerDownload(String url, final Loja loja, final int size){
//        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
//        try {
//            final File file = File.createTempFile("icone",".png");
//            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    try {
//                        loja.bitmap = BitmapFactory.decodeFile(file.getPath());
//                        loja.bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
    public Bitmap PhotoDownload(String url){
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        try {
            final File file = File.createTempFile("icone",".png");
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap = BitmapFactory.decodeFile(file.getPath());

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}