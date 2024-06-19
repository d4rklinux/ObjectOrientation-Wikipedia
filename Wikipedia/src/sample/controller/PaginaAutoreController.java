package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;
import sample.database.DatabaseHandler;
import sample.model.Pagina;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The type Pagina autore controller.
 */
public class PaginaAutoreController {
    private String USERNAME;
    // Dichiarazioni degli elementi grafici dalla schermata del controllore dell'autore
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Text salutoUtenteButton;

    @FXML
    private Button creaPaginaButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button cercaButton;

    @FXML
    private Button notificaButton;

    @FXML
    private ImageView logoWikiButton;

    @FXML
    private ListView<Pagina> listPage;

    @FXML
    private TextField loginUsername;

    @FXML
    private MenuBar notificheButton;

    // Lista osservabile per le pagine dell'autore
    private ObservableList<Pagina> pagine;
    // Oggetto per gestire le interazioni con il database
    private DatabaseHandler databaseHandler;

    /**
     * Initialize.
     *
     * @throws SQLException the sql exception
     */
// Metodo di inizializzazione chiamato quando viene creato il controller
    @FXML
    void initialize() throws SQLException {

        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        logoWikiButton.setCursor(Cursor.HAND);
        homeButton.setCursor(Cursor.HAND);
        creaPaginaButton.setCursor(Cursor.HAND);
        cercaButton.setCursor(Cursor.HAND);
        notificaButton.setCursor(Cursor.HAND);
        salutoUtenteButton.setText("Ciao, "+ LoginController.getUsername()+ "!");

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

        // Gestione del clic sul creaPaginaButton
        creaPaginaButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/CreaPagina.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Nuova Pagina");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();
                // Chiudi la finestra corrente (quella dell'autore)
                creaPaginaButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Imposta l'azione da eseguire quando il pulsante di ricerca viene premuto
        cercaButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/RisultatiRicerca.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Ricerca");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                cercaButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Gestione del clic sul homeButton
        notificaButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/NotificaProposta.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Proposta di Notifica");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                notificaButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Inizializzazione della lista delle pagine
        pagine = FXCollections.observableArrayList();
        // Inizializzazione dell'oggetto per l'interazione con il database
        databaseHandler = new DatabaseHandler();

        // Itera sui risultati del database e popola la lista delle pagine
        ResultSet resultSet = databaseHandler.getPageByUser(LoginController.getUsername());
        // Itera sui risultati del database e popola la lista delle pagine
        while (resultSet.next()) {
            Pagina pagina = new Pagina();
            pagina.setTitolo(resultSet.getString("titolo"));

            // Converte Timestamp a LocalDateTime e poi a LocalDate per data_creazione
            pagina.setData_creazione(resultSet.getTimestamp("data_creazione").toLocalDateTime().toLocalDate());

            // Converte Timestamp a LocalDateTime e poi a LocalTime per ora_creazione
            pagina.setOra_creazione(resultSet.getTimestamp("ora_creazione").toLocalDateTime().toLocalTime());

            pagina.setId_pagina(resultSet.getInt("id_pagina"));

            pagine.addAll(pagina);
        }

        // Imposta la lista delle pagine nel ListView e imposta la cella personalizzata
        listPage.setItems(pagine);
        listPage.setCellFactory(list -> new CellAutoreController());
    }


    /**
     * Sets username.
     *
     * @param USERNAME the username
     */
    public void setUsername(String USERNAME) {
        this.USERNAME = USERNAME;
    }



}
