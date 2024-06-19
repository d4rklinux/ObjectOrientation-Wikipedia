package sample.model;

import sample.controller.StatoProposta;

/**
 * The type Modifica.
 */
public class Modifica {
    private int id_proposta;
    private String username_utente;

    private StatoProposta stato;

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
     * Gets username utente.
     *
     * @return the username utente
     */
    public String getUsername_utente() {
        return username_utente;
    }

    /**
     * Sets username utente.
     *
     * @param username_utente the username utente
     */
    public void setUsername_utente(String username_utente) {
        this.username_utente = username_utente;
    }

    /**
     * Gets stato.
     *
     * @return the stato
     */
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
     * Instantiates a new Modifica.
     *
     * @param id_proposta     the id proposta
     * @param username_utente the username utente
     * @param stato           the stato
     */
    public Modifica(int id_proposta, String username_utente, StatoProposta stato) {
        this.id_proposta = id_proposta;
        this.username_utente = username_utente;
        this.stato = stato;
    }
}
