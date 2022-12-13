package com.poo.projetfinal.Model;

public class RecipeAffinity {
    private Recette recipe;
    private int score;

    public RecipeAffinity(Recette recipe, int score) {
        this.recipe = recipe;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public Recette getRecipe() {
        return recipe;
    }
}
