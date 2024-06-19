package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;
import sample.database.DatabaseHandler;
import sample.model.Pagina;

/**
 * The type Risultati ricerca controller.
 */
public class RisultatiRicercaController {

    @FXML
    private ImageView logoWikiButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button cercaButton;

    @FXML
    private Button paginaAutoreButton;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Pagina> listPage;

    // Caricatore FXML per gestire la creazione dell'interfaccia utente
    private FXMLLoader fxmlLoader;
    @FXML
    private TextField scriviRicercaButton;

    // Lista osservabile per contenere le pagine visualizzate nella ListView
    private ObservableList<Pagina> pagine;

    // Oggetto per gestire l'accesso al database
    private DatabaseHandler databaseHandler = new DatabaseHandler();

    // Metodo per mostrare un alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        // Aggiungi un evento che viene eseguito dopo che l'utente ha premuto "OK"
        alert.setOnHidden(evt ->
                showAllPages());
                scriviRicercaButton.clear(); // Cancella il testo dalla TextField
        alert.showAndWait();

    }

    /**
     * Handle cerca button action.
     */
// Metodo chiamato quando il pulsante di ricerca viene premuto
    @FXML
    void handleCercaButtonAction() {
        // Ottieni il titolo di ricerca dalla TextField
        String titoloRicerca = scriviRicercaButton.getText();

        // Esegui la ricerca solo se il titolo di ricerca non è vuoto
        try {
            if (titoloRicerca.isEmpty()) {
                // Se la casella di ricerca è vuota, mostra tutte le pagine
                showAllPages();
            } else {
                // Esegui la ricerca solo se il titolo di ricerca non è vuoto
                System.out.println("Titolo Ricercato: " + titoloRicerca);
                ResultSet resultSet = databaseHandler.searchPages(titoloRicerca);

                // Itera sui risultati del database e popola la lista delle pagine
                pagine.clear(); // Pulisci la lista delle pagine prima di aggiungere i nuovi risultati
                while (resultSet.next()) {
                    Pagina pagina = new Pagina();
                    pagina.setTitolo(resultSet.getString("titolo"));
                    pagina.setData_creazione(resultSet.getTimestamp("data_creazione").toLocalDateTime().toLocalDate());
                    pagina.setOra_creazione(resultSet.getTimestamp("ora_creazione").toLocalDateTime().toLocalTime());
                    pagina.setId_pagina(resultSet.getInt("id_pagina"));
                  //  pagina.setUsername_autore(resultSet.getString("username_autore"));

                    pagine.addAll(pagina);
                }

                // Se la lista è vuota, mostra un alert
                if (pagine.isEmpty()) {
                    System.out.println("Titolo non trovato." );
                    showAlert("Titolo non trovato", "Il titolo cercato non è presente nella lista.");
                } else {
                    // Imposta la lista delle pagine nel ListView e imposta la cella personalizzata
                    listPage.setItems(pagine);
                    listPage.setCellFactory(list -> new CellRicercaController());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'eccezione, ad esempio mostrando un messaggio di errore
        }
    }

    // Ottieni tutte le pagine dal database
    private void showAllPages() {
        try {
            // Ottieni tutte le pagine dal database
            ResultSet resultSet = databaseHandler.getAllPages();

            // Itera sui risultati del database e popola la lista delle pagine
            pagine.clear(); // Pulisci la lista delle pagine prima di aggiungere i nuovi risultati
            while (resultSet.next()) {
                Pagina pagina = new Pagina();
                pagina.setTitolo(resultSet.getString("titolo"));
                pagina.setData_creazione(resultSet.getTimestamp("data_creazione").toLocalDateTime().toLocalDate());
                pagina.setOra_creazione(resultSet.getTimestamp("ora_creazione").toLocalDateTime().toLocalTime());
                pagina.setId_pagina(resultSet.getInt("id_pagina"));
               // pagina.setUsername_autore(resultSet.getString("username_autore"));

                pagine.addAll(pagina);
            }

            // Imposta la lista delle pagine nel ListView e imposta la cella personalizzata
            listPage.setItems(pagine);
            listPage.setCellFactory(list -> new CellRicercaController());
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'eccezione, ad esempio mostrando un messaggio di errore
        }
    }

    /**
     * Initialize.
     */
// Metodo di inizializzazione, chiamato dopo il caricamento del layout FXML
    @FXML
    void initialize() {

        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        logoWikiButton.setCursor(Cursor.HAND);
        homeButton.setCursor(Cursor.HAND);
        paginaAutoreButton.setCursor(Cursor.HAND);
        cercaButton.setCursor(Cursor.HAND);


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

        pagine = FXCollections.observableArrayList();
        // Inizializzazione dell'oggetto per l'interazione con il database
        databaseHandler = new DatabaseHandler();

        // Collega la gestione dell'azione del bottone
        cercaButton.setOnAction(event -> handleCercaButtonAction());

        //Visualizza tutte le pagine all'interno di Wikipedia
        showAllPages();



    }


}



