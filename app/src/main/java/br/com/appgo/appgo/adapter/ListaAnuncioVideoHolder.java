package br.com.appgo.appgo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import br.com.appgo.appgo.R;

public class ListaAnuncioVideoHolder extends RecyclerView.ViewHolder {
    public TextView txtData;
    public VideoView mVideoView;

    public ListaAnuncioVideoHolder(View itemView) {
        super(itemView);
        txtData = itemView.findViewById(R.id.txt_lista_item);
        mVideoView = itemView.findViewById(R.id.video_view);
    }
}
