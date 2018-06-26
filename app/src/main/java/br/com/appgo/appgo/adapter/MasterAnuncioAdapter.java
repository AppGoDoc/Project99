package br.com.appgo.appgo.adapter;

//public class MasterAnuncioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    Loja loja;
//    Context context;
//    static final int headerView = 0, posts = 1;
//    List<Foto> fotos;
//    FirebaseUser user;
//    FireBase fireBase;
//    boolean curtidaToken;
//    public MasterAnuncioAdapter(Loja loja, Context context, FirebaseUser user){
//        this.loja = loja;
//        fotos = new ArrayList<>();
//        this.context = context;
//        this.user = user;
//        fireBase = new FireBase(context);
//        curtidaToken = false;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        RecyclerView.ViewHolder viewHolder;
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//
//        switch (viewType){
//            case headerView:
//                View topView = inflater.inflate(R.layout.activity_anuncio, parent, false);
//                viewHolder = new TopAnuncioHolder(topView);
//                break;
//            default:
//                View mainView = inflater.inflate(R.layout.ver_anuncio_carroussel, parent, false);
//                viewHolder = new FotoAnuncioHolder(mainView);
//                break;
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
//        switch (position){
//            case 0:
//                final TopAnuncioHolder topAnuncioHolder = (TopAnuncioHolder) holder;
//                topAnuncioHolder.btnAnuncio.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(context, "Esse Botão nao é mais utilizado", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                topAnuncioHolder.btnAnuncio.setText(VER_ANUNCIO);
//                topAnuncioHolder.btnDenunciar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SendDenuncia();
//                    }
//                });
//                topAnuncioHolder.compartilhar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shareImage(loja);
//                    }
//                });
//                topAnuncioHolder.comentar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SendComent();
//                    }
//                });
//                topAnuncioHolder.comentarios.setText(getComments());
//                topAnuncioHolder.curtidas.setText(String.valueOf(loja.curtidas.size()));
//                topAnuncioHolder.curtidas.setEnabled(false);
//                if (user!=null){
//                    if (user.isAnonymous()){
//                        topAnuncioHolder.curtidas.setEnabled(true);
//                    }
//                }
//                for (int i = 0; i < loja.curtidas.size(); ++i) {
//                    if (loja.curtidas.get(i).equals(user.getUid())) {
//                        topAnuncioHolder.curtidas.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//                        curtidaToken = true;
//                    }
//                }
//                topAnuncioHolder.curtir.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (user!=null) {
//                            if (!user.isAnonymous()) {
//                                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Anuncios/" + loja.anunciante);
//                                if (curtidaToken){
//                                    for (int i = 0; i<loja.curtidas.size(); i++){
//                                        if (loja.curtidas.get(i).equals(user.getUid())){
//                                            loja.curtidas.remove(i);
//                                        }
//                                        topAnuncioHolder.curtidas.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
//                                        curtidaToken = false;
//                                        database.child("curtidas").setValue(loja.curtidas);
//                                    }
//                                }
//                                else{
//                                    loja.curtidas.add(user.getUid());
//                                    database.child("curtidas").setValue(loja.curtidas);
//                                }
//                                topAnuncioHolder.curtidas.setText(String.valueOf(getCurtidas()));
//                            }
//                            }
//                            else{
//                                Toast.makeText(context, "Faça Login para ter acesso a todas " +
//                                        "as funcionalidades do AppGo!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                topAnuncioHolder.mCompartilhamentos.setText(loja.sharing);
//                topAnuncioHolder.mAnuncioTitulo.setText(loja.titulo);
//                topAnuncioHolder.txtEndereco.setText(String.format("%s", loja.local.endereco));
//                topAnuncioHolder.btnTelefone.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TelephoneContact();
//                    }
//                });
//                topAnuncioHolder.btnWhatsapp.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        WhatsAppContact();
//                    }
//                });
//                topAnuncioHolder.btnEmail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SendEmail();
//                    }
//                });
//                topAnuncioHolder.btnRamo.setText(loja.ramo);
//                topAnuncioHolder.btnCriarRota.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CreateRota();
//                    }
//                });
//                fileShare = null;
//                bitmapUri = null;
//                mImageStore = findViewById(R.id.carroussel_fotos_loja);
//                PhotoPicasso picasso = new PhotoPicasso(this);
//                picasso.Photo2fit(loja.urlFoto1, mImageStore, 1200, 800, true);
//                break;
//                default:
//                    final FotoAnuncioHolder fotoAnuncioHolder = (FotoAnuncioHolder) holder;
//                    fotos = loja.AnuncioFotografico.get(position-1).fotos;
//                    AnuncioFotoAdapter fotosAdapter = new AnuncioFotoAdapter(fotos, context, loja.urlFoto1, loja.titulo);
//                    fotoAnuncioHolder.viewPager.setAdapter(fotosAdapter);
//                    fotoAnuncioHolder.indicatorView.setCount(fotos.size());
//                    fotoAnuncioHolder.indicatorView.setAnimationType(AnimationType.DROP);
//                    fotoAnuncioHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                        @Override
//                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                        }
//
//                        @Override
//                        public void onPageSelected(int position) {
//                            fotoAnuncioHolder.indicatorView.setSelection(position);
//                        }
//
//                        @Override
//                        public void onPageScrollStateChanged(int state) {
//
//                        }
//                    });
//                    break;
//        }
//    }
//
//    private void CreateRota() {
//        Intent intentRota = new Intent();
//        intentRota.putExtra(LATITUDE_LOJA, loja.local);
//        intentRota.putExtra(LONGITUDE_LOJA, loja.local.longitude);
//        context.getApplicationContext().setResult(RESULT_LATLNG_LOJA, intentRota);
//        finish();
//    }
//
//    private void SendEmail() {
//        Intent it = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
//        it.setType("text/plain");
//        it.putExtra(Intent.EXTRA_SUBJECT, "Cliente AppGo! " + user.getDisplayName());
//        //it.putExtra(Intent.EXTRA_TEXT, "Body of email");
//        it.setData(Uri.parse("mailto:" + loja.emailAnuncio)); // or just "mailto:" for blank
//        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//        context.startActivity(it);
//    }
//
//    private void WhatsAppContact() {
//        UnmaskPhoneNumber unmask = new UnmaskPhoneNumber();
//        String number = unmask.whatsNumber(loja.whatsapp);
//        PackageManager packageManager = context.getPackageManager();
//        Intent i = new Intent(Intent.ACTION_VIEW);
//
//        try {
//            String url = "https://api.whatsapp.com/send?phone="+ number +"&text=" +
//                    URLEncoder.encode("Olá,\nsou cliente AppGo!", "UTF-8");
//            i.setPackage("com.whatsapp");
//            i.setData(Uri.parse(url));
//            if (i.resolveActivity(packageManager) != null) {
//                context.startActivity(i);
//            }
//            else {
//                Toast.makeText(context, "Você precisa instalar o WhatsApp no seu celular.", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private int getCurtidas() {
//        return loja.curtidas.size();
//    }
//
//    private String getComments(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ANUNCIOS);
//        List<Comentario> comentario = fireBase.GetListComentario(
//                reference.child(loja.anunciante).child("Comentario"));
//        return String.format("%s",comentario.size());
//    }
//
//    private void SendComent() {
//        if (user == null || user.isAnonymous()) {
//            Toast.makeText(context, "É preciso estar logado\n" +
//                    "para fazer um comentário.", Toast.LENGTH_SHORT).show();
//        } else {
//            Intent comentIntent = new Intent(context, ComentActivity.class);
//            comentIntent.putExtra(LOJA_COMENT, loja);
//            context.getApplicationContext().startActivity(comentIntent);
//        }
//    }
//
//    private void shareImage(Loja loja) {
//        try {
//            ShareImage shareImage = new ShareImage(context);
//            shareImage.shareItemFromReference(
//                    loja.urlFoto1,
//                    loja.titulo + " está no AppGo!\nBaixe o App e confira",
//                    loja.titulo + " está no AppGo!\nBaixe o App e confira:\nhttps://play.google.com/store/apps/details?id=br.com.appgo.appgo"
//            );
//            loja.sharing += 1;
//            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Anuncios/" + loja.anunciante);
//            database.child("sharing").setValue(loja.sharing);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void SendDenuncia() {
//        Intent denunciaMail = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
//        denunciaMail.setType("text/plain");
//        denunciaMail.putExtra(Intent.EXTRA_SUBJECT, "Denuncia de abuso de " + user.getDisplayName());
//        //it.putExtra(Intent.EXTRA_TEXT, "Body of email");
//        denunciaMail.setData(Uri.parse("mailto:" + EMAIL_APP_DENUNCIA)); // or just "mailto:" for blank
//        denunciaMail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//        context.startActivity(denunciaMail);
//    }
//    private void TelephoneContact() {
//        final int REQUEST_PHONE_CALL = 1;
//        UnmaskPhoneNumber unmaskPhoneNumber = new UnmaskPhoneNumber();
//        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + unmaskPhoneNumber.whatsNumber(loja.telefone)));
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
//            }
//            else{
//                context.startActivity(intent);
//            }
//        }
//        else {
//            context.startActivity(intent);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}
