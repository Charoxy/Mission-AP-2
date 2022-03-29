package fr.charoxy.ticket.panel;

import fr.charoxy.ticket.utils.SQLConnexion;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class UserPanel extends Parent {

    Stage stage;
    SQLConnexion connexion;

    TextArea text;
    TextField titre;
    Button button;

    public UserPanel(Stage stage , SQLConnexion connexion){

        this.connexion = connexion;
        this.stage = stage;



        text = new TextArea();
        text.setPromptText("Description");
        text.setLayoutX(25);
        text.setLayoutY(50);
        text.setPrefWidth(450);
        text.setPrefHeight(200);

        titre = new TextField();
        titre.setPromptText("Titre du ticket");
        titre.setLayoutX(25);

        button = new Button();
        button.setText("Crée le ticket");
        button.setLayoutX(200);
        button.setLayoutY(300);
        button.setOnAction(e -> {

            if(titre.getText() != null && text.getText() != null){

                connexion.createTicket(titre.getText(),text.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ticket");
                alert.setContentText("Le ticket a bien etais transmi");
                alert.showAndWait();
                titre.setText("");
                text.setText("");
            }

        });



        this.getChildren().addAll(titre,text,button);

    }


}
