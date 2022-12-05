package com.poo.projetfinal;

import org.springframework.web.multipart.MultipartFile;

public class Recette {
    private int id;
    private String nom;
    private int duree;
    private int budget;
    private String[] prepa;
    private String[] ingredients;
    private MultipartFile image;

    public Recette(String nom, int duree, int budget, int id) {
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.budget = budget;
    }

    public Recette(String nom, int duree, int budget, String ingredients, String prepa, MultipartFile image) {
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
            list_ingredient += ingredient+"\n";
        }
        list_ingredient = list_ingredient.subSequence(0, list_ingredient.length()-1).toString();

        String list_prepa = "";
        for (String prep : prepa) {
            list_prepa += prep+"\n";
        }
        list_prepa = list_prepa.subSequence(0, list_prepa.length()-1).toString();


        sql.createRecipe(nom, duree, budget, list_ingredient, list_prepa, image);
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
