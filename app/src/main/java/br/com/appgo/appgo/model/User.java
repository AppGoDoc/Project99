package br.com.appgo.appgo.model;


public class User {
    public String userPhotoUri;
    public String name;
    public String email;
    public boolean anuncio;

    public User(String userPhotoUri, String name, String email) {
        this.userPhotoUri = userPhotoUri;
        this.name = name;
        this.email = email;
    }

    public User() {}

}