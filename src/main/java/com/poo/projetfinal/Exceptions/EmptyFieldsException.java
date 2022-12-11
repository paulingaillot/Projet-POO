package com.poo.projetfinal.Exceptions;

public class EmptyFieldsException extends Throwable{
    public static String ACRONYM = "NLL";

    private String webMessage = "Certains champs sont vide";
    public EmptyFieldsException() {
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
