package br.com.appgo.appgo.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.model.AnuncioFoto;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.PhotoPicasso;

import static br.com.appgo.appgo.view.CriarAnuncioActivity.ANUNCIOS;
import static br.com.appgo.appgo.view.PostarActivity.RESULT_VIDEO_ANUNCIO;
import static br.com.appgo.appgo.view.PostarActivity.USER_EDIT_POSITION;
import static br.com.appgo.appgo.view.PostarActivity.USER_LOJA_ACTION_TO_POST;
import static br.com.appgo.appgo.view.PostarActivity.USER_LOJA_EDIT_POST;
import static br.com.appgo.appgo.view.PostarActivity.USER_LOJA_TO_POST;

public class CriarAnuncioVideo extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {
    private static final int GET_VIDEO_FROM_GALLERY = 1010;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1011;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1012;
    private static final String TAG = "BOMBOMSHELLS";
    private String selectVideoPath;
    private VideoView mVideoView;
    private EditText mDescricaoVideo;
    private Button mBack, mGetVideo, mSharing, mPlayVideo;
    private TextView title_store;
    private ImageView mUserStoreImage;
    private MediaController mMediaController;
    private boolean videoPrepared, ffmpegRightDownload;
    private int editPosition = -1;
    private Loja loja;
    private FFmpeg ffmpeg;
    private Uri videoPath;
    private String filePath;
    private ProgressBar uploadBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_video_post);
        super.onCreate(savedInstanceState);

        videoPrepared = ffmpegRightDownload = false;
        uploadBar = findViewById(R.id.progress_bar);
        mDescricaoVideo = findViewById(R.id.edt_desc_video);
        title_store = findViewById(R.id.text_store_title);
        mSharing = findViewById(R.id.sharing_video);
        mSharing.setOnClickListener(this);
        mBack = findViewById(R.id.btn_cancel_video_sharing);
        mBack.setOnClickListener(this);
        mUserStoreImage = findViewById(R.id.image_user);
        mVideoView = findViewById(R.id.video_view);
        mVideoView.setOnPreparedListener(this);
        mPlayVideo = findViewById(R.id.btn_play_video);
        mPlayVideo.setOnClickListener(this);
        mGetVideo = findViewById(R.id.btn_get_video);
        mGetVideo.setOnClickListener(this);
        mMediaController = new MediaController(this);
        mPlayVideo.setVisibility(View.GONE);
        loja = RequestLoja();
//        loadFFMpegBinary();
        videoPath = null;
        try {
            PhotoPicasso picasso = new PhotoPicasso(this);
            picasso.Photo2fit(loja.urlFoto1, mUserStoreImage, mUserStoreImage.getWidth(), mUserStoreImage.getHeight(), true);
            title_store.setText(loja.titulo);
        } catch (Exception e){
            e.printStackTrace();
        }
        UploadBar(false);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mVideoView.getDuration() > 60000){
//            CheckWritePermission();
            Toast.makeText(this, "Video muito grande, escolha um video com até " +
                    "60 segundos de duração.", Toast.LENGTH_LONG).show();
        }
        else {
            mVideoView.start();
            videoPrepared = true;
            mPlayVideo.setVisibility(View.VISIBLE);
            mPlayVideo.setText("Iniciar/pausar video");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sharing_video:
                if (videoPath!=null){
                    SharingVideo();
                }
                else {
                    Toast.makeText(this, "Você precisa escolher um video para criar seu " +
                            "'post'", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancel_video_sharing:
                CancelSharing();
                break;
            case R.id.btn_get_video:
                GetVideoFromGalery();
                break;
            case R.id.btn_play_video:
                PlayVideo();
                break;
        }
    }

    private void PlayVideo() {
        try {
            if (videoPrepared) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                } else {
                    mVideoView.start();
                }
            } else {
                Toast.makeText(this, "Erro ao iniciar o video.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_VIDEO_FROM_GALLERY) {
            if (resultCode == RESULT_OK){
                selectVideoPath = getPath(data.getData());
                try{
                    if (selectVideoPath == null){
                        Toast.makeText(this, "Erro ao selecionar o video, tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        videoPath = Uri.parse(selectVideoPath);
                        mVideoView.setVideoURI(videoPath);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==REQUEST_READ_EXTERNAL_STORAGE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetVideoIntent();
            }
            else {
                Toast.makeText(this, "Você precisa dar permissõa para o aplicativo buscar videos em sua galeria",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                executeCutVideoCommand();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    private void GetVideoFromGalery(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        }
        else{
            GetVideoIntent();
        }
    }

    private void CancelSharing() {
        finish();
    }


    private void SharingVideo() {
        UploadBar(true);
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        final StorageReference videoReference = reference.child(loja.anunciante).child(ANUNCIOS).child(GetData()+".mp4");
        try {
            InputStream inputStream = new FileInputStream(new File(videoPath.toString()));
            UploadTask uploadTask = videoReference.putStream(inputStream);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    UploadBar(false);
                    if (task.isSuccessful()){
                        loja.AnuncioFotografico.add(
                                UpdateAnuncio(videoReference.toString())
                        );
                        UpdateDBStore();
                    }
                    else {

                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void UpdateDBStore(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(ANUNCIOS).child(loja.anunciante).setValue(loja);
        Intent sucessIntent = new Intent();
        setResult(RESULT_VIDEO_ANUNCIO, sucessIntent);
        finish();
    }
    private void GetVideoIntent(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GET_VIDEO_FROM_GALLERY);
    }
    private Loja RequestLoja() {
        Loja loja = null;
        Intent intent = getIntent();
        if (intent.getAction().equals(USER_LOJA_ACTION_TO_POST)){
            byte[] data = intent.getByteArrayExtra(USER_LOJA_TO_POST);
            loja = SerializationUtils.deserialize(data);
        }
        if (intent.getAction().equals(USER_LOJA_EDIT_POST)){
            byte[] data = intent.getByteArrayExtra(USER_LOJA_TO_POST);
            loja = SerializationUtils.deserialize(data);
            editPosition = intent.getIntExtra(USER_EDIT_POSITION, -1);
        }
        return loja;
    }

//    private void loadFFMpegBinary() {
//        try {
//            if (ffmpeg == null) {
//                ffmpeg = FFmpeg.getInstance(this);
//            }
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onFailure() {
//                    showUnsupportedExceptionDialog();
//                }
//
//                @Override
//                public void onSuccess() {
//                    ffmpegRightDownload = true;
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void showUnsupportedExceptionDialog() {
//        Toast.makeText(this, "Falha ao carregar a biblioteca FFMPEG.", Toast.LENGTH_SHORT).show();
//    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d(TAG, "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "SUCCESS with output : " + s);
                //Perform action on success
                }
                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "progress : " + s);
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg " + command);

                }
                });
            } catch (FFmpegCommandAlreadyRunningException e) {
                e.printStackTrace();
        }
    }
//    private void executeCutVideoCommand() {
//        File moviesDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES
//        );
//
//        String filePrefix = "cut_video";
//        String fileExtn = ".mp4";
//        String yourRealPath = getPath(videoPath);
//        File dest = new File(moviesDir, filePrefix + fileExtn);
//        int fileNo = 0;
//        while (dest.exists()) {
//            fileNo++;
//            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
//        }
//        Log.d(TAG, "startTrim: src: " + yourRealPath);
//        Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
//        Log.d(TAG, "startTrim: startMs: " + startMs);
//        Log.d(TAG, "startTrim: endMs: " + endMs);
//        filePath = dest.getAbsolutePath();
//        //String[] complexCommand = {"-i", yourRealPath, "-ss", "" + startMs / 1000, "-t", "" + endMs / 1000, dest.getAbsolutePath()};
//        String[] command = {"-ss", "0", "-y", "-i", videoPath.toString(), "-t", "60000", "-s", "480x360", "-r", "20",  }
//        String[] complexCommand = {
//                "-ss", "" + "0", "-y", "-i", yourRealPath, "-t", "" + "60",
//                "-s", "480x360", "-r", "20", "-vcodec", "mpeg4", "-b:v", "150000", "-b:a", "48000",
//                "-ac", "2", "-ar", "22050", filePath};
//        String[] complexCommand = {"-ss", "" + startMs / 1000, "-y", "-i", yourRealPath, "-t", ""
//                + (endMs - startMs) / 1000,"-vcodec", "mpeg4", "-b:v", "2097152", "-b:a",
//                "48000", "-ac", "2", "-ar", "22050", filePath};
//        String[] complexCommand = {"-y", "-i", yourRealPath, "-s", "160x120", "-r", "25", "-vcodec", "mpeg4", "-b:v", "150k", "-b:a", "48000", "-ac", "2", "-ar", "22050", filePath};
//        execFFmpegBinary(complexCommand);
//    }
//    private void CheckWritePermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_WRITE_EXTERNAL_STORAGE);
//        }
//        else {
//            executeCutVideoCommand();
//        }
//    }
    private void UploadBar(boolean token){
        if (token){
            uploadBar.setVisibility(View.VISIBLE);
        }
        else {
            uploadBar.setVisibility(View.GONE);
        }
    }
    private AnuncioFoto UpdateAnuncio(String urlVideoUploaded) {
        AnuncioFoto anuncioFoto = new AnuncioFoto();
        anuncioFoto.data_do_anuncio = GetData();
        anuncioFoto.urlVideo = urlVideoUploaded;
        anuncioFoto.descricaoVideo = mDescricaoVideo.getText().toString();
        return anuncioFoto;
    }
    @SuppressLint("DefaultLocale")
    private String GetData(){
        Calendar calendar = Calendar.getInstance();
        return String.format("%d%d%d_h%dm%d_",calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR),calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }
}