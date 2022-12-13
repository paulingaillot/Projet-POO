package com.poo.projetfinal.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.awt.image.BufferedImage;

import com.poo.projetfinal.Controllers.Index;

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

    // User

    public ResultSet userConnect(String mail) {
        try {
            st = ct.prepareStatement("SELECT * FROM users WHERE mail='" + mail + "';");
            ResultSet result = st.executeQuery();

            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet getUserByToken(String token) {
        try {
            st = ct.prepareStatement("SELECT * FROM users WHERE token='" + token + "';");
            ResultSet result = st.executeQuery();

            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    public void updateUID(String mail, String UID) {
        try {
            PreparedStatement ps = ct.prepareStatement("UPDATE `users` SET `token`=? WHERE `mail`=?");
            try {
                ps.setString(1, UID);
                ps.setString(2, mail);
                ps.executeUpdate();
            } finally {
                ps.close();
            }
        } catch (Exception e) {

        }
    }

    public void updateUser(String UID, String nom, String prenom, String mail, int budget, int temps) {
        try {
            System.out.println("TOKEN DEBUGGG : "+UID);
            PreparedStatement ps = ct.prepareStatement("UPDATE `users` SET `nom`=?, `prenom`=?, `mail`=?, `budget`=?, `temps`=? WHERE `token`=?");
            try {
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setString(3, mail);
                ps.setInt(4, budget);
                ps.setInt(5, temps);
                ps.setString(6, UID);
                ps.executeUpdate();
            } finally {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Recette

    public ResultSet getRecettes() {
        try {
            st = ct.prepareStatement("SELECT * FROM recette ORDER BY id DESC;;");

            return st.executeQuery();
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet getRecette(String id_recette) {
        try {
            st = ct.prepareStatement("SELECT * FROM recette WHERE id=" + id_recette + ";");

            return st.executeQuery();
        } catch (SQLException e) {
            return null;
        }
    }

    public int getRecetteId(String titre) {
        try {
            st = ct.prepareStatement("SELECT * FROM recette WHERE nom='" + titre + "';");
            ResultSet result = st.executeQuery();
            result.next();
            String id = result.getString("id");
            result.close();
            return Integer.parseInt(id);
        } catch (SQLException e) {
            return 0;
        }
    }

    public ResultSet getUser(String mail) {
        try {
            st = ct.prepareStatement("SELECT * FROM `users` WHERE `mail`='" + mail + "';");

            return st.executeQuery();
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

            }catch ( NullPointerException e){
                System.out.println("No image registered for this element");
            }
            finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
        System.out.println("error");
        return  null;

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

    public void createRecipe(String titre, int prix, int duree, String ingredients, String prepa, BufferedImage image) {
        try {
            Statement sl = ct.createStatement();

            sl.execute(
                    "INSERT INTO `test`.`recette` (`nom`,`temps`, `budget`, `ingredients`, `prepa`)VALUES('"
                            + titre + "', " + duree + ", " + prix + ", '" + ingredients + "', '"
                            + prepa + "');");

            sl.close();

            int recette_id = this.getRecetteId(titre);

            File file = new File("src/main/resources/static/tmp.png");

            try (OutputStream os = new FileOutputStream(file)) {
                os.write(Index.toByteArray(image, "png"));
            }

            this.sauveIMG("src/main/resources/static/tmp.png", recette_id + "");
            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
