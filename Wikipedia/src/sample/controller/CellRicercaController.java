package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.model.Pagina;

/**
 * The type Cell ricerca controller.
 */
public class CellRicercaController extends ListCell<Pagina> {
    private ListView<Pagina> listView;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label hourLabel;

    @FXML
    private ImageView modifyIcon;

    @FXML
    private Label pageLabel;
    @FXML
    private Label autoreLabel;

    @FXML
    private ImageView readIcon;
    private VisualizzaPaginaController visualizzaPaginaController;
    private ModificaFrasiController modificaFrasiController;
    // Usato per caricare il layout FXML della cella
    private FXMLLoader fxmlLoader;

    /**
     * Sets visualizza pagina controller.
     *
     * @param visualizzaPaginaController the visualizza pagina controller
     */
    public void setVisualizzaPaginaController(VisualizzaPaginaController visualizzaPaginaController) {
        this.visualizzaPaginaController = visualizzaPaginaController;
    }
    // Pannello radice dell'elemento della lista
    @FXML
    private AnchorPane rootAnchorPane;

    // Oggetto per gestire l'accesso al database
    private DatabaseHandler databaseHandler = new DatabaseHandler();

    /**
     * Initialize.
     */
    // Metodo di inizializzazione, chiamato dopo il caricamento del layout FXML
    @FXML
    void initialize() {


        // Impostare il cursore della tua icona su Hand (un puntatore a forma di mano)
        readIcon.setCursor(Cursor.HAND);
        modifyIcon.setCursor(Cursor.HAND);

        // Aggiungere un gestore di eventi per il clic sull'icona
        readIcon.setOnMouseClicked(event -> {
            // Ottenere l'oggetto Pagina dalla cella corrente
            Pagina myPage = getItem();
            try {
                // Caricare la finestra di VisualizzaPagina utilizzando FXMLLoader
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/VisualizzaPagina.fxml"));
                // Ottenere la radice (root) del layout della finestra
                Parent root = loader.load();

                // Ottenere il controller associato alla finestra di VisualizzaPagina
                VisualizzaPaginaController visualizzaPaginaController = loader.getController();
                // Impostare la pagina corrente nel controller della finestra di VisualizzaPagina
                visualizzaPaginaController.setPaginaCorrente(myPage, myPage.getTitolo(),myPage.getUsername_autore());
                // Passare il riferimento al controller della cella di ricerca alla finestra di VisualizzaPagina
                visualizzaPaginaController.setCellRicercaController(CellRicercaController.this);
                // Creare una nuova scena utilizzando la radice (root) e visualizzarla in una nuova finestra
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Visualizza Pagina");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                // Gestire eventuali errori durante il caricamento della finestra
                e.printStackTrace();
            }
        });

        modifyIcon.setOnMouseClicked(event -> {
            // Ottenere l'oggetto Pagina dalla cella corrente
            Pagina myPage = getItem();
            try {
                // Caricare la finestra di VisualizzaPagina utilizzando FXMLLoader
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/ModificaFrasi.fxml"));
                // Ottenere la radice (root) del layout della finestra
                Parent root = loader.load();

                // Ottenere il controller associato alla finestra di VisualizzaPagina
                ModificaFrasiController modificaFrasiController = loader.getController();
                modificaFrasiController.setPaginaCorrente(myPage, myPage.getTitolo());

                // Passare il riferimento al controller della cella di ricerca alla finestra di VisualizzaPagina
               modificaFrasiController.setCellRicercaController(CellRicercaController.this);
                // Creare una nuova scena utilizzando la radice (root) e visualizzarla in una nuova finestra
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Modifica");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                // Gestire eventuali errori durante il caricamento della finestra
                e.printStackTrace();
            }
        });







    }

    // Metodo chiamato quando la cella deve essere aggiornata con nuovi dati
    @Override
    protected void updateItem(Pagina myPage, boolean empty) {
        super.updateItem(myPage, empty);

        // Se la cella è vuota o il dato è nullo, resetta il testo e la grafica
        if (empty || myPage == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Se il loader non è stato inizializzato, crea un nuovo loader
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/CellRicerca.fxml"));
                fxmlLoader.setController(this);
                // Carica il file FXML e gestisci eventuali eccezioni
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Imposta i valori dei campi della cella con i dati della Pagina
            pageLabel.setText(myPage.getTitolo());
            String nomeAutore= null;
            try {
                nomeAutore = databaseHandler.getAutoreByTitolo(myPage.getTitolo());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            autoreLabel.setText(nomeAutore);
            // Formattazione della data
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = myPage.getData_creazione().format(dateFormatter);
            dateLabel.setText(formattedDate);

            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedHour = myPage.getOra_creazione().format(hourFormatter);
            hourLabel.setText(formattedHour);
            int idpagina = myPage.getId_pagina();

        // Resetta il testo e imposta la grafica con l'AnchorPane radice
            setText(null);
            setGraphic(rootAnchorPane);
        }

    }




}
