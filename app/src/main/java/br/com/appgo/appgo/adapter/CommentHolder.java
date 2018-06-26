package br.com.appgo.appgo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.appgo.appgo.R;

public class CommentHolder extends RecyclerView.ViewHolder {
    public ImageView imageUser;
    public TextView comment;

    public CommentHolder(View itemView) {
        super(itemView);
        imageUser = itemView.findViewById(R.id.user_image);
        comment = itemView.findViewById(R.id.edt_coment);
    }
}
