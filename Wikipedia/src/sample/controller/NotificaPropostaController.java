package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;
import sample.database.DatabaseHandler;
import sample.model.Pagina;
import sample.model.Proposta;

/**
 * The type Notifica proposta controller.
 */
public class NotificaPropostaController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button homeButton;

    @FXML
    private ListView<Proposta> listProp;

    @FXML
    private ImageView logoWikiButton;

    @FXML
    private Button paginaAutoreButton;

    // Lista osservabile per le pagine dell'autore
    private ObservableList<Proposta> proposte;
    // Oggetto per gestire le interazioni con il database
    private DatabaseHandler databaseHandler;


    /**
     * Initialize.
     *
     * @throws SQLException the sql exception
     */
    @FXML
    void initialize() throws SQLException {
        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        logoWikiButton.setCursor(Cursor.HAND);
        homeButton.setCursor(Cursor.HAND);
        paginaAutoreButton.setCursor(Cursor.HAND);


        // Gestione del clic sul logoWikiButton
        logoWikiButton.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Homepage.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Home");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                logoWikiButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Gestione del clic sul homeButton
        homeButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Homepage.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Home");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                homeButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Apri la finestra della pagina autore e chiudi la finestra corrente
        paginaAutoreButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/PaginaAutore.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Pagina Autore");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                paginaAutoreButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ResultSet resultSet = null;
try{
        // Inizializzazione della lista delle pagine
        proposte = FXCollections.observableArrayList();
        // Inizializzazione dell'oggetto per l'interazione con il database
        databaseHandler = new DatabaseHandler();

        // Itera sui risultati del database e popola la lista delle pagine
       resultSet = databaseHandler.getProposteOrderedByDataOra(LoginController.getUsername());
        // Itera sui risultati del database e popola la lista delle pagine
        while (resultSet.next()) {
            Proposta proposta= new Proposta();
            proposta.setId_proposta(resultSet.getInt("id_proposta"));

            // Converte Timestamp a LocalDateTime e poi a LocalDate per data_creazione
            proposta.setData_creazione(resultSet.getTimestamp("data_proposta").toLocalDateTime().toLocalDate());

            // Converte Timestamp a LocalDateTime e poi a LocalTime per ora_creazione
            proposta.setOra_creazione(resultSet.getTimestamp("ora_proposta").toLocalDateTime().toLocalTime());
            proposta.setUsername_utente_proposta(resultSet.getString("username_utente_proposta"));
            proposta.setNuovo_contenuto(resultSet.getString("nuovo_contenuto"));
            proposta.setVecchio_contenuto(resultSet.getString("vecchio_contenuto"));
            proposta.setStato(mapStringToStato(resultSet.getString("stato")));


            proposte.addAll(proposta);
        }


        // Imposta la lista delle pagine nel ListView e imposta la cella personalizzata
        listProp.setItems( proposte);
        listProp.setCellFactory(list -> new CellNotificaController());
        listProp.refresh();


    }catch (SQLException e) {
    e.printStackTrace();
} finally {
    // Chiudi il ResultSet dopo averlo completamente utilizzato
    if (resultSet != null) {
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}}


    private StatoProposta mapStringToStato(String statoString) {
        // Confronta in modo case-insensitive per gestire "in attesa" e "InAttesa"
        if ("Accettata".equalsIgnoreCase(statoString)) {
            return StatoProposta.Accettata;
        } else if ("Rifiutata".equalsIgnoreCase(statoString)) {
            return StatoProposta.Rifiutata;
        } else if ("In attesa".equalsIgnoreCase(statoString) || "InAttesa".equalsIgnoreCase(statoString)) {
            return StatoProposta.InAttesa;
        } else {
            throw new IllegalArgumentException("Stato non valido: " + statoString);
        }
    }}
