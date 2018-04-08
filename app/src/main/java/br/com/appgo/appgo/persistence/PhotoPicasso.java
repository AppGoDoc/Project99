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
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .resize(90, 90)
                        .transform(transformation)
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
    public void Photo18x18(String url, final Marker marker, boolean token) {
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
//    public Bitmap PhotoDownload(String url, String name, Bitmap bitmap){
//        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
//        try {
//            final File file = File.createTempFile("foto",".png");
//            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                }
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}