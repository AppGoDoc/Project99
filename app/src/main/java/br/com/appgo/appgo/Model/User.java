package br.com.appgo.appgo.Model;


public class User {
    public String userPhotoUri;
    public String name;
    public String email;
    public boolean anuncio;

    public User(String name, String email, String cpf,
                    String celular) {
        this.email = email;
        this.name = name;
        anuncio = false;
    }

    public User() {}

}