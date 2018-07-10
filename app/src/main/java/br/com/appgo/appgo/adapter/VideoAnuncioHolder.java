package br.com.appgo.appgo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import br.com.appgo.appgo.R;

public class VideoAnuncioHolder extends RecyclerView.ViewHolder {
    VideoView videoView;
    ImageView mUserImage, mPlayVideo;
    TextView mUserName, mTextAdviser;
    Button mLike, mShare, mComment;

    public VideoAnuncioHolder(View itemView) {
        super(itemView);
        mPlayVideo = itemView.findViewById(R.id.btn_play);
        videoView = itemView.findViewById(R.id.video_advise);
        mUserImage = itemView.findViewById(R.id.image_anuncer);
        mUserName = itemView.findViewById(R.id.text_adviser);
        mTextAdviser = itemView.findViewById(R.id.text_advise);
        mLike = itemView.findViewById(R.id.btn_like);
        mShare = itemView.findViewById(R.id.btn_share);
        mComment = itemView.findViewById(R.id.btn_comment);
    }
}
