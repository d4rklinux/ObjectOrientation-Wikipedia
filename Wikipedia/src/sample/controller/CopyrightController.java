package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;

/**
 * The type Copyright controller.
 */
public class CopyrightController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button contattiButton;

    @FXML
    private Button faqButton;

    @FXML
    private Button citareWikiButton;


    @FXML
    private Button homeButton;

    @FXML
    private ImageView logoWikiButton;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        // Imposta il tipo di cursore del pulsante su Cursor.HAND
        logoWikiButton.setCursor(Cursor.HAND);
        homeButton.setCursor(Cursor.HAND);
        contattiButton.setCursor(Cursor.HAND);
        faqButton.setCursor(Cursor.HAND);
        citareWikiButton.setCursor(Cursor.HAND);
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

        // Gestione del clic sul logoWikiButton
        citareWikiButton.setOnMouseClicked(event -> {
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

    }





}