package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import sample.Main;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.model.Pagina;

/**
 * The type Home page controller.
 */
public class HomePageController {
    // Dichiarazioni degli elementi grafici dalla cella FXML

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ToggleButton cercaButton;

    @FXML
    private Button citareWikiButton;

    @FXML
    private Button contattiButton;

    @FXML
    private Button copyrightButton;

    @FXML
    private Button faqButton;


    @FXML
    private Button loginButton;

    @FXML
    private Button loginSignupButton;

    @FXML
    private TextField scriviRicercaButton;

    @FXML
    private ListView<Pagina> listPage;

    private ObservableList<Pagina> pagine;


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


                    pagine.addAll(pagina);
                }

                // Se la lista è vuota, mostra un alert
                if (pagine.isEmpty()) {
                    System.out.println("Titolo non trovato." );
                    showAlert("Titolo non trovato", "Il titolo cercato non è presente nella lista.");
                } else {
                    // Imposta la lista delle pagine nel ListView e imposta la cella personalizzata
                    listPage.setItems(pagine);
                    listPage.setCellFactory(list -> new CellRicercaHomepageController());
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
            listPage.setCellFactory(list -> new CellHomepageController());
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'eccezione, ad esempio mostrando un messaggio di errore
        }
    }

    /**
     * Initialize.
     */
// Metodo di inizializzazione chiamato quando viene creato il controller
    @FXML
    void initialize() {

        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        loginButton.setCursor(Cursor.HAND);
        loginSignupButton.setCursor(Cursor.HAND);
        cercaButton.setCursor(Cursor.HAND);
        contattiButton.setCursor(Cursor.HAND);
        copyrightButton.setCursor(Cursor.HAND);
        citareWikiButton.setCursor(Cursor.HAND);
        faqButton.setCursor(Cursor.HAND);


        // Gestore dell'azione per il pulsante di login
        loginButton.setOnAction(event -> {
            // Porta gli utenti alla schermata di accesso
            loginButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/Login.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
            stage.setScene(new Scene(root));
            stage.show();
        });

        // Gestore dell'azione per il pulsante di registrazione/login
        loginSignupButton.setOnAction(event -> {
            // Porta gli utenti alla schermata di registrazione
            loginSignupButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/Signup.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Registrazione");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
            stage.setScene(new Scene(root));
            stage.show();
        });

        // Gestione del clic sul contattiButton
        contattiButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Contatti.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Contatti");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                contattiButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Gestione del clic sul contattiButton
        copyrightButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Copyright.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Copyright");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                copyrightButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Gestione del clic sul faqButton
        citareWikiButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/CitareWikipedia.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Citare Wikipedia");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                citareWikiButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Gestione del clic sul faqButton
        faqButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Faq.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("FAQ");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                faqButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Inizializza la lista osservabile per le pagine
        pagine = FXCollections.observableArrayList();

        // Inizializza l'oggetto per l'interazione con il database
        databaseHandler = new DatabaseHandler();

        // Collega la gestione dell'azione del bottone
        cercaButton.setOnAction(event -> handleCercaButtonAction());

        showAllPages();

    }


}


