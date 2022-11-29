package com.poo.projetfinal;

public class Recette {
    private int id;
    private String nom;
    private int duree;
    private int budget;

    public Recette(String nom, int duree, int budget, int id) {
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.budget = budget;
    }

    public int getId() {
        return id;
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
