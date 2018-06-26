package br.com.appgo.appgo.persistence;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static br.com.appgo.appgo.view.CriarAnuncioActivity.ANUNCIOS;
import static br.com.appgo.appgo.view.RegisterActivity.USERS_REFERENCE;

/**
 * Created by hex on 03/03/18.
 */

public class UploaFile {

    private static final String FAIL_GET_URL = "fail_to_get_url";
    Context context;
    String urlPhoto;
    Uri uriPhoto;
    boolean token;

    public UploaFile(Context context) {
        this.context = context;
        token = false;
        urlPhoto = null;
        uriPhoto = null;
    }

    public String  UploadPhoto(Bitmap bitmap, String fileName, StorageReference reference, String Id){
        final StorageReference referenceIcone = reference.child(Id).child("fotos").child(fileName);
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
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

    public String UploadPhotoUrl(Bitmap bitmap, String fileName, StorageReference reference, String Id, String dataAnuncio){
        token = false;
        final StorageReference storageReference = reference.child(Id).child(ANUNCIOS).child(dataAnuncio + fileName);
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, stream);
            byte[] dataByte = stream.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(dataByte);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    token = true;
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return storageReference.toString();
    }

    public boolean UploadUserData(Bitmap bitmap, String userId, StorageReference reference,
                                  final String userName, final FirebaseUser user){
        token = false;
        final StorageReference storageReference = reference.child(USERS_REFERENCE).child(userId);
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, stream);
            byte[] dataByte = stream.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(dataByte);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(userName)
                                            .setPhotoUri(uri)
                                            .build();
                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                token = true;
                                            }
                                            else {
                                                token = false;
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            token = false;
                        }
                    });
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            token = false;
        }
        return token;
    }

    public boolean UploadUserData(String userName, FirebaseUser user) {
        token = false;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    token = true;
                else
                    token = false;
            }
        });
        return token;
    }

    public Uri getUriPhoto(StorageReference storageReference){
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    uriPhoto = task.getResult();
                }
                else {
                    uriPhoto = null;
                }
            }
        });
        return uriPhoto;
    }


    public String GetUrl(String reference){
        urlPhoto = null;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(reference);
        storageReference.child(reference).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                urlPhoto = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                urlPhoto = FAIL_GET_URL;
            }
        });
        return urlPhoto;
    }
}