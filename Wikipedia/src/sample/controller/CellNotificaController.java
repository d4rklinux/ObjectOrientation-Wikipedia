package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.model.Pagina;
import sample.model.Proposta;

/**
 * The type Cell notifica controller.
 */
public class CellNotificaController extends ListCell<Proposta> {
    @FXML
    private Label statoLabel;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label dateLabel;

    @FXML
    private Label hourLabel;

    @FXML
    private Label pageLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView readIcon;
    private FXMLLoader fxmlLoader;
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    @FXML
    private AnchorPane rootAnchorPane;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        readIcon.setCursor(Cursor.HAND);

        readIcon.setOnMouseClicked(event -> {
        // Ottenere l'oggetto Proposta dalla cella corrente
        Proposta proposta = getItem();
        try {
            // Caricare la finestra di VisualizzaPagina utilizzando FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/VisualizzaNotifica.fxml"));
            // Ottenere la radice (root) del layout della finestra
            Parent root = loader.load();

            // Ottenere il controller associato alla finestra di VisualizzaNotifica
            VisualizzaNotificaController visualizzaNotificaController = loader.getController();
            visualizzaNotificaController.setPaginaCorrente(proposta, proposta.getId_proposta(), proposta.getVecchio_contenuto(),proposta.getNuovo_contenuto(),proposta.getUsername_utente_proposta());


            // Passare il riferimento al controller della cella di ricerca alla finestra di VisualizzaPagina
            visualizzaNotificaController.setCellNotificaController(CellNotificaController.this);
            // Creare una nuova scena utilizzando la radice (root) e visualizzarla in una nuova finestra
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Proposta dell'utente");
            stage.setScene(scene);
            stage.show();

            // Chiudi la finestra corrente (quella di pubblicazione)
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (IOException e) {
            // Gestire eventuali errori durante il caricamento della finestra
            e.printStackTrace();
        }
    });


    }


    @Override
    protected void updateItem(Proposta proposta, boolean empty) {
        super.updateItem(proposta, empty);

        // Se la cella è vuota o il dato è nullo, resetta il testo e la grafica
        if (empty || proposta == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Se il loader non è stato inizializzato, crea un nuovo loader
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/CellNotifica.fxml"));
                fxmlLoader.setController(this);
                // Carica il file FXML e gestisci eventuali eccezioni
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            int idpagina = proposta.getId_proposta();
            // Imposta i valori dei campi della cella con i dati della Pagina
            pageLabel.setText(String.valueOf(idpagina));
            // Modifica per visualizzare "In attesa" separatamente
            String statoText = proposta.getStato() == StatoProposta.InAttesa ? "In attesa" : String.valueOf(proposta.getStato());
            statoLabel.setText(statoText);
            usernameLabel.setText(proposta.getUsername_utente_proposta());
            // Formattazione della data
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = proposta.getData_creazione().format(dateFormatter);
            dateLabel.setText(formattedDate);

            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedHour = proposta.getOra_creazione().format(hourFormatter);
            hourLabel.setText(formattedHour);



        }

        // Resetta il testo e imposta la grafica con l'AnchorPane radice
        setText(null);
        setGraphic(rootAnchorPane);

        // Verifica se la proposta è diversa da null e se il suo stato è "In Attesa"
        if (proposta != null && proposta.getStato() == StatoProposta.InAttesa) {
            // Se la condizione è soddisfatta, imposta uno stile di sfondo lightblue per evidenziare le proposte in attesa
            setStyle("-fx-background-color: lightblue;");
        } else {
            // Se la proposta è null o il suo stato non è "In Attesa", reimposta lo stile a quello predefinito
            setStyle("");
        }

    }

}


