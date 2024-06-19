package sample.model;

/**
 * The type Testo.
 */
public class Testo {

    private int id_testo;
    private int id_pagina;

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
     * Instantiates a new Testo.
     *
     * @param id_testo  the id testo
     * @param id_pagina the id pagina
     */
    public Testo(int id_testo, int id_pagina) {
        this.id_testo = id_testo;
        this.id_pagina = id_pagina;
    }
}
