package sample.model;

/**
 * The type Collegamento.
 */
public class Collegamento {

    private int id_pagina;
    private int id_frase;
   private String URL;

    /**
     * Instantiates a new Collegamento.
     *
     * @param id_pagina the id pagina
     * @param id_frase  the id frase
     * @param URL       the url
     */
    public Collegamento(int id_pagina, int id_frase, String URL) {
        this.id_pagina = id_pagina;
        this.id_frase = id_frase;
        this.URL = URL;
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
     * Gets url.
     *
     * @return the url
     */
    public String getURL() {
        return URL;
    }

    /**
     * Sets url.
     *
     * @param URL the url
     */
    public void setURL(String URL) {
        this.URL = URL;
    }
}
