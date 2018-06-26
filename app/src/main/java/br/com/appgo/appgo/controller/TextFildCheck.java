package br.com.appgo.appgo.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hex on 07/02/18.
 */

public class TextFildCheck {
    public boolean CheckName(String Name){
        boolean token = false;
        if (!Name.isEmpty()){
            if (Name.length() > 2){
                token = true;
            }
        }
        return token;
    }
    public boolean validEmail(String email) {
        boolean token = false;
        Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher m = p.matcher(email);
        if (m.find()){
            token = true;
        }
        return token;
    }
    public boolean isTelefone(String numeroTelefone) {
        String telefone = unmask(numeroTelefone);
        return telefone.length()==10 || telefone.length()==11;
    }
    public String telefoneNumber(String telefone){
        char[] digit = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        char[] numberCelphone = telefone.toCharArray();
        String verifiedNumber = "";
        int cont = 0;
        for (int i = 0; i < numberCelphone.length;i++){
            for (int j = 0; j < digit.length; j++){
                if (numberCelphone[i] == digit[j]){
                    verifiedNumber += numberCelphone[i];
                    cont++;
                }
            }
        }
        return verifiedNumber;
    }
    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }
}

