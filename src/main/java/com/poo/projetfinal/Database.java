package com.poo.projetfinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private String url = "jdbc:mysql://127.0.0.1:3306/test";
    private String username = "new_user";
    private String passwd = "test";
    private Connection ct;
    private PreparedStatement st;

    public Database() {
        try {
        ct = DriverManager.getConnection(url, username, passwd);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet userConnect(String mail) {
        try {
         st = ct.prepareStatement("SELECT * FROM users WHERE mail='" + mail + "';");
        ResultSet result = st.executeQuery();

        return result;
        }catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close()  {
        try {
            st.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void userInscription(String mail, String encoded_password, String nom, String prenom, int age, char sexe, int budget, int temps) {
        try {
        Statement sl = ct.createStatement();

        sl.execute("INSERT INTO `test`.`users` (`mail`,`password`, `nom`, `prenom`, `age`, `sexe`, `budget`, `temps`)VALUES('"
                        + mail + "', '" + encoded_password + "', '" + nom + "', '" + prenom + "', "
                        + age + ", '" + sexe + "', " + budget + ", "
                        + temps + ");");

        sl.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }


}
