package com.poo.projetfinal;

public class User {
    private String mail;
    private String nom;
    private String prenom;

    private int age;
    private char sexe;

    private int temps;
    private int budget;

    public User(String mail, String nom, String prenom, char sexe, int age, int budget, int temps) {
        this.mail = mail;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.sexe = sexe;
        this.temps = temps;
        this.budget = budget;

    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSexe() {
        return this.sexe;
    }

    public void setSexe(char sexe) {
        this.sexe = sexe;
    }

    public int getTemps() {
        return this.temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public int getBudget() {
        return this.budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

}
