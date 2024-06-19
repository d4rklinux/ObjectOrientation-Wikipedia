package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;  // Assicurati di importare la classe Text corretta
import sample.database.DatabaseHandler;
import sample.model.Pagina;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.sql.SQLException;

/**
 * The type Visualizza pagina controller.
 */
// Definizione della classe controller per la visualizzazione di una pagina
public class VisualizzaPaginaController {

    // Dichiarazioni delle variabili di istanza
    private Pagina myPage;
    @FXML
    private Label title;

    @FXML
    private Label numPag;

    @FXML
    private Label pagineLabel;



    @FXML
    private TextArea textArea;

    @FXML
    private Label dateLabel;

    @FXML
    private Label hourLabel;

    @FXML
    private Label autoreLabel;

    @FXML
    private Text testoPag;  // Aggiungi questa dichiarazione

    private CellRicercaController cellRicercaController;

    private CellRicercaHomepageController cellRicercaHomepageController;

    private CellAutoreController cellAutoreController;
    private CellHomepageController cellHomepageController;
    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    /**
     * Gets titolo corrente.
     *
     * @return the titolo corrente
     */
    public String getTitoloCorrente() {
        return myPage.getTitolo();
    }
    // Metodo per impostare il controller della cella di ricerca

    /**
     * Sets cell ricerca controller.
     *
     * @param cellRicercaController the cell ricerca controller
     */
// Metodo per impostare il controller della cella di ricerca
    public void setCellRicercaController(CellRicercaController cellRicercaController) {
        this.cellRicercaController = cellRicercaController;
    }

    // Metodo per impostare il controller della cella di ricerca
    public void setCellRicercaHomepageController(CellRicercaHomepageController cellRicercaHomepageController) {
        this.cellRicercaHomepageController = cellRicercaHomepageController;
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

    /**
     * Sets cell home page controller.
     *
     * @param cellHomePageController the cell home page controller
     */
// Metodo per impostare il controller della cella autore
    public void setCellHomePageController(CellHomepageController cellHomePageController) {
        this.cellHomepageController = cellHomepageController;
    }

    /**
     * Initialize.
     *
     * @throws SQLException the sql exception
     */
// Metodo chiamato automaticamente durante l'inizializzazione del controller
    @FXML
    void initialize() throws SQLException {


    }

    /**
     * Sets pagina corrente.
     *
     * @param myPage    the my page
     * @param titolo    the titolo
     * @param us_autore the us autore
     */
// Metodo per impostare la pagina corrente e visualizzarne il titolo e il testo
    public void setPaginaCorrente(Pagina myPage,String titolo,String us_autore) {
        this.myPage = myPage;
        title.setText(titolo);



        try {
            // Recupero del testo della pagina dal database
            String testo = databaseHandler.visualizzaFrasi(titolo);

            // Formattare la data e l'ora e impostarle nelle etichette
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");  // Solo ora e minuti

            dateLabel.setText(myPage.getData_creazione().format(dateFormatter));
            hourLabel.setText(myPage.getOra_creazione().format(timeFormatter));

            String autore = databaseHandler.getAutoreByTitolo(titolo);
            // Stampare il titolo e il testo nel terminale
            System.out.println("Titolo: " + titolo);  // Stampare il titolo
            System.out.println("Testo: " + testo);   // Stampare il testo
            // Impostare il testo nella vista della pagina
            textArea.setText(testo);


            autoreLabel.setText(autore);

            int num_pagine=databaseHandler.getTotal_pagine(autore);
            numPag.setText(String.valueOf(num_pagine));


        } catch (SQLException ex) {
            // Gestione dell'eccezione nel caso di errore durante l'accesso al database
            System.out.println(ex.getMessage());
        }
    }



    }
