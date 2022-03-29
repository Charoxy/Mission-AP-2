package fr.charoxy.ticket.panel;

import fr.charoxy.ticket.utils.SQLConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RespPanel extends Parent {

    ListView<String> listTache;
    Label desciption, titre, responsable, etat;
    SQLConnexion sqlConnexion;
    Button refresh,modifier;
    Rectangle recinfo;
    ComboBox comboEtat;

    public RespPanel(Stage stage , SQLConnexion sqlConnexion){

        this.getStylesheets().add("fr/charoxy/ticket/style.css");
        this.sqlConnexion = sqlConnexion;

        listTache = new ListView<String>();
        refreshList();
        listTache.getSelectionModel().select(0);
        listTache.setOnMouseClicked(e -> {
            System.out.println(listTache.getSelectionModel().getSelectedItems());
            refreshTache();
        });

        refresh = new Button("Refresh");
        refresh.setTranslateX(100);
        refresh.setTranslateY(410);
        refresh.setOnAction(e -> {
            refreshList();
            refreshTache();
        });

        recinfo = new Rectangle();
        recinfo.setTranslateX(300);
        recinfo.setTranslateY(10);
        recinfo.setWidth(250);
        recinfo.setHeight(300);
        recinfo.getStyleClass().add("rec");

        titre = new Label();
        titre.setTranslateX(310);
        titre.setTranslateY(10);
        titre.getStyleClass().add("titre");

        desciption = new Label();
        desciption.setTranslateX(310);
        desciption.setTranslateY(50);
        desciption.setMaxHeight(100);
        desciption.setMaxWidth(180);
        desciption.getStyleClass().add("texte");
        desciption.setWrapText(true);


        responsable = new Label();
        responsable.setTranslateX(300);
        responsable.setTranslateY(250);
        responsable.getStyleClass().add("texte");

        etat = new Label();
        etat.setTranslateX(300);
        etat.setTranslateY(270);
        etat.getStyleClass().add("texte");

        ObservableList<String> etatL = FXCollections.observableArrayList();
        etatL.add("non commencer");
        etatL.add("en cours");
        etatL.add("fini");
        comboEtat = new ComboBox();
        comboEtat.setItems(etatL);
        comboEtat.getSelectionModel().select(0);
        comboEtat.setTranslateX(300);
        comboEtat.setTranslateY(350);

        modifier = new Button("Modifier le ticket");
        modifier.setTranslateX(300);
        modifier.setTranslateY(400);
        modifier.setOnAction(e -> {
            sqlConnexion.updateTicket(listTache.getSelectionModel().getSelectedItem().toString(),sqlConnexion.getUsername(), comboEtat.getSelectionModel().getSelectedItem().toString());
            refreshTache();
        });
        this.getChildren().addAll(recinfo,listTache,desciption,titre,responsable,etat,refresh);

        refreshList();
        refreshTache();
        stage.setWidth(600);

    }

    public void refreshList() {
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            Statement statement = sqlConnexion.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tache WHERE responsable=\""+sqlConnexion.getUsername()+ "\"");
            while (resultSet.next()) {
                items.add(resultSet.getString(4));
                listTache.getSelectionModel().select(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listTache.setItems(items);
    }

    public void refreshTache() {

        String item = listTache.getSelectionModel().getSelectedItems().toString().replace("[", "").replace("]", "");
        try {
            Statement statement = sqlConnexion.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tache WHERE titre=\"" + item + "\"");
            int count = 0;
            while (resultSet.next()) {
                count++;
                if(!this.getChildren().contains(modifier)){
                    this.getChildren().addAll(comboEtat,modifier);
                }
                titre.setText(item);
                desciption.setText(resultSet.getString(1));
                responsable.setText("Responsable : " + resultSet.getString(2));
                etat.setText("Etat : " + resultSet.getString(3));
            }
            if (count == 0){
                titre.setText("Il n'y a pas de ticket");
                desciption.setText("");
                responsable.setText("");
                etat.setText("");
                this.getChildren().removeAll(comboEtat,modifier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
