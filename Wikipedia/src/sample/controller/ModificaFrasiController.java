package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;
import sample.database.DatabaseHandler;
import sample.model.Pagina;
import sample.model.Proposta;

/**
 * The type Modifica frasi controller.
 */
public class ModificaFrasiController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button InvioPropostaButton;

    @FXML
    private TextField nuovaFrase;

    @FXML
    private TextField vecchiaFrase;
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    @FXML
    private ImageView logoWikiButton;
    private CellRicercaController cellRicercaController;
    private CellAutoreController cellAutoreController;
    private Pagina myPage;

    /**
     * Sets cell ricerca controller.
     *
     * @param cellRicercaController the cell ricerca controller
     */
    public void setCellRicercaController(CellRicercaController cellRicercaController) {
        this.cellRicercaController = cellRicercaController;
    }

    /**
     * Sets cell autore controller.
     *
     * @param cellAutoreController the cell autore controller
     */
// Metodo per impostare il controller della cella autore
    public void setCellAutoreController(CellAutoreController cellAutoreController) {
        this.cellAutoreController = cellAutoreController;
    }



private VisualizzaPaginaController visualizzaPaginaController;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        InvioPropostaButton.setCursor(Cursor.HAND);

        InvioPropostaButton.setOnAction(event -> {
            String vecchio_contenuto = vecchiaFrase.getText().trim();
            String nuovo_contenuto = nuovaFrase.getText().trim();

            // Verifica che entrambi i campi siano compilati
            if (vecchio_contenuto.isEmpty() || nuovo_contenuto.isEmpty()) {
                showAlert("Errore", "Compila entrambi i campi correttamente");
                return; // Esci dal metodo se uno dei campi è vuoto
            }

            // Verifica che il vecchio e il nuovo contenuto siano diversi
            if (vecchio_contenuto.equals(nuovo_contenuto)) {
                showAlert("Errore", "Il vecchio e il nuovo contenuto devono essere diversi");
                return; // Esci dal metodo se il contenuto non è diverso
            }

            String titolo = myPage.getTitolo();
            int idFrase = 0;

            try {
                idFrase = databaseHandler.trovaIdFraseConTitolo(titolo, vecchio_contenuto);
            } catch (SQLException e) {
                showAlert("Errore", "Si è verificato un errore durante l'invio della proposta. Riprova più tardi.");
                return;
            }

            String username_utente_proposta = LoginController.getUsername();

            // Aggiungi un controllo per verificare se la proposta è stata inviata con successo
            if (!createProposta(vecchio_contenuto, nuovo_contenuto, idFrase, username_utente_proposta)) {
                // Se ci sono stati errori durante l'invio della proposta, esci senza mostrare l'alert
                return;
            }

            // Mostriamo l'alert dopo che la proposta è stata creata con successo
            showAlert("Proposta Inviata", "La proposta è stata inviata con successo!");

            // Otteniamo lo Stage associato all'evento e lo chiudiamo
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Sets pagina corrente.
     *
     * @param myPage the my page
     * @param titolo the titolo
     */
    public void setPaginaCorrente(Pagina myPage, String titolo) {
        this.myPage = myPage;

    }


    /**
     * Create proposta.
     *
     * @param vecchioContenuto       the vecchio contenuto
     * @param nuovoContenuto         the nuovo contenuto
     * @param idFrase                the id frase
     * @param usernameUtenteProposta the username utente proposta
     */
    public boolean createProposta(String vecchioContenuto, String nuovoContenuto, int idFrase, String usernameUtenteProposta) {
        try {
            // Creazione di un oggetto Proposta con i dati forniti
            Proposta nuovaProposta = new Proposta();
            nuovaProposta.setVecchio_contenuto(vecchioContenuto);
            nuovaProposta.setNuovo_contenuto(nuovoContenuto);

            nuovaProposta.setUsername_utente_proposta(usernameUtenteProposta);
            nuovaProposta.setId_frase(idFrase);

            // Chiamata al metodo per inserire la proposta nel database utilizzando il DatabaseHandler
            databaseHandler.insertProposta(nuovaProposta);

            // Altre azioni o notifiche a seconda delle esigenze dell'applicazione

            // Restituisci true se l'operazione è andata a buon fine
            return true;
        } catch (SQLException e) {
            // Gestione delle eccezioni, ad esempio mostrare un messaggio di errore
            showAlert("Errore durante l'invio della proposta", "Il contenuto del testo è diverso da quanto scritto in vecchia frase, Compila correttamente.");
            //Visualizzare un messaggio di errore all'utente o effettuare altre azioni di gestione degli errori
            System.out.println("Frase non presente nel contenuto da modificare");
            // Restituisci false se c'è stato un errore durante l'operazione
            return false;
        }
    }


    /**
     * Sets visualizza pagina controller.
     *
     * @param visualizzaPaginaController the visualizza pagina controller
     */
    public void setVisualizzaPaginaController(VisualizzaPaginaController visualizzaPaginaController) {
        this.visualizzaPaginaController = visualizzaPaginaController;
    }

}
