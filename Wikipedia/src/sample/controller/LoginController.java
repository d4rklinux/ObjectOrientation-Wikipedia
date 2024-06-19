package sample.controller;

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
import sample.animations.Shaker;
import sample.database.DatabaseHandler;
import sample.database.UserRoleQueryHandler;
import sample.model.Utente;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The type Login controller.
 */
public class LoginController {
    private static String USERNAME;

    /**
     * The constant username.
     */
// Variabile statica per memorizzare l'username dopo il login
    public static String username;
    // Dichiarazioni degli elementi grafici dalla cella FXML

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button SignupButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField loginUsername;

    @FXML
    private ImageView logoWikiButton;

    private void showAlertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Login Fallito");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    // Oggetto per gestire le interazioni con il database
    private DatabaseHandler databaseHandler;

    /**
     * Initialize.
     */
// Metodo di inizializzazione chiamato quando viene creato il controller
    @FXML
    void initialize() {

        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        logoWikiButton.setCursor(Cursor.HAND);
        homeButton.setCursor(Cursor.HAND);
        loginButton.setCursor(Cursor.HAND);
        SignupButton.setCursor(Cursor.HAND);

        // Inizializza l'oggetto per l'interazione con il database
        databaseHandler = new DatabaseHandler();
        // Richiedi il focus sulla casella di testo dell'username
        loginUsername.requestFocus();
        // Assicurati che la casella di testo dell'username sia abilitata
        loginUsername.setDisable(false);
        // Gestore dell'azione per il pulsante di accesso alla home

        // Gestore dell'azione dell icona home
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

        // Gestore dell'azione per il pulsante di home
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


        // Gestore dell'azione per il pulsante di registrazione
        SignupButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Signup.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Registrazione");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                stage.setScene(new Scene(root));
                stage.show();

                // Chiudi la finestra corrente (quella di registrazione)
                SignupButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Gestore dell'azione per il pulsante di accesso
        loginButton.setOnAction(event -> {
            // Ottieni l'username e la password inseriti dall'utente
            String loginText = loginUsername.getText().trim();
            String loginPwd = loginPassword.getText().trim();

            // Crea un oggetto Utente con i dati inseriti
            Utente utente = new Utente();
            utente.setUsername(loginText);
            utente.setPassword(loginPwd);

            // Esegui la query per ottenere l'utente dal database
            ResultSet userRow = null;
            try {
                userRow = databaseHandler.getUser(utente);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Contatore per verificare il numero di utenti restituiti dalla query
            int counter = 0;

            try {
                // Verifica se il result set è nullo prima di chiamare il metodo next()
                if (userRow != null) {
                    while (userRow.next()) {
                        counter++;
                        String nome = userRow.getString("nome");
                        USERNAME = userRow.getString("username_utente");
                        System.out.println("Login avvenuto con successo!");
                        System.out.println("Benvenuto " + nome);
                    }
                }

                if (counter == 1) {
                    showPagina();
                    // Chiudi la finestra corrente (quella di registrazione)
                    loginButton.getScene().getWindow().hide();
                } else {
                    System.out.println("Login Fallito");
                    showAlertError("Username o password inserita non corretta. Riprova.");
                    Shaker usernameShaker = new Shaker(loginUsername);
                    Shaker passwordShaker = new Shaker(loginPassword);
                    usernameShaker.shake();
                    passwordShaker.shake();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
    // Metodo per mostrare la pagina corrispondente al ruolo dell'utente
    private void showPagina() {
        String username = loginUsername.getText().trim();
        String password = loginPassword.getText().trim();

        // Ottieni il ruolo dell'utente dal database
        String ruolo = UserRoleQueryHandler.getUserRole(username, password);

        // Verifica il ruolo e mostra la pagina corrispondente
        if (ruolo != null) {
            try {
                FXMLLoader loader = new FXMLLoader();

                switch (ruolo) {
                    case "Autore":
                        loader.setLocation(getClass().getResource("/sample/view/PaginaAutore.fxml"));
                        break;
                    default:
                        loader.setLocation(getClass().getResource("/sample/view/PaginaUtente.fxml"));
                        break;
                }

                Parent root = loader.load();
                Stage stage = new Stage();
                // Imposta il titolo della finestra in base al ruolo dell'utente
                stage.setTitle("Pagina " + (ruolo.equals("Autore") ? "Autore" : "Utente"));
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/sample/images/iconwiki.png")));
                // Imposta la scena della finestra con il layout caricato
                stage.setScene(new Scene(root));

                // Se il ruolo è Autore, passa l'username al controller della PaginaAutore
                if (ruolo.equals("Autore")) {
                    PaginaAutoreController paginaAutoreController = loader.getController();
                    paginaAutoreController.setUsername(USERNAME);
                }

                // Chiudi la finestra di login corrente
                Stage loginStage = (Stage) loginUsername.getScene().getWindow();
                loginStage.close();

                // Mostra la finestra della pagina corrispondente
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ruolo non trovato");
        }
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public static String getUsername() {
        return USERNAME;
    }
}