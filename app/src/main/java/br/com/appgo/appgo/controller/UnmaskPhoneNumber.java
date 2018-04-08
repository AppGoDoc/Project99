package br.com.appgo.appgo.controller;

/**
 * Created by hex on 15/03/18.
 */

public class UnmaskPhoneNumber {
    public String whatsNumber(String number){
        number = number.replace("(", "");
        number = number.replace(")", "");
        number = number.replace("-", "");
        return "55"+number;
    }
}
