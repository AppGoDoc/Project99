package br.com.appgo.appgo.controller;

/**
 * Created by hex on 21/02/18.
 */

public class CheckDigit {

    /*
    * as contas de CPF e CNPJ são baseadas no número de ordem do dígito na string
    * os arrays baixo armazenam a ordem para cada dígito
    */
    private static final int[] pesosCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesosCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    /*
     * rotina genérica para calcular qual o dígito verificador
     * @PARAM str   string que contem o CPF ou CNPJ
     * @PARAM peso  array dos pesos, podem ser pesoCPF ou pesoCNPJ
    */
    private static int calcularDigitoVerificador(String str, int[] peso) {
        int soma = 0;
        for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
            digito = Integer.parseInt(str.substring( indice, indice + 1) );
            soma += digito*peso[peso.length-str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    /*
    * @PARAM cpf no formato 99999999999 ou 999.999.999-99
    * @RETURN volta true se o CPF é válido
    */
    public static boolean eValidoCPF(String cpf) {
        if (cpf == null) {
            return false;
        }
        // remove "." e "-"
        cpf = cpf.replace(".","");
        cpf = cpf.replace("-","");
        if (cpf.length() != 11) {
            return false; // sai se não tem o tamanho esperado
        }

        // passo 1 - calcula somente para a string sem o digito verificador
        Integer digito1 = calcularDigitoVerificador(cpf.substring(0,9), pesosCPF);
        // passo 2 - calculo novamente com o dígito obtido no passo 1
        Integer digito2 = calcularDigitoVerificador(cpf.substring(0,9) + digito1,
                pesosCPF);
        // retorna indicando se o CPF fornecido é igual o CPF com os
        // digitos verificadores calculados
        return cpf.equals(cpf.substring(0,9) + digito1.toString() +
                digito2.toString());
    }

    /*
    * @PARAM cnpj no formato 99999999999999 ou 99.999.999/9999-99
    * @RETURN volta true se o CNPJ é válido
    */
    public static boolean eValidoCNPJ(String cnpj) {
        if (cnpj==null) {
            return false;
        }
        // remove "." e "-"
        cnpj = cnpj.replace(".","");
        cnpj = cnpj.replace("-","");
        cnpj = cnpj.replace("/","");
        if (cnpj.length() != 14) {
            return false;
        }
        // passo 1 - calcula somente para a string sem o digito verificador
        Integer digito1 = calcularDigitoVerificador(cnpj.substring(0,12), pesosCNPJ);
        // passo 2 - calculo novamente com o dígito obtido no passo 1
        Integer digito2 = calcularDigitoVerificador(cnpj.substring(0,12) + digito1,
                pesosCNPJ);
        return cnpj.equals(cnpj.substring(0,12) + digito1.toString() +
                digito2.toString());
    }

}
