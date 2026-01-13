package com.example.xmlklausurasmus.db;

import java.util.Calendar;

/**
 * @author Ass
 *
 * erstellt am 04.11.2025
 *
 * Die Klasse User bildet die Tabelle User in unserem Androidprojekt ab.
 */
public class User {

    private String username, pw;
    private Calendar erstelltAm;
    private int aktiv;

    /**
     *  Erstellt ein neues Objekt User mit folgenden Eingenschaften:
     *
     * @param username      Name des Benutzers
     * @param aktiv         Aktiver Account JA/Nein (1/0)
     * @param pw            Passworthash des Benutzers
     * @param erstelltAm    Erstellungsdatum des Accounts
     */
    public User(String username, String pw, Calendar erstelltAm, int aktiv) {
        this.username = username;
        this.aktiv = aktiv;
        this.pw = pw;
        this.erstelltAm = erstelltAm;
    }

    /**
     *  Gibt den Benutzernamen zurück
     * @return username name des Benutzers
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setzt den aktuellen Benutzernamen
     * @param username Name des Benutzers
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public Calendar getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(Calendar erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public int getAktiv() {
        return aktiv;
    }

    public void setAktiv(int aktiv) {
        this.aktiv = aktiv;
    }
}
