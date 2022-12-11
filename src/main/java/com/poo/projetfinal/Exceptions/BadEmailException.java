package com.poo.projetfinal.Exceptions;

public class BadEmailException extends Throwable{
    public static String ACRONYM = "EML";

    private String webMessage = "Le mail n'est pas correct";
    public BadEmailException() {
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
