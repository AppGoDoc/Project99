package br.com.appgo.appgo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import br.com.appgo.appgo.model.Comentario;
import br.com.appgo.appgo.persistence.PhotoPicasso;
import br.com.appgo.appgo.R;

public class AdapterComments extends RecyclerView.Adapter<CommentHolder> {
    private List<Comentario> comentarios;
    private Context context;

    public AdapterComments(List<Comentario> comentarios, Context context) {
        this.comentarios = comentarios;
        this.context = context;
    }
    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.coment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        String stringComent = "<strong>" + comentarios.get(position).name + "</strong><br><br>" +
                        "<em>" + comentarios.get(position).coment + "</em>";
        holder.comment.setText(Html.fromHtml(stringComent));
        PhotoPicasso picasso = new PhotoPicasso(context);
        picasso.PhotoUser(comentarios.get(position).urlPhoto, holder.imageUser);
    }

    @Override
    public int getItemCount() {
        return comentarios != null ? comentarios.size() : 0;
    }

}
