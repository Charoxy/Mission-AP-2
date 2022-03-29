package fr.charoxy.ticket.panel;

import fr.charoxy.ticket.utils.SQLConnexion;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPanel extends Parent {

    TextField username;
    PasswordField password;
    Button login;

    public LoginPanel(Stage stage , SQLConnexion sqlConnexion){

        username = new TextField();
        username.setPromptText("Nom d'utilisateur");
        username.setTranslateY(100);

        password = new PasswordField();
        password.setPromptText("Mot de passe");
        password.setTranslateY(130);

        login = new Button("Connexion");
        login.setTranslateY(160);
        login.getStyleClass().add("btn");
        login.setOnAction(e ->{
            sqlConnexion.setLogin(username.getText(),password.getText());
            if(sqlConnexion.checkLogin()){

                System.out.println("\n===============");
                System.out.println("Username : " + sqlConnexion.getUsername());
                System.out.println("Grade    : " + sqlConnexion.getGrade());
                System.out.println("===============");

                switch (sqlConnexion.getGrade()){
                    case "admin":
                        stage.setScene(new Scene(new AdminPanel(sqlConnexion,stage)));
                        break;
                    case "responsable":
                        stage.setScene(new Scene(new RespPanel(stage,sqlConnexion)));
                        break;
                    case "utilisateur":
                        stage.setScene(new Scene(new UserPanel(stage,sqlConnexion)));
                        break;
                }
            }

        });

        this.getChildren().addAll(username , password , login);

    }


}
