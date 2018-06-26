package br.com.appgo.appgo.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.model.AnuncioFoto;
import br.com.appgo.appgo.model.Foto;
import br.com.appgo.appgo.model.Loja;
import br.com.appgo.appgo.persistence.PhotoPicasso;

public class AdapterAnuncios extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int FOTO_ANUNCIO = 0;
    private final static int VIDEO_ANUNCIO = 1;
    private Context context;
    private Loja loja;
    public AdapterAnuncios(Loja loja, Context context) {
        this.context = context;
        this.loja = loja;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case FOTO_ANUNCIO:
                return new ListaAnuncioFotoHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.lista_anuncios_foto,parent,false));
            case VIDEO_ANUNCIO:
                return new ListaAnuncioVideoHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.lista_anuncios_video,parent,false));
                default:
                    return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case FOTO_ANUNCIO:
                ListaAnuncioFotoHolder fotoHolder = (ListaAnuncioFotoHolder)holder;
                fotoHolder.txtData.setText(loja.AnuncioFotografico.get(position).data_do_anuncio);
                AnuncioFoto anuncioFoto = loja.AnuncioFotografico.get(position);
                PhotoPicasso picasso = new PhotoPicasso(context);
                int i = 0;
                for (Foto foto: anuncioFoto.fotos){
                    picasso.PhotoOrigSize(anuncioFoto.fotos.get(i).urlFoto, fotoHolder.fotos.get(i), true);
                    i++;
                }
                break;
            case VIDEO_ANUNCIO:
                final ListaAnuncioVideoHolder videoHolder = (ListaAnuncioVideoHolder)holder;
                videoHolder.txtData.setText(loja.AnuncioFotografico.get(position).data_do_anuncio);
                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(loja.AnuncioFotografico.get(position).urlVideo);
                reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri videoUri = task.getResult();
                        videoHolder.mVideoView.setVideoURI(videoUri);
                        videoHolder.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                videoHolder.mVideoView.start();
                            }
                        });
                    }
                });
                break;
        }
    }
    @Override
    public int getItemViewType(int position){
        if (loja.AnuncioFotografico.get(position).urlVideo == null){
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