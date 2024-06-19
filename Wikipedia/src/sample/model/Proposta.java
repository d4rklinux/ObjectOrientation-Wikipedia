package sample.model;

import sample.controller.StatoProposta;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The type Proposta.
 */
// Campi della classe Proposta
public class Proposta {
    private StatoProposta stato;
    private int id_proposta;
    private int id_frase;
    private String nuovo_contenuto;
    private String vecchio_contenuto;
    /**
     * The Utente.
     */
    public Utente utente;
    /**
     * The Username utente proposta.
     */
    public String username_utente_proposta;
    private LocalDate data_creazione;  // Modificato da Date a LocalDate
    private LocalTime ora_creazione;
    /**
     * The Frase.
     */
    public Frase frase;

    // Costruttore della classe Proposta

    /**
     * Instantiates a new Proposta.
     */
    public Proposta() {

    }

    /**
     * Instantiates a new Proposta.
     *
     * @param stato                    the stato
     * @param id_proposta              the id proposta
     * @param id_frase                 the id frase
     * @param nuovo_contenuto          the nuovo contenuto
     * @param vecchio_contenuto        the vecchio contenuto
     * @param utente                   the utente
     * @param username_utente_proposta the username utente proposta
     * @param data_creazione           the data creazione
     * @param ora_creazione            the ora creazione
     */
    public Proposta(StatoProposta stato, int id_proposta, int id_frase, String nuovo_contenuto, String vecchio_contenuto, Utente utente, String username_utente_proposta, LocalDate data_creazione, LocalTime ora_creazione) {
        this.stato = stato;
        this.id_proposta = id_proposta;
        this.id_frase = id_frase;
        this.nuovo_contenuto = nuovo_contenuto;
        this.vecchio_contenuto = vecchio_contenuto;
        this.utente = utente;
        this.username_utente_proposta = username_utente_proposta;
        this.data_creazione = data_creazione;
        this.ora_creazione = ora_creazione;
    }

    /**
     * Gets stato.
     *
     * @return the stato
     */
// Metodi getter e setter per gli attributi della classe Proposta
    public StatoProposta getStato() {
        return stato;
    }

    /**
     * Sets stato.
     *
     * @param stato the stato
     */
    public void setStato(StatoProposta stato) {
        this.stato = stato;
    }

    /**
     * Gets id proposta.
     *
     * @return the id proposta
     */
    public int getId_proposta() {
        return id_proposta;
    }

    /**
     * Sets id proposta.
     *
     * @param id_proposta the id proposta
     */
    public void setId_proposta(int id_proposta) {
        this.id_proposta = id_proposta;
    }

    /**
     * Gets nuovo contenuto.
     *
     * @return the nuovo contenuto
     */
    public String getNuovo_contenuto() {
        return nuovo_contenuto;
    }

    /**
     * Sets nuovo contenuto.
     *
     * @param nuovo_contenuto the nuovo contenuto
     */
    public void setNuovo_contenuto(String nuovo_contenuto) {
        this.nuovo_contenuto = nuovo_contenuto;
    }

    /**
     * Gets vecchio contenuto.
     *
     * @return the vecchio contenuto
     */
    public String getVecchio_contenuto() {
        return vecchio_contenuto;
    }

    /**
     * Sets vecchio contenuto.
     *
     * @param vecchio_contenuto the vecchio contenuto
     */
    public void setVecchio_contenuto(String vecchio_contenuto) {
        this.vecchio_contenuto = vecchio_contenuto;
    }

    /**
     * Gets data creazione.
     *
     * @return the data creazione
     */
    public LocalDate getData_creazione() {
        return data_creazione;
    }

    /**
     * Sets data creazione.
     *
     * @param data_creazione the data creazione
     */
    public void setData_creazione(LocalDate data_creazione) {
        this.data_creazione = data_creazione;
    }

    /**
     * Gets ora creazione.
     *
     * @return the ora creazione
     */
    public LocalTime getOra_creazione() {
        return ora_creazione;
    }

    /**
     * Sets ora creazione.
     *
     * @param ora_creazione the ora creazione
     */
    public void setOra_creazione(LocalTime ora_creazione) {
        this.ora_creazione = ora_creazione;
    }

    /**
     * Gets id frase.
     *
     * @return the id frase
     */
    public int getId_frase() {
        return id_frase;
    }

    /**
     * Sets id frase.
     *
     * @param id_frase the id frase
     */
    public void setId_frase(int id_frase) {
        this.id_frase = id_frase;
    }

    /**
     * Gets username utente proposta.
     *
     * @return the username utente proposta
     */
    public String getUsername_utente_proposta() {
        return username_utente_proposta;
    }

    /**
     * Sets username utente proposta.
     *
     * @param username_utente_proposta the username utente proposta
     */
    public void setUsername_utente_proposta(String username_utente_proposta) {
        this.username_utente_proposta = username_utente_proposta;
    }
}
