package com.poo.projetfinal.Exceptions;

public class BadPasswordException extends Throwable {
    public static String ACRONYM = "BPE";

    private String webMessage = "Le mot de passe est incorrect";
    public BadPasswordException() {
        super();
      }

    @Override
    public String getMessage() {
        return webMessage;
    }

    public String getAcronym() {
        return ACRONYM;
    }
}

