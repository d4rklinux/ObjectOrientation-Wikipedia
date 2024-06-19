package sample.controller;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;
import sample.database.DatabaseHandler;
import sample.model.Pagina;
import sample.model.Proposta;

/**
 * The type Visualizza notifica controller.
 */
public class VisualizzaNotificaController {

    @FXML
    private Label numeroReputazione;
    @FXML
    private Label reputazioneUtente;

    @FXML
    private Button ConfermaButton;

    @FXML
    private Label label3;

    @FXML
    private Text titoloPag;

    @FXML
    private Label label1;

    @FXML
    private Label label2;
    private CellNotificaController cellNotificaController;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button notificheButton;

    @FXML
    private CheckBox accettaCheck;

    @FXML
    private Text nuovoContenuto;

    @FXML
    private CheckBox rifiutaCheck;
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    @FXML
    private Text vecchioContenuto;
    private Proposta proposta;

    /**
     * Sets cell notifica controller.
     *
     * @param cellNotificaController the cell notifica controller
     */
    public void setCellNotificaController(CellNotificaController cellNotificaController) {
        this.cellNotificaController = cellNotificaController;
    }


    /**
     * Sets pagina corrente.
     *
     * @param proposta the proposta
     * @param id       the id
     * @param vecchio  the vecchio
     * @param nuovo    the nuovo
     * @param username the username
     */
    public void setPaginaCorrente(Proposta proposta, int id,String vecchio,String nuovo,String username) {
        this.proposta = proposta;
        vecchioContenuto.setText(vecchio);
        nuovoContenuto.setText(nuovo);
        try {
            double ratio = databaseHandler.getTotal_modifiche(username);
            numeroReputazione.setText(String.valueOf(ratio));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String titolo=databaseHandler.getTitoloPaginaByIdProposta(id);
            titoloPag.setText(titolo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        ConfermaButton.setOnAction(event -> {
            StatoProposta stato;

            // Determina il ruolo in base alla selezione delle checkbox
            if (accettaCheck.isSelected()) {
                stato = StatoProposta.Accettata;
                try {
                    databaseHandler.updateStatoProposta(id, stato);
                    mostraAlert("Proposta accettata!");

                    // Disabilita il pulsante dopo l'accettazione
                    ConfermaButton.setDisable(true);


                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/NotificaProposta.fxml"));
                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Proposta di Notifica");
                        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                        stage.setScene(new Scene(root));
                        stage.show();

                        // Chiudi la finestra corrente (quella di pubblicazione)
                        ((Node) event.getSource()).getScene().getWindow().hide();
                    } catch (IOException e) {
                        e.printStackTrace(); // o gestisci l'eccezione in base alle tue esigenze
                    }

                    // Stampa l'esito su terminale
                    System.out.println("Proposta accettata!");

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                stato = StatoProposta.Rifiutata;

                // Chiedi conferma prima di rifiutare la proposta
                Alert confermaAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confermaAlert.setTitle("Conferma Rifiuto Proposta");
                confermaAlert.setHeaderText(null);
                confermaAlert.setContentText("Sei sicuro di voler rifiutare la proposta?");
                Optional<ButtonType> result = confermaAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        databaseHandler.updateStatoProposta(id, stato);
                        mostraAlert("Proposta rifiutata!");

                        // Disabilita il pulsante dopo il rifiuto
                        ConfermaButton.setDisable(true);
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/NotificaProposta.fxml"));
                            Parent root = loader.load();

                            Stage stage = new Stage();
                            stage.setTitle("Proposta di notifica");
                            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                            stage.setScene(new Scene(root));
                            stage.show();

                            // Chiudi la finestra corrente (quella di pubblicazione)
                            ((Node) event.getSource()).getScene().getWindow().hide();
                        } catch (IOException e) {
                            e.printStackTrace(); // o gestisci l'eccezione in base alle tue esigenze
                        }

                        // Stampa l'esito su terminale
                        System.out.println("Proposta rifiutata!");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });

// Disabilita il pulsante se la proposta è già stata accettata o rifiutata
        if (proposta.getStato() == StatoProposta.Accettata || proposta.getStato() == StatoProposta.Rifiutata) {
            ConfermaButton.setDisable(true);
        }
    }

    private void mostraAlert(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stato Proposta");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }


    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        notificheButton.setCursor(Cursor.HAND);
        ConfermaButton.setCursor(Cursor.HAND);
        accettaCheck.setCursor(Cursor.HAND);
        rifiutaCheck.setCursor(Cursor.HAND);

        accettaCheck.setOnAction(event -> {
            if (accettaCheck.isSelected()) {
                // Se signUpCheckAutore è selezionato, deseleziona signUpCheckUtente
                rifiutaCheck.setSelected(false);
            }
        });


        rifiutaCheck.setOnAction(event -> {
            if (rifiutaCheck.isSelected()) {
                // Se signUpCheckAutore è selezionato, deseleziona signUpCheckUtente
                accettaCheck.setSelected(false);
            }
        });

        //Azione per ritornare alle Proposte di notifiche
        notificheButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/NotificaProposta.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Notifiche di Proposta");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
               notificheButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}
