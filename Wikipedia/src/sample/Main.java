package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * The type Main.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica la vista principale (homepage.fxml) utilizzando FXMLLoader
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/homepage.fxml"));
        // Imposta la scena con la vista principale e le dimensioni
        primaryStage.setTitle("Wikipedia");
        primaryStage.getIcons().add(new Image("/sample/images/iconwiki.png"));
        primaryStage.setScene(new Scene(root, 800, 700));
        // Mostra la finestra principale
        primaryStage.show();

    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
// Metodo principale per avviare l'applicazione
    public static void main(String[] args) {
        // Chiamata al metodo launch per avviare l'applicazione JavaFX
        launch(args);

    }
}
