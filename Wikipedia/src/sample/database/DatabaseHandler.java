package sample.database;
import sample.model.Pagina;
import sample.model.Proposta;
import sample.model.Utente;
import sample.service.DatabaseManager;
import javafx.scene.control.TextArea;
import sample.controller.StatoProposta;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * The type Database handler.
 */
public class DatabaseHandler extends Configs{
    /**
     * The Db connection.
     */
// Connessione al database
    Connection dbConnection;
    /**
     * The Database manager.
     */
// Oggetto per gestire le interazioni con il database
    DatabaseManager databaseManager = new DatabaseManager();
    /**
     * The Logger.
     */
// Logger per la registrazione degli errori
    Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    /**
     * Gets db connection.
     *
     * @return the db connection
     */
// Metodo per ottenere una connessione al database
    public  Connection getDbConnection() {
        String connectionString = "jdbc:postgresql://" + dbHost + ":"
                + dbPort + "/"
                + dbName;
        // Carica il driver JDBC per PostgreSQL
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException classNotFoundException) {
            logger.info(classNotFoundException.getMessage());
        }
        // Ottiene una connessione al database utilizzando la stringa di connessione e le credenziali
        try {
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        } catch (SQLException sqlException) {
            logger.info(sqlException.getMessage());
        }

        return dbConnection;
    }

    /**
     * Close db connection.
     *
     * @throws SQLException the sql exception
     */
// Metodo per chiudere la connessione al database
    public void closeDbConnection
    () throws SQLException {
        if (dbConnection != null && !dbConnection.isClosed()) {
            dbConnection.close();
        }
    }

    /**
     * Gets page by user.
     *
     * @param username the username
     * @return the page by user
     * @throws SQLException the sql exception
     */
    /* Metodo per ottenere le informazioni di una pagina in base all'utente
     * Viene eseguita una query sul database per ottenere il titolo, la data e l'ora
     * di una pagina scritta dall'utente specificato.
     */
    public ResultSet getPageByUser(String username) throws SQLException {
        // Costruzione della query SQL utilizzando le costanti definite nella classe Const
        String query =  "SELECT " + Const.PAG_TITOLO + "," + Const.PAG_DATA + "," + Const.PAG_ORA + "," + Const.PAG_ID +
                        " FROM "  + Const.PAG_TABLE  +
                        " WHERE " + Const.PAG_USAUTORE + "=?";
        // Creazione di un oggetto PreparedStatement per eseguire la query parametrica
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, username);
        // Esegue la query e restituisce il risultato
        return databaseManager.doQuery(preparedStatement);
    }

    /**
     * Sign up user.
     *
     * @param utente the utente
     * @throws SQLException the sql exception
     */
    /* Metodo per registrare un nuovo utente nel database
     * Viene eseguita una query di inserimento nel database utilizzando le informazioni
     * fornite dall'oggetto Utente passato come parametro.
     */
    public void signUpUser(Utente utente) throws SQLException {
        // Costruzione della query di inserimento SQL utilizzando le costanti definite nella classe Const
        String insert = "INSERT INTO "+ Const.USERS_TABLE +
                    "(" + Const.USERS_NOME +
                    "," + Const.USERS_COGNOME +
                    "," + Const.USERS_USERNAME +
                    "," + Const.USERS_PASSWORD +
                    "," + Const.USERS_EMAIL +
                    "," + Const.USERS_RUOLO +") "+" VALUES(?,?,?,?,?,?)";
        // Creazione di un oggetto PreparedStatement per eseguire la query parametrica di inserimento
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
        // Impostazione dei parametri della query con i valori dell'oggetto Utente
        preparedStatement.setString(1,utente.getNome());
        preparedStatement.setString(2,utente.getCognome());
        preparedStatement.setString(3,utente.getUsername());
        preparedStatement.setString(4,utente.getPassword());
        preparedStatement.setString(5,utente.getEmail());
        preparedStatement.setString(6,utente.getRuolo());
        // Esecuzione della query di inserimento nel database
        databaseManager.doQuery(preparedStatement);



    }

    /**
     * Gets user.
     *
     * @param utente the utente
     * @return the user
     * @throws SQLException the sql exception
     */
// Metodo per ottenere un utente dal database
    public ResultSet getUser(Utente utente) throws SQLException {
        ResultSet resultSet = null;
        // Verifica che siano stati forniti username e password
        if( !utente.getUsername().equals("") || !utente.getPassword().equals("")){
            String query =  " SELECT * " +
                            " FROM "  + Const.USERS_TABLE +
                            " WHERE " + Const.USERS_USERNAME + "=?" + " AND " + Const.USERS_PASSWORD + "=?";

            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setString(1,utente.getUsername());
            preparedStatement.setString(2,utente.getPassword());
            resultSet = preparedStatement.executeQuery();


        }else{
            System.out.println("Inserisci le tue credenziali");

        }
        return resultSet;
    }

    /**
     * Delete page.
     *
     * @param username the username
     * @param idpagina the idpagina
     * @throws SQLException the sql exception
     */
    /*
     * Metodo per eliminare una pagina dal database
     * Viene eseguita una query di eliminazione nel database utilizzando l'username e l'ID pagina specificati.
     */
    public void deletePage(String username, int idpagina ) throws SQLException {
        String query =  " DELETE FROM  " + Const.PAG_TABLE +
                        " WHERE " + Const.PAG_USAUTORE + "=?" +
                        " AND " + Const.PAG_ID + "=?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, idpagina);
        // Esegue la query di eliminazione nel database
        preparedStatement.execute();
        preparedStatement.close();

    }

    /**
     * Insert pagina.
     *
     * @param pagina the pagina
     * @throws SQLException the sql exception
     */
// * Inserisce una nuova pagina nel database.
    public void insertPagina(Pagina pagina) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getDbConnection();

            // Chiamata alla funzione PL/pgSQL inserisci_titolo_e_frasi
            String callFunction = "SELECT inserisci_titolo_e_frasi(?, ?, ?)"; // Nota l'utilizzo di SELECT

            preparedStatement = connection.prepareStatement(callFunction);

            // Impostazione dei parametri della chiamata alla funzione
            preparedStatement.setString(1, pagina.getTitolo());
            preparedStatement.setString(2, pagina.getTesto()); // Assumi che Pagina abbia un metodo getTesto()
            preparedStatement.setString(3, pagina.getUsername_autore());

            // Esecuzione della chiamata alla funzione
            preparedStatement.executeQuery();

        } finally {
            // Chiudi le risorse
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Search pages result set.
     *
     * @param title the title
     * @return the result set
     * @throws SQLException the sql exception
     */
// Costruzione della query per la ricerca di pagine con un titolo simile
    public ResultSet searchPages(String title) throws SQLException {
        String query =
                        "SELECT id_pagina, titolo, data_creazione, ora_creazione " +
                        "FROM " +Const.PAG_TABLE + " " +
                        "WHERE LOWER(titolo) LIKE LOWER(?)";

        // Preparazione della query
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, "%" + title + "%");
        // Esecuzione della query
        return preparedStatement.executeQuery();
    }

    /**
     * Gets all pages.
     *
     * @return the all pages
     * @throws SQLException the sql exception
     */
// Costruzione della query per ottenere tutte le pagine
    public ResultSet getAllPages() throws SQLException {
        String query =
                "SELECT id_pagina, titolo, data_creazione, ora_creazione " +
                "FROM " + Const.PAG_TABLE;
        // Preparazione della query
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        // Esecuzione della query
        return preparedStatement.executeQuery();
    }

    /**
     * Recupera e restituisce una stringa contenente il contenuto delle frasi associate a una pagina
     * identificata dal titolo.
     *
     * @param titolo Titolo della pagina di cui visualizzare le frasi
     * @return Stringa contenente il contenuto delle frasi associate alla pagina
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public String visualizzaFrasi(String titolo) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getDbConnection();

            // Controlla se la connessione è aperta
            if (connection == null) {
                throw new SQLException("Connessione al database non riuscita.");
            }

            // Chiamata alla funzione PL/pgSQL visualizza_frasi_per_pagina
            String callFunction =   "SELECT * " +
                                    "FROM visualizza_frasi_per_pagina(?)";
            preparedStatement = connection.prepareStatement(callFunction);

            // Impostazione dei parametri della chiamata alla funzione
            preparedStatement.setString(1, titolo);


            // Esecuzione della chiamata alla funzione
            resultSet = preparedStatement.executeQuery();

            // Elaborazione dei risultati
            StringBuilder resultStringBuilder = new StringBuilder();


            while (resultSet.next()) {
                String contenutoFrase = resultSet.getString("contenuto_frase");

                // Aggiungi il contenuto della frase alla StringBuilder
                resultStringBuilder.append(String.format("%s%n", contenutoFrase));
            }


            // Restituisci il risultato come stringa
            return resultStringBuilder.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'eccezione o visualizza un messaggio di errore all'utente
            throw e; // Rilancia l'eccezione per segnalare il problema
        } finally {
            // Chiudi le risorse
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Recupera e restituisce l'username dell'autore di una pagina identificata dal titolo.
     *
     * @param titolo Titolo della pagina di cui recuperare l'autore
     * @return Username dell'autore della pagina
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public String getAutoreByTitolo(String titolo) throws SQLException {
        String query =  "SELECT username_autore " +
                        "FROM pagina " +
                        "WHERE titolo = ?";

        try (PreparedStatement preparedStatement = getDbConnection().prepareStatement(query)) {
            preparedStatement.setString(1, titolo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Restituisci l'username_autore trovato
                    return resultSet.getString("username_autore");
                }
            }
        }

        // Se non viene trovato alcun risultato, puoi restituire null o gestire diversamente
        return null;
    }

    /**
     * Trova id frase con titolo int.
     *
     * @param titoloPagina          the titolo pagina
     * @param vecchioContenutoFrase the vecchio contenuto frase
     * @return the int
     * @throws SQLException the sql exception
     */
    public int trovaIdFraseConTitolo(String titoloPagina, String vecchioContenutoFrase) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getDbConnection();

            // Chiamata alla funzione PL/pgSQL trova_id_frase_con_titolo
            String callFunction = "SELECT trova_id_frase_con_titolo(?, ?)";

            preparedStatement = connection.prepareStatement(callFunction);

            // Impostazione dei parametri della chiamata alla funzione
            preparedStatement.setString(1, titoloPagina);
            preparedStatement.setString(2, vecchioContenutoFrase);

            // Esecuzione della chiamata alla funzione
            resultSet = preparedStatement.executeQuery();

            // Estrai il risultato dalla query
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                // Puoi gestire il caso in cui la query non restituisce risultati
                throw new SQLException("La funzione trova_id_frase_con_titolo non ha restituito alcun risultato.");
            }

        } finally {
            // Chiudi le risorse
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Insert proposta.
     *
     * @param proposta the proposta
     * @throws SQLException the sql exception
     */
    public void insertProposta(Proposta proposta) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getDbConnection();

            // Query di inserimento nella tabella proposta
            String insertQuery = "INSERT INTO proposta " +
                    "(vecchio_contenuto, " +
                    "nuovo_contenuto, " +
                    "id_frase, " +
                    "ora_proposta, " +
                    "data_proposta, " +
                    "username_utente_proposta) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(insertQuery);

            // Impostazione dei parametri della query
            preparedStatement.setString(1, proposta.getVecchio_contenuto());
            preparedStatement.setString(2, proposta.getNuovo_contenuto());
            preparedStatement.setInt(3, proposta.getId_frase());

            // Impostazione dell'ora e della data correnti
            Timestamp oraProposta = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(4, oraProposta);
            preparedStatement.setDate(5, new java.sql.Date(new java.util.Date().getTime()));

            preparedStatement.setString(6, proposta.getUsername_utente_proposta());

            // Esecuzione della query di inserimento
            preparedStatement.executeUpdate();

            System.out.println("Proposta inserita con successo!");

        } finally {
            // Chiudi le risorse
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Gets proposte ordered by data ora.
     *
     * @param autoreDestinatario the autore destinatario
     * @return the proposte ordered by data ora
     * @throws SQLException the sql exception
     */
    public ResultSet getProposteOrderedByDataOra(String autoreDestinatario) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getDbConnection();

            // Chiamata alla funzione PL/pgSQL get_proposte_ordered_by_data_ora
            String callFunction = "SELECT * FROM get_proposte_ordered_by_data_ora(?)";

            preparedStatement = connection.prepareStatement(callFunction);

            // Impostazione dei parametri della chiamata alla funzione
            preparedStatement.setString(1, autoreDestinatario);

            // Esecuzione della chiamata alla funzione
            resultSet = preparedStatement.executeQuery();

            return resultSet;

        }catch (SQLException e) {
            // Gestisci le eccezioni SQL
            e.printStackTrace();
            throw e;
        }  finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Update stato proposta.
     *
     * @param idProposta the id proposta
     * @param nuovoStato the nuovo stato
     * @throws SQLException the sql exception
     */
    public void updateStatoProposta(int idProposta, StatoProposta nuovoStato) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getDbConnection();

            // Query SQL per l'aggiornamento dello stato della proposta
            String query = "UPDATE proposta SET stato = ?::stato_proposta WHERE id_proposta = ?";
            preparedStatement = connection.prepareStatement(query);

            // Imposta i parametri nella query
            preparedStatement.setString(1, nuovoStato.toString()); // Converte StatoProposta a String
            preparedStatement.setInt(2, idProposta);

            // Esegui l'aggiornamento
            preparedStatement.executeUpdate();

            // Se necessario, esegui ulteriori azioni dopo l'aggiornamento

        } finally {
            // Chiudi le risorse
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Gets titolo pagina by id proposta.
     *
     * @param idProposta the id proposta
     * @return the titolo pagina by id proposta
     * @throws SQLException the sql exception
     */
    public String getTitoloPaginaByIdProposta(int idProposta) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String titoloPagina = null;

        try {
            connection = getDbConnection();

            // Query SQL per ottenere il titolo della pagina dalla tabella pagina
            String query = "SELECT p.titolo FROM pagina p JOIN frase f ON p.id_pagina = f.id_pagina "
                    + "JOIN proposta pr ON f.id_frase = pr.id_frase WHERE pr.id_proposta = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idProposta);

            resultSet = preparedStatement.executeQuery();

            // Se ci sono risultati, ottieni il titolo della pagina
            if (resultSet.next()) {
                titoloPagina = resultSet.getString("titolo");
            }

        } finally {
            // Chiudi le risorse
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return titoloPagina;
    }


    /**
     * Gets total modifiche.
     *
     * @param username the username
     * @return the total modifiche
     * @throws SQLException the sql exception
     */
    public double getTotal_modifiche(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getDbConnection();

            // Chiamata alla funzione PL/pgSQL calcola_ratio_modifiche
            String callFunction = "SELECT calcola_ratio_modifiche(?) AS ratio";

            preparedStatement = connection.prepareStatement(callFunction);

            // Impostazione dei parametri della chiamata alla funzione
            preparedStatement.setString(1, username);

            // Esecuzione della chiamata alla funzione
            resultSet = preparedStatement.executeQuery();

            // Estrai il risultato dal ResultSet
            if (resultSet.next()) {
                return resultSet.getDouble("ratio");
            } else {
                // Puoi gestire il caso in cui la query non restituisce risultati
                throw new SQLException("La funzione calcola_ratio_modifiche non ha restituito alcun risultato.");
            }

        } catch (SQLException e) {
            // Gestisci le eccezioni SQL
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


    /**
     * Gets total pagine.
     *
     * @param username the username
     * @return the total pagine
     * @throws SQLException the sql exception
     */
    public int getTotal_pagine(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getDbConnection();

            // Chiamata alla funzione PL/pgSQL calcola_totale_pagine_autore
            String callFunction = "SELECT calcola_totale_pagine_autore(?) AS total_pagine";

            preparedStatement = connection.prepareStatement(callFunction);

            // Impostazione dei parametri della chiamata alla funzione
            preparedStatement.setString(1, username);

            // Esecuzione della chiamata alla funzione
            resultSet = preparedStatement.executeQuery();

            // Estrai il risultato dal ResultSet
            if (resultSet.next()) {
                return resultSet.getInt("total_pagine");
            } else {
                // Puoi gestire il caso in cui la query non restituisce risultati
                throw new SQLException("La funzione calcola_totale_pagine_autore non ha restituito alcun risultato.");
            }

        } catch (SQLException e) {
            // Gestisci le eccezioni SQL
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }



}



















