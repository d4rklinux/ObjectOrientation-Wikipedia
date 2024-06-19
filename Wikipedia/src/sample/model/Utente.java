package sample.model;

import java.util.ArrayList;

/**
 * The type Utente.
 */
// Campi della classe Utente
public class Utente {
    private String ruolo;
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private String email;
    //ArrayList<Proposta> proposta;
    //ArrayList<Pagina> pagina;

    /**
     * Instantiates a new Utente.
     *
     * @param nome     the nome
     * @param cognome  the cognome
     * @param username the username
     * @param password the password
     * @param email    the email
     * @param ruolo    the ruolo
     */
// Costruttore della classe Utente
    public Utente( String nome, String cognome, String username, String password,String email,String ruolo) {
        this.ruolo = ruolo;
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    /**
     * Instantiates a new Utente.
     */
// Costruttore vuoto della classe Utente
    public Utente() {

    }

    /**
     * Gets ruolo.
     *
     * @return the ruolo
     */
// Metodi getter e setter per gli attributi della classe Utente
    public String getRuolo() {
        return ruolo;
    }

    /**
     * Sets ruolo.
     *
     * @param ruolo the ruolo
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Gets cognome.
     *
     * @return the cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Sets cognome.
     *
     * @param cognome the cognome
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
