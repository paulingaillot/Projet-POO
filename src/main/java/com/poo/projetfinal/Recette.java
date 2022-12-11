package com.poo.projetfinal;

import java.awt.image.BufferedImage;


public class Recette {
    private int id;
    private String nom;
    private int duree;
    private int budget;
    private String[] prepa;
    private String[] ingredients;
    private BufferedImage image;

    private String imageRendered;

    public Recette(String nom, int duree, int budget, int id) {
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.budget = budget;
    }

    public Recette(String nom, int duree, int budget, String ingredients, String prepa, BufferedImage image) {
        this.nom = nom;
        this.duree = duree;
        this.budget = budget;
        this.prepa = prepa.split("\n");
        this.ingredients = ingredients.split("\n");
        this.image = image;

    }

    public void CreateEntry() {
        Database sql = new Database();

        String list_ingredient = "";
        for (String ingredient : ingredients) {
            list_ingredient += ingredient + "\n";
        }
        list_ingredient = list_ingredient.subSequence(0, list_ingredient.length() - 1).toString();

        String list_prepa = "";
        for (String prep : prepa) {
            list_prepa += prep + "\n";
        }
        list_prepa = list_prepa.subSequence(0, list_prepa.length() - 1).toString();

        sql.createRecipe(nom, duree, budget, list_ingredient, list_prepa, image);
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getDuree() {
        return duree;
    }

    public int getBudget() {
        return budget;
    }

    public String getImageRendered() {
        return imageRendered;
    }

    public void setImageRendered(String imageRendered) {
        this.imageRendered = imageRendered;
    }
}
