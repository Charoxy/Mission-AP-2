package fr.charoxy.ticket.panel;

import fr.charoxy.ticket.utils.SQLConnexion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class AdminPanel extends Parent {

    ListView<String> listTache;
    SQLConnexion sqlConnexion;
    Button refresh, supprimer,modifier;
    Rectangle recinfo;
    ComboBox comboResponsable , comboEtat;


    Label desciption, titre, responsable, etat;

    public AdminPanel(SQLConnexion sqlConnexion, Stage stage) {
        this.getStylesheets().add("fr/charoxy/ticket/style.css");

        this.sqlConnexion = sqlConnexion;

        stage.setWidth(900);

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

        supprimer = new Button();
        supprimer.setText("supprimer le ticket");
        supprimer.setTranslateY(320);
        supprimer.setTranslateX(360);
        supprimer.getStyleClass().add("supprimer");
        supprimer.setOnAction(e -> {
            supprimerTicket();
        });


        comboResponsable = new ComboBox();
        comboResponsable.setItems(sqlConnexion.getResponsable());
        comboResponsable.getSelectionModel().select(0);
        comboResponsable.setTranslateX(600);
        comboResponsable.setTranslateY(50);

        ObservableList<String> etatL = FXCollections.observableArrayList();
        etatL.add("non commencer");
        etatL.add("en cours");
        etatL.add("fini");
        comboEtat = new ComboBox();
        comboEtat.setItems(etatL);
        comboEtat.getSelectionModel().select(0);
        comboEtat.setTranslateX(600);
        comboEtat.setTranslateY(100);

        modifier = new Button("Modifier le ticket");
        modifier.setTranslateX(600);
        modifier.setTranslateY(320);
        modifier.setOnAction(e -> {
            sqlConnexion.updateTicket(listTache.getSelectionModel().getSelectedItem().toString(),comboResponsable.getSelectionModel().getSelectedItem().toString(), comboEtat.getSelectionModel().getSelectedItem().toString());
            refreshTache();
        });


        this.getChildren().addAll(listTache, refresh, recinfo, desciption, titre, responsable, etat,supprimer,comboResponsable , comboEtat , modifier);
        refreshTache();

    }

    public void refreshList() {
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            Statement statement = sqlConnexion.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tache");
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
                    this.getChildren().addAll(comboEtat,comboResponsable,modifier,supprimer);
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
                this.getChildren().removeAll(comboEtat,comboResponsable,modifier,supprimer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerTicket() {
        String item = listTache.getSelectionModel().getSelectedItems().toString().replace("[", "").replace("]", "");
        try {
            Statement statement = sqlConnexion.getConnection().createStatement();
            statement.execute("DELETE FROM tache WHERE titre=\"" + item + "\"");
            refreshList();
            refreshTache();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}