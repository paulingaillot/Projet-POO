package com.poo.projetfinal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class Database {
    private String url = "jdbc:mysql://127.0.0.1:3306/test";
    private String username = "new_user";
    private String passwd = "test";
    private Connection ct;
    private PreparedStatement st;

    public Database() {
        try {
            ct = DriverManager.getConnection(url, username, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet userConnect(String mail) {
        try {
            st = ct.prepareStatement("SELECT * FROM users WHERE mail='" + mail + "';");
            ResultSet result = st.executeQuery();

            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    public void close() {
        try {
            st.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ResultSet getRecettes() {
        try {
            st = ct.prepareStatement("SELECT * FROM recette ORDER BY id DESC;;");
            ResultSet result = st.executeQuery();

            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet getUser(String mail) {
        try {
            st = ct.prepareStatement("SELECT * FROM `users` WHERE `mail`='"+mail+"';");
            ResultSet result = st.executeQuery();

            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    public void sauveIMG(String location, String id) throws Exception {
        File monImage = new File(location);
        FileInputStream istreamImage = new FileInputStream(monImage);
        try {
            PreparedStatement ps = ct.prepareStatement("UPDATE `recette` SET `image`=? WHERE `id`=?");
            try {
                ps.setBinaryStream(1, istreamImage, (int) monImage.length());
                ps.setLong(2, Integer.parseInt(id));
                ps.executeUpdate();
            } finally {
                ps.close();
            }
        } finally {
            istreamImage.close();
        }
    }

    public byte[] chargeIMG(String id) throws Exception {

        PreparedStatement ps = ct.prepareStatement("SELECT `image` FROM recette WHERE id=?");

        try {
            ps.setLong(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();

            try {
                if (rs.next()) {
                    InputStream istreamImage = rs.getBinaryStream("image");
                    byte[] result = istreamImage.readAllBytes();
                    return result;
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
        System.out.println("error");
        return null;

    }

    public void userInscription(String mail, String encoded_password, String nom, String prenom, int age, char sexe,
            int budget, int temps) {
        try {
            Statement sl = ct.createStatement();

            sl.execute(
                    "INSERT INTO `test`.`users` (`mail`,`password`, `nom`, `prenom`, `age`, `sexe`, `budget`, `temps`)VALUES('"
                            + mail + "', '" + encoded_password + "', '" + nom + "', '" + prenom + "', "
                            + age + ", '" + sexe + "', " + budget + ", "
                            + temps + ");");

            sl.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
