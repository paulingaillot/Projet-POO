package com.poo.projetfinal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import com.poo.projetfinal.Exceptions.BadPasswordException;
import com.poo.projetfinal.Exceptions.BadUserException;

public class User {
    private String mail;
    private String nom;
    private String prenom;
    
    private String password;

    private int age;
    private char sexe;

    private int temps;
    private int budget;

    private String UID;

    public User(String mail, String password, String prenom, String nom, int age, char sexe, int temps, int budget) {
        System.out.println("test 1 ");
        Database sql = new Database();
        sql.userInscription(mail, password, prenom, nom, age, sexe, budget, temps);


    }

    public User(String mail, String password) throws BadPasswordException, BadUserException {
        try {
        Database sql = new Database();
        ResultSet result = sql.userConnect(mail);
        if(!result.next()) throw new BadUserException();


        this.mail = mail;
        String encoded_password = result.getString("password");
        String decoded_password = new String(Base64.getDecoder().decode(encoded_password));
      
        this.password  = decoded_password;

        if(password.equals(this.password)) {
        this.prenom = result.getString("prenom");
        this.nom = result.getString("nom");

        this.age = Integer.parseInt(result.getString("age"));
        this.sexe = result.getString("sexe").charAt(0);

        this.temps = Integer.parseInt(result.getString("temps"));
        this.budget = Integer.parseInt(result.getString("budget"));
        
        sql.close();
        }else {
            throw new BadPasswordException();
        }
        }catch(SQLException e) {
            e.printStackTrace();
        }catch(BadUserException e ){
            throw new BadUserException();
        }
    }

    public User(String token) throws BadUserException {
        try {
        ResultSet result = ProjetfinalApplication.sql.getUserByToken(token);
        if(!result.next()) throw new BadUserException();

        this.mail = result.getString("mail");
        String encoded_password = result.getString("password");
        String decoded_password = new String(Base64.getDecoder().decode(encoded_password));
      

        this.UID = token;
        this.password  = decoded_password;

        this.prenom = result.getString("prenom");
        this.nom = result.getString("nom");

        this.age = Integer.parseInt(result.getString("age"));
        this.sexe = result.getString("sexe").charAt(0);

        this.temps = Integer.parseInt(result.getString("temps"));
        this.budget = Integer.parseInt(result.getString("budget"));

        ProjetfinalApplication.sql.close();
        }catch(Exception e) {

        }
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

    public String getEncodedPassword() {
        return Base64.getEncoder().encodeToString(this.password.getBytes());
    }

    public void saveUID(String UID) {
        ProjetfinalApplication.sql.updateUID(this.mail, UID);
    }

    public void updateDatabase() {
        ProjetfinalApplication.sql.updateUser(this.UID, this.nom, this.prenom, this.mail, this.budget, this.temps);
    }

}
