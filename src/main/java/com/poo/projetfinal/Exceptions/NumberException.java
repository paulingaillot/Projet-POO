package com.poo.projetfinal.Exceptions;

public class NumberException extends NumberFormatException {
    public static String ACRONYM = "NBR";

    private String webMessage = "Veuillez indiquer un nombre valide";
    public NumberException() {
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
