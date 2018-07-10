package br.com.appgo.appgo.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.model.Foto;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.PhotoPicasso;

public class AdapterFotoAnuncio extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FOTO_ANUNCIO = 0;
    private static final int VIDEO_ANUNCIO = 1;
    Context context;
    Loja loja;
    PhotoPicasso photoPicasso;
    boolean videoPrepared;
    List<Foto> fotos;
    public AdapterFotoAnuncio(Context context, Loja loja) {
        this.context = context;
        photoPicasso = new PhotoPicasso(context);
        this.loja = loja;
        fotos = new ArrayList<>();
        videoPrepared = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case FOTO_ANUNCIO:
                return new FotoAnuncioHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ver_anuncio_carroussel, parent, false));
            case VIDEO_ANUNCIO:
                return new VideoAnuncioHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.ver_anuncio_carroussel_video,parent,false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case FOTO_ANUNCIO:
                final FotoAnuncioHolder fotoHolder = (FotoAnuncioHolder) holder;
                fotos = loja.AnuncioFotografico.get(position).fotos;
                AnuncioFotoAdapter fotosAdapter = new AnuncioFotoAdapter(fotos, context, loja.urlFoto1, loja.titulo);
                fotoHolder.viewPager.setAdapter(fotosAdapter);
                fotoHolder.indicatorView.setCount(fotos.size());
                fotoHolder.indicatorView.setAnimationType(AnimationType.DROP);
                fotoHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        fotoHolder.indicatorView.setSelection(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                fotoHolder.mLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "like", Toast.LENGTH_SHORT).show();
                    }
                });
                fotoHolder.mComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "comment", Toast.LENGTH_SHORT).show();
                    }
                });
                fotoHolder.mShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "share", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case VIDEO_ANUNCIO:
                final VideoAnuncioHolder videoHolder = (VideoAnuncioHolder) holder;
                photoPicasso.Photo2(loja.urlFoto1, videoHolder.mUserImage, 100, 100, true);
                videoHolder.mUserName.setText(loja.titulo);
                videoHolder.mTextAdviser.setText(loja.AnuncioFotografico.get(position).descricaoVideo);
                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(loja.AnuncioFotografico.get(position).urlVideo);
                reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri videoUri = task.getResult();
                        videoHolder.videoView.setVideoURI(videoUri);
                        videoHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                videoHolder.videoView.start();
                                videoPrepared = true;
                            }
                        });
                    }
                });
                videoHolder.mLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show();
                    }
                });
                videoHolder.mComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();
                    }
                });
                videoHolder.mShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
                    }
                });
                videoHolder.mPlayVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (videoPrepared) {
                                if (videoHolder.videoView.isPlaying()) {
                                    videoHolder.videoView.pause();
                                } else {
                                    videoHolder.videoView.start();
                                }
                            } else {
                                Toast.makeText(context, "Erro ao iniciar o video.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position){
        if (loja.AnuncioFotografico.get(position).fotos.size() > 0){
            return FOTO_ANUNCIO;
        }
        else {
            return VIDEO_ANUNCIO;
        }
    }


    @Override
    public int getItemCount() {
        return loja.AnuncioFotografico != null ? loja.AnuncioFotografico.size() : 0;
    }
}
