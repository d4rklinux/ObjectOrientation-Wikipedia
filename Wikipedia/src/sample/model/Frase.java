package sample.model;

import java.util.ArrayList;

/**
 * The type Frase.
 */
// Campi della classe Frase
public class Frase {
    private String contenuto;
    private int versione;
    private int id_testo;
    private int id_pagina;
    private String collegamento;
    /**
     * The Pagina.
     */
    public Pagina pagina;
    /**
     * The Destinazione.
     */
    public Pagina destinazione;
    /**
     * The Proposte.
     */
    ArrayList<Proposta> proposte;

    /**
     * Instantiates a new Frase.
     *
     * @param contenuto    the contenuto
     * @param versione     the versione
     * @param id_testo     the id testo
     * @param collegamento the collegamento
     * @param pagina       the pagina
     * @param destinazione the destinazione
     * @param proposte     the proposte
     */
// Costruttore della classe Frase
    public Frase(String contenuto, int versione, int id_testo, String collegamento, Pagina pagina, Pagina destinazione, Proposta proposte) {
        this.contenuto = contenuto;
        this.versione = versione;
        this.id_testo = id_testo;
        this.collegamento = collegamento;
        this.pagina = pagina;
        this.destinazione=destinazione;
        this.proposte.add(proposte);
    }

    /**
     * Gets contenuto.
     *
     * @return the contenuto
     */
// Costruttore della classe Frase
    public String getContenuto() {
        return contenuto;
    }

    /**
     * Sets contenuto.
     *
     * @param contenuto the contenuto
     */
    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    /**
     * Gets versione.
     *
     * @return the versione
     */
    public int getVersione() {
        return versione;
    }

    /**
     * Sets versione.
     *
     * @param versione the versione
     */
    public void setVersione(int versione) {
        this.versione = versione;
    }

    /**
     * Gets id testo.
     *
     * @return the id testo
     */
    public int getId_testo() {
        return id_testo;
    }

    /**
     * Sets id testo.
     *
     * @param id_testo the id testo
     */
    public void setId_testo(int id_testo) {
        this.id_testo = id_testo;
    }

    /**
     * Gets collegamento.
     *
     * @return the collegamento
     */
    public String getCollegamento() {
        return collegamento;
    }

    /**
     * Sets collegamento.
     *
     * @param collegamento the collegamento
     */
    public void setCollegamento(String collegamento) {
        this.collegamento = collegamento;
    }

    /**
     * Gets id pagina.
     *
     * @return the id pagina
     */
    public int getId_pagina() {
        return id_pagina;
    }

    /**
     * Sets id pagina.
     *
     * @param id_pagina the id pagina
     */
    public void setId_pagina(int id_pagina) {
        this.id_pagina = id_pagina;
    }
}
