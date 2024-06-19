package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import sample.Main;
import sample.database.DatabaseHandler;
import sample.model.Utente;
import sample.utils.LoginUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The type Signup controller.
 */
public class SignupController {
    // Dichiarazioni degli elementi grafici dalla schermata di registrazione
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button homeButton;

    @FXML
    private ImageView logoWikiButton;

    @FXML
    private Button signUpButton;

    @FXML
    private CheckBox signUpCheckAutore;

    @FXML
    private CheckBox signUpCheckUtente;

    @FXML
    private TextField signUpCognome;

    @FXML
    private TextField signUpEmail;

    @FXML
    private TextField signUpNome;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpUsername;
    private boolean registrationSuccessful = false;

    //Metodo per mostrare un Alert di errore con un pulsante "OK" e un messaggio specificato.
    private void showAlertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    // Metodo per mostrare un Alert di conferma con un pulsante "OK" e un messaggio specificato per poi andare in home.
    private void showAlertOkay(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        alert.setTitle("");
        alert.setHeaderText(null);
        Stage currentStage = (Stage) alert.getDialogPane().getScene().getWindow();

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Carica la nuova schermata (homepage.fxml)
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/homepage.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // Chiudi la finestra corrente (quella di registrazione)
                    Stage signUpStage = (Stage) signUpButton.getScene().getWindow();
                    signUpStage.setScene(scene);
                    signUpStage.setTitle("Home");
                    signUpStage.show();
                } catch (IOException e) {
                    e.printStackTrace(); // Gestisci l'eccezione appropriatamente
                }
            }
        });
    }

    // Metodo per verificare il formato dell'email
    private boolean isValidEmail(String email) {
        // Utilizza un'espressione regolare per verificare il formato dell'email
        String emailRegex = "^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]+$";
        return email.matches(emailRegex);
    }

    /**
     * Initialize.
     */
// Metodo di inizializzazione chiamato quando viene creato il controller
    @FXML
    void initialize() {

        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        logoWikiButton.setCursor(Cursor.HAND);
        homeButton.setCursor(Cursor.HAND);
        signUpButton.setCursor(Cursor.HAND);
        signUpCheckUtente.setCursor(Cursor.HAND);
        signUpCheckAutore.setCursor(Cursor.HAND);


        // Azione quando si fa clic sul logo per tornare alla homepage
        logoWikiButton.setOnMouseClicked(event -> {
            try {
                // Carica la homepage
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/homepage.fxml"));
                Parent root = loader.load();
                // Crea una nuova finestra per la homepage
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

        // Azione quando si fa clic sul pulsante di ritorno alla home
        homeButton.setOnAction(event -> {
            try {
                // Carica la homepage
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Homepage.fxml"));
                Parent root = loader.load();
                // Crea una nuova finestra per la homepage
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

        // Azione quando si fa clic sul pulsante di registrazione
        signUpButton.setOnAction(event -> {

            // Crea un nuovo utente
            createUser();
            // Se la registrazione è avvenuta con successo, passa alla schermata di login
            if(registrationSuccessful){
                // Carica la finestra di login senza bloccare l'esecuzione
                try {
                    // Carica la finestra di login senza bloccare l'esecuzione
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/sample/view/Homepage.fxml"));
                    loader.load();
                    // Crea una nuova finestra per la homepage
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setTitle("Home");
                    stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                    stage.setScene(new Scene(root));
                    stage.show();

                    // Chiudi la finestra corrente (quella di registrazione)
                    signUpButton.getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();

                }}
        });

        // Aggiungi un listener per il checkBox signUpCheckAutore
        signUpCheckAutore.setOnAction(event -> {
            if (signUpCheckAutore.isSelected()) {
                // Se signUpCheckAutore è selezionato, deseleziona signUpCheckUtente
                signUpCheckUtente.setSelected(false);
            }
        });

        // Aggiungi un listener per il checkBox signUpCheckUtente
        signUpCheckUtente.setOnAction(event -> {
            if (signUpCheckUtente.isSelected()) {
                // Se signUpCheckUtente è selezionato, deseleziona signUpCheckAutore
                signUpCheckAutore.setSelected(false);
            }
        });

    }

    // Metodo per creare un nuovo utente
    private void createUser() {
        // Ottieni i valori dai campi di input
        String nome = signUpNome.getText();
        String cognome = signUpCognome.getText();
        String username = signUpUsername.getText();
        String password = signUpPassword.getText();
        String email = signUpEmail.getText();
        String ruolo = "";

        // Determina il ruolo in base alla selezione delle checkbox
        if (signUpCheckUtente.isSelected()) {
            ruolo = "Utente";
        } else {
            ruolo = "Autore";
        }

        // Verifica se tutti i campi sono stati compilati
        if (nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || ruolo.isEmpty()) {
            // Se manca qualche campo, mostra un messaggio di errore
            System.out.println("Per favore, inserisci tutti i campi di registrazione!");
            showAlertError("Per favore, inserisci tutti i campi di registrazione.");
            registrationSuccessful = false;
            return; // Esci dal metodo senza registrare l'utente nel database
        }

        //Verifica se l'email sia corretta
        if (!isValidEmail(email)) {
            showAlertError("Inserisci un indirizzo email valido.");
            registrationSuccessful = false;
            return;
        }

        // Check if the password contains at least one uppercase letter, one symbol, or one number
        if (!password.matches(".*[A-Z].*") || !password.matches(".*\\d.*") || !password.matches(".*[^a-zA-Z\\d].*")) {
            showAlertError("La password deve contenere almeno una lettera maiuscola, un simbolo e un numero.");
            registrationSuccessful = false;
            return;
        }

        // Continua solo se tutti i campi sono stati compilati e la password soddisfa i requisiti
        Utente utente = new Utente(nome, cognome, username, password, email, ruolo);
        // Crea un gestore del database
        DatabaseHandler databaseHandler = new DatabaseHandler();

        try {
            // Registra il nuovo utente nel database
            System.out.println("Utente registrato con successo!");
            showAlertOkay("Utente registrato con successo!");
            databaseHandler.signUpUser(utente);
            registrationSuccessful = true;
            // Gestisci eventuali eccezioni durante la registrazione
        } catch (SQLException sqlException) {
            // Handle SQL exception
        } finally {
            try {
                if (databaseHandler != null) {
                    databaseHandler.closeDbConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Gestisci eventuali eccezioni durante la chiusura
            }
        }
    }



}