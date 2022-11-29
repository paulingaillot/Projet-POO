package com.poo.projetfinal;

public class Recette {
    private String nom;
    private int duree;
    private int budget;

    public Recette(String nom, int duree, int budget) {
        this.nom = nom;
        this.duree = duree;
        this.budget = budget;
    }

    public String getNom() {
        return nom;
    }

    public int getduree() {
        return duree;
    }

    public int getBudget() {
        return budget;
    }
}
