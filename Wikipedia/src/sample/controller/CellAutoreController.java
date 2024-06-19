package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.model.Pagina;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;



/**
 * Controller per la visualizzazione di una cella in una ListView di pagine associate a un autore.
 */
public class CellAutoreController extends ListCell<Pagina> {

    // Dichiarazioni degli elementi grafici dalla cella FXML
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView deleteIcon;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label hourLabel;

    @FXML
    private ImageView modifyIcon;

    @FXML
    private Label pageLabel;

    @FXML
    private ImageView readIcon;

    @FXML
    private AnchorPane rootAnchorPane;

    // Dichiarazione del loader per il file FXML
    private FXMLLoader fxmlLoader;
    private DatabaseHandler databaseHandler = new DatabaseHandler();

    /**
     * Metodo di inizializzazione chiamato quando viene creato il controller.
     * Inizializza il tipo di cursore dei pulsanti e gestisce l'evento di visualizzazione della pagina.
     */
    @FXML
    void initialize() {

        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        readIcon.setCursor(Cursor.HAND);
        modifyIcon.setCursor(Cursor.HAND);
        deleteIcon.setCursor(Cursor.HAND);

        // Gestione dell'icona di visualizzazione
        readIcon.setOnMouseClicked(event -> {
            // Ottiene l'oggetto Pagina associato all'elemento corrente nella ListView
            Pagina myPage = getItem();
            try {
                // Carica il file FXML associato alla visualizzazione della pagina
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/VisualizzaPagina.fxml"));
                Parent root = loader.load();
                // Ottiene il controller associato al file FXML caricato
                VisualizzaPaginaController visualizzaPaginaController = loader.getController();
                // Imposta la pagina corrente nel controller della visualizzazione pagina
                visualizzaPaginaController.setPaginaCorrente(myPage, myPage.getTitolo(),myPage.getUsername_autore());
                // Imposta il riferimento a questo controller della cella nell'istanza VisualizzaPaginaController
                visualizzaPaginaController.setCellAutoreController(CellAutoreController.this);
                // Crea una nuova scena usando la radice (root) appena creata
                Scene scene = new Scene(root);
                // Crea una nuova finestra (Stage) per visualizzare la pagina
                Stage stage = new Stage();
                stage.setTitle("Visualizza Pagina");
                // Imposta la scena appena creata nella finestra
                stage.setScene(scene);
                // Mostra la finestra
                stage.show();
            } catch (IOException e) {
                // Gestisce eventuali eccezioni durante il caricamento del file FXML o la creazione della finestra
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
                modificaFrasiController.setCellAutoreController(CellAutoreController.this);
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

/**
 * Metodo chiamato ogni volta che la cella deve essere aggiornata con nuovi dati.
 * Aggiorna la visualizzazione della cella con i dettagli della Pagina.
 */
    @Override
    protected void updateItem(Pagina myPage, boolean empty) {
        super.updateItem( myPage, empty);

        // Se la cella è vuota o il dato è nullo, resetta il testo e la grafica
        if( empty ||  myPage == null){
            setText(null);
            setGraphic(null);
        }else {
            // Se il loader non è stato inizializzato, crea un nuovo loader
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/CellAutore.fxml"));
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
            // Formattazione della data
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = myPage.getData_creazione().format(dateFormatter);
            dateLabel.setText(formattedDate);
            // Formattazione dell'ora
            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedHour = myPage.getOra_creazione().format(hourFormatter);
            hourLabel.setText(formattedHour);
            int idpagina = myPage.getId_pagina();

            // Gestione dell'icona di eliminazione
            deleteIcon.setOnMouseClicked(event -> {
                // Creazione dell'Alert di conferma
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Conferma eliminazione");
                alert.setHeaderText(null);
                alert.setContentText("Sei sicuro di voler eliminare questa pagina?");

                // Ottenere il pulsante premuto
                Optional<ButtonType> result = alert.showAndWait();

                // Verificare se l'utente ha premuto il pulsante OK
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        // Elimina la pagina dal database
                        databaseHandler.deletePage(LoginController.getUsername(), idpagina);

                        // Rimuovi la cella dalla ListView
                        getListView().getItems().remove(getItem());

                        // Carica la nuova pagina senza chiudere la finestra corrente
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/PaginaAutore.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) deleteIcon.getScene().getWindow();
                        stage.setScene(scene);

                        System.out.println("Pagina Eliminata");
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
            // Resetta il testo e imposta la grafica con l'AnchorPane radice
            setText(null);
            setGraphic(rootAnchorPane);
        }
    }

