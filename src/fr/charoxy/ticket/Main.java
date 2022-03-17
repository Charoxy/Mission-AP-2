package fr.charoxy.ticket;

import fr.charoxy.ticket.panel.LoginPanel;
import fr.charoxy.ticket.utils.SQLConnexion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    SQLConnexion sqlConnexion = new SQLConnexion("MDL",3307,"root","");

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        Scene scene = new Scene(new LoginPanel(primaryStage, sqlConnexion));
        scene.getStylesheets().add("fr/charoxy/ticket/style.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}
