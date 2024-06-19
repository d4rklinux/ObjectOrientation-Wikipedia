package sample.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type User role query handler.
 */
// Esempio di una classe ausiliaria per gestire la query del ruolo dell'utente
public class UserRoleQueryHandler {
    /**
     * Gets user role.
     *
     * @param username the username
     * @param password the password
     * @return the user role
     */
// Metodo per ottenere il ruolo dell'utente basato su username e password
    public static String getUserRole(String username, String password) {
        // Inizializza il ruolo a null
        String ruolo = null;
        // Verifica che username e password non siano vuoti
        if (!username.isEmpty() && !password.isEmpty()) {
            // Creazione della query SQL per ottenere il ruolo
            String query = "SELECT " + Const.USERS_RUOLO + " FROM " + Const.USERS_TABLE
                    + " WHERE " + Const.USERS_USERNAME + "=? AND " + Const.USERS_PASSWORD + "=?";

            try {
                // Creazione di un'istanza di DatabaseHandler
                DatabaseHandler databaseHandler = new DatabaseHandler();
                // Chiamata al metodo getDbConnection sull'istanza appena creata
                Connection connection = databaseHandler.getDbConnection();
                // Utilizzo del try-with-resources per gestire le risorse
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    // Esecuzione della query e ottenimento del risultato
                    ResultSet resultSet = preparedStatement.executeQuery();
                    // Se ci sono risultati, ottieni il ruolo
                    if (resultSet.next()) {
                        ruolo = resultSet.getString(Const.USERS_RUOLO);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Restituzione del ruolo ottenuto
        return ruolo;
    }
}

