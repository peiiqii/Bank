package ui;

import bank.*;
import bank.exceptions.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start (Stage stage) throws Exception{
        Parent anwendung = FXMLLoader.load(getClass().getResource("/Mainview.fxml"));
        Scene scene = new Scene(anwendung);
        stage.setTitle("PrivateBank");
        stage.setScene(scene);
        stage.show();
    }

    public static void main (String[] args) throws AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, InterestExceptions {
        launch(args);
    }
}
