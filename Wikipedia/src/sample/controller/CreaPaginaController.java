package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import sample.Main;
import sample.database.DatabaseHandler;
import sample.model.Pagina;

/**
 * The type Crea pagina controller.
 */
public class CreaPaginaController {
    // Dichiarazioni degli elementi grafici dalla cella FXML

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button elencoTestiButton;
    @FXML
    private TextArea testoArea;

    @FXML
    private Button homeButton;

    @FXML
    private ImageView logoWikiButton;

    @FXML
    private Button pubblicaTestoButton;

    @FXML
    private TextField titleButton;

    /**
     * Show success alert.
     */
    public void showSuccessAlert() {
        Alert confermaAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confermaAlert.setTitle("Conferma pubblicazione");
        confermaAlert.setHeaderText("Sei sicuro di voler pubblicare il testo?");
        confermaAlert.setContentText("Una volta pubblicato, il testo sarà visibile agli altri utenti.");

        ButtonType confermaButton = new ButtonType("Conferma");
        ButtonType annullaButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        confermaAlert.getButtonTypes().setAll(confermaButton, annullaButton);

        Optional<ButtonType> result = confermaAlert.showAndWait();
    }

    private void showEmptyTestoAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText(null);
        alert.setContentText("Il titolo o il testo non può essere vuoto. Inserisci un titolo e un testo prima di pubblicare.");
        alert.showAndWait();
    }

    private DatabaseHandler databaseHandler = new DatabaseHandler();

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        logoWikiButton.setCursor(Cursor.HAND);
        homeButton.setCursor(Cursor.HAND);
        elencoTestiButton.setCursor(Cursor.HAND);
        pubblicaTestoButton.setCursor(Cursor.HAND);

        // Azione quando si fa clic sul logo per tornare alla homepage
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

        // Azione quando si fa clic sul logo per tornare alla homepage
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

        // Azione quando si fa clic sul logo per tornare alla Pagina Autore
        elencoTestiButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/PaginaAutore.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Home");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                elencoTestiButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Azione quando si fa clic sul logo per pubblicare un testo
        pubblicaTestoButton.setOnAction(event -> {
            // Ottenere i dati della pagina che l'utente vuole pubblicare
            String titolo = titleButton.getText(); // Ottieni il titolo dall'interfaccia
            String testo = testoArea.getText().trim(); // Ottieni il testo associato alla pagina dall'interfaccia
            LocalDateTime now = LocalDateTime.now();

            LocalDate dataCreazione = now.toLocalDate(); // Estrai la data
            LocalTime oraCreazione = now.toLocalTime(); // Estrai l'orario

            String autore = LoginController.getUsername(); // Puoi ottenere l'autore da altri elementi dell'interfaccia o da dove desideri

            // Aggiungi una condizione per verificare se il titolo o il testo sono vuoti
            boolean isTitoloVuoto = titolo.trim().isEmpty();
            boolean isTestoVuoto = testo.trim().isEmpty();

            if (isTitoloVuoto || isTestoVuoto) {
                // Mostra un messaggio di avviso se il titolo o il testo sono vuoti
                showEmptyTestoAlert();
            } else {
                // Chiamare il metodo per creare e inserire la pagina nel database
                createPagina(titolo, dataCreazione, oraCreazione, autore, testo);
                showSuccessAlert();

                // Aprire la homepage
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/paginaAutore.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setTitle("Pagina Autore");
                    stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                    stage.setScene(new Scene(root));
                    stage.show();

                    // Chiudi la finestra corrente (quella di pubblicazione)
                    ((Node) event.getSource()).getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Pagina Creata con successo!");
            }
        });


    }

    /**
     * Create pagina.
     *
     * @param titolo         the titolo
     * @param dataCreazione  the data creazione
     * @param oraCreazione   the ora creazione
     * @param usernameAutore the username autore
     * @param testo          the testo
     */
    public void createPagina(String titolo, LocalDate dataCreazione, LocalTime oraCreazione, String usernameAutore, String testo) {
       try {
           // Creazione di un oggetto Pagina con i dati forniti
           Pagina nuovaPagina = new Pagina();
           nuovaPagina.setTitolo(titolo);
           nuovaPagina.setData_creazione(dataCreazione);
           nuovaPagina.setOra_creazione(oraCreazione);
           nuovaPagina.setUsername_autore(usernameAutore);
           nuovaPagina.setTesto(testo); // Aggiungi il metodo setTesto() se non è già presente nella classe Pagina

           // Chiamata al metodo per inserire la pagina nel database utilizzando il DatabaseHandler
           databaseHandler.insertPagina(nuovaPagina);

           // Altre azioni o notifiche a seconda delle esigenze dell'applicazione
       } catch (SQLException e) {
           // Gestione delle eccezioni, ad esempio mostrare un messaggio di errore
           e.printStackTrace();
           // Puoi anche visualizzare un messaggio di errore all'utente o effettuare altre azioni di gestione degli errori
       }
   }







}
