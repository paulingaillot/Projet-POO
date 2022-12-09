package com.poo.projetfinal.Exceptions;

public class BadUserException extends Throwable{
    public static String ACRONYM = "BUE";

    private String webMessage = "Le nom d'utilisateur est inconnu";
    public BadUserException() {
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
