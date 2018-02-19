package br.com.appgo.appgo.Model;

import android.location.Location;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class User {
    public String userPhotoUri;
    public String name;
    public String celular;
    public String cpf;
    public String email;

    public User(String name, String email, String cpf,
                    String celular) {
        this.celular = celular;
        this.cpf = cpf;
        this.email = email;
        this.name = name;
    }

    public User() {}

}