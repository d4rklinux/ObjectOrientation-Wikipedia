package sample.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The type Pagina.
 */
// Campi della classe Pagina
public class Pagina {
    private String titolo;
    private LocalDate data_creazione;  // Modificato da Date a LocalDate
    private LocalTime ora_creazione;    // Modificato da Timestamp a LocalTime
    private int id_pagina;
    private String username_autore;
    private ArrayList<Frase> frasi;
    private ArrayList<Frase> destinazione;

    private String testo;

    /**
     * Gets testo.
     *
     * @return the testo
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Sets testo.
     *
     * @param testo the testo
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Instantiates a new Pagina.
     *
     * @param titolo         the titolo
     * @param data_creazione the data creazione
     * @param ora_creazione  the ora creazione
     * @param id_pagina      the id pagina
     * @param frasi          the frasi
     * @param destinazione   the destinazione
     */
// Costruttore della classe Pagina
    public Pagina(String titolo, LocalDate data_creazione, LocalTime ora_creazione, int id_pagina, Frase frasi, Frase destinazione) {
        this.titolo = titolo;
        this.data_creazione = data_creazione;
        this.ora_creazione = ora_creazione;
        this.id_pagina = id_pagina;
        this.frasi = new ArrayList<>();
        this.frasi.add(frasi);
        this.destinazione = new ArrayList<>();
        this.destinazione.add(destinazione);
    }

    /**
     * Instantiates a new Pagina.
     */
// Costruttore vuoto della classe Pagina
    public Pagina() {

    }

    /**
     * Gets titolo.
     *
     * @return the titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Sets titolo.
     *
     * @param titolo the titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
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

    /**
     * Gets username autore.
     *
     * @return the username autore
     */
    public String getUsername_autore() {
        return username_autore;
    }

    /**
     * Sets username autore.
     *
     * @param username_autore the username autore
     */
    public void setUsername_autore(String username_autore) {
        this.username_autore = username_autore;
    }
}
