package com.poo.projetfinal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WebSitePattern {
    // Class permettant de generer le pattern du site web
    // -- Header
    // -- Head
    // -- Footer

    private String header;
    private String head;
    private String footer;

    public WebSitePattern() {

        // Head

        try {
            BufferedReader lecteurAvecBuffer = null;
            String ligne;
            this.head = "";

            lecteurAvecBuffer = new BufferedReader(new FileReader("src/main/resources/templates/blocks/head.html"));

            while ((ligne = lecteurAvecBuffer.readLine()) != null) {
                head += ligne;
            }

            lecteurAvecBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Header

        try {
            BufferedReader lecteurAvecBuffer = null;
            String ligne;
            this.header = "";

            lecteurAvecBuffer = new BufferedReader(new FileReader("src/main/resources/templates/blocks/header.html"));

            while ((ligne = lecteurAvecBuffer.readLine()) != null) {
                header += ligne;
            }

            lecteurAvecBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Footer

        try {
            BufferedReader lecteurAvecBuffer = null;
            String ligne;
            this.footer = "";

            lecteurAvecBuffer = new BufferedReader(new FileReader("src/main/resources/templates/blocks/footer.html"));

            while ((ligne = lecteurAvecBuffer.readLine()) != null) {
                footer += ligne;
            }

            lecteurAvecBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHead() {
        return this.head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

}
