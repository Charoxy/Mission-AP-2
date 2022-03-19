package fr.charoxy.ticket.utils;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

public class SQLConnexion {

    private Connection connection;
    private String username,mdp;

    public SQLConnexion(String bdd , int port , String user,String mdp){

        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:" + port + "/" + bdd ,user ,mdp);
            System.out.println("* Ticket > Connexion a la base de donnée OK.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setLogin(String username , String mdp){

        this.mdp = mdp;
        this.username = username;

    }

    public boolean checkLogin(){
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM compte WHERE username=\""+username+"\" AND mdp=\""+mdp+"\"");
            if(resultSet.next()){
                System.out.println("* Ticket > Connexion au compte OK");
                return true;
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("mauvais coople (mots de passe / username)");
                alert.showAndWait();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getGrade(){

        Statement statement = null;

        try {
            statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM compte WHERE username=\""+username+"\" AND mdp=\""+mdp+"\"");
            if(resultSet.next()){
               return resultSet.getString(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateTicket(String titre, String responsable, String etat){

        Statement statement = null;
        System.out.println(titre + responsable + etat + "");
        try {
            statement = getConnection().createStatement();
            statement.execute("UPDATE mdl.tache SET responsable=\""+responsable+"\", etat=\""+etat+"\" WHERE titre=\""+ titre +"\";");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ObservableList<String> getResponsable(){

        Statement statement = null;
        ObservableList<String> reponsable = FXCollections.observableArrayList();

        try {
            statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM compte WHERE grade=\"responsable\"");
            while (resultSet.next()){
                reponsable.add(resultSet.getString(1));
            }
            return reponsable;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createTicket(String titre ,String description){

        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO mdl.tache\n" +
                                "(description, responsable, etat, titre)\n" +
                                "VALUES(\"" + description + "\", NULL, NULL, \"" + titre + "\");");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Connection getConnection() {
        return connection;
    }

    public String getUsername() {
        return username;
    }
}
