package br.com.appgo.appgo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.appgo.appgo.R;

class TopAnuncioHolder extends RecyclerView.ViewHolder {
    TextView mAnuncioTitulo, curtidas, comentarios, txtEndereco, mCompartilhamentos;
    Button btnRamo, btnDenunciar, btnAnuncio, btnCriarRota;
    ImageView btnWhatsapp, btnTelefone, btnEmail, mImageStore;
    ImageButton curtir, comentar, compartilhar;
    public TopAnuncioHolder(View itemView) {
        super(itemView);
        btnAnuncio = itemView.findViewById(R.id.button_ver_anuncio);
        btnDenunciar = itemView.findViewById(R.id.denuncia_button);
        compartilhar = itemView.findViewById(R.id.sharing_anuncio);
        comentar = itemView.findViewById(R.id.comentar_anuncio);
        comentarios = itemView.findViewById(R.id.cont_comment);
        curtidas = itemView.findViewById(R.id.contagem_curtir);
        curtir = itemView.findViewById(R.id.curtir_anuncio);
        mCompartilhamentos = itemView.findViewById(R.id.cont_share);
        mAnuncioTitulo = itemView.findViewById(R.id.titulo_anuncio);
        txtEndereco = itemView.findViewById(R.id.anuncio_endereco);
        btnTelefone = itemView.findViewById(R.id.image_telefone);
        btnWhatsapp = itemView.findViewById(R.id.image_whatsapp);
        btnEmail = itemView.findViewById(R.id.image_email);
        btnRamo = itemView.findViewById(R.id.anuncio_ramo);
        btnCriarRota = itemView.findViewById(R.id.create_route);
        mImageStore = itemView.findViewById(R.id.carroussel_fotos_loja);
    }
}
