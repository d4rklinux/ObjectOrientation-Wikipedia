package sample.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Database manager.
 */
public class DatabaseManager {

    /**
     * Do query result set.
     *
     * @param preparedStatement the prepared statement
     * @return the result set
     * @throws SQLException the sql exception
     */
// Metodo che esegue la query e restituisce il risultato
    public ResultSet doQuery(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeQuery();
    }
}



