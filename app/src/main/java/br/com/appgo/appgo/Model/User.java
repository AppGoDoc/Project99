package br.com.appgo.appgo.Model;

import android.location.Location;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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