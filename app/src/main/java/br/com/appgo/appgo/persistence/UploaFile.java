package br.com.appgo.appgo.persistence;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;

/**
 * Created by hex on 03/03/18.
 */

public class UploaFile {
    Context context;
    String urlPhoto;

    public UploaFile(Context context) {
        this.context = context;
    }
    public String  UploadPhoto(Bitmap bitmap, String fileName, StorageReference reference, String Id){
        final StorageReference referenceIcone = reference.child(Id).child("fotos").child(fileName);
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
            byte[] dataByte = stream.toByteArray();

            UploadTask uploadTask = referenceIcone.putBytes(dataByte);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return referenceIcone.toString();
    }
}
