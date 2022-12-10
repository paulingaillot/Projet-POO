package com.poo.projetfinal.Exceptions;

public class BadFormatException extends Throwable{
    public static String ACRONYM = "BFE";

    private String webMessage = "Le Fichier envoyé n'est pas au format demandé (png/jpg/jpeg)";
    public BadFormatException() {
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
