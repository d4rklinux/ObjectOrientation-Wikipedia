package sample.database;

/**
 * The type Const.
 */
// Classe costante che definisce i nomi delle tabelle e delle colonne nel database
public class Const {
    /**
     * The constant USERS_TABLE.
     */
// Nomi delle tabelle nel database
    public static final String USERS_TABLE = "utente";
    /**
     * The constant FRA_TABLE.
     */
    public static final String FRA_TABLE = "frase";
    /**
     * The constant MOD_TABLE.
     */
    public static final String MOD_TABLE = "modifica";
    /**
     * The constant PAG_TABLE.
     */
    public static final String PAG_TABLE = "pagina";
    /**
     * The constant PROP_TABLE.
     */
    public static final String PROP_TABLE = "proposta";

    /**
     * The constant USERS_NOME.
     */
// Nomi delle colonne nella tabella utente
   public static final String USERS_NOME = "nome";
    /**
     * The constant USERS_COGNOME.
     */
    public static final String USERS_COGNOME = "cognome";
    /**
     * The constant USERS_EMAIL.
     */
    public static final String USERS_EMAIL = "email";
    /**
     * The constant USERS_USERNAME.
     */
    public static final String USERS_USERNAME = "username_utente";
    /**
     * The constant USERS_PASSWORD.
     */
    public static final String USERS_PASSWORD = "password";
    /**
     * The constant USERS_RUOLO.
     */
    public static final String USERS_RUOLO = "ruolo";
    /**
     * The constant PAG_TITOLO.
     */
// Altre colonne nelle altre tabelle possono essere aggiunte qui
    // Nomi delle colonne nella tabella pagina
    public static final String PAG_TITOLO = "titolo";
    /**
     * The constant PAG_ID.
     */
    public static final String PAG_ID = "id_pagina";

    /**
     * The constant PAG_ORA.
     */
    public static final String PAG_ORA = "ora_creazione";
    /**
     * The constant PAG_DATA.
     */
    public static final String PAG_DATA = "data_creazione";
    /**
     * The constant PAG_USAUTORE.
     */
    public static final String PAG_USAUTORE = "username_autore";
    // Nomi delle colonne nella tabella proposta possono essere aggiunte qui
}
