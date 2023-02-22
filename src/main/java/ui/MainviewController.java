package ui;

import bank.PrivateBank;
import bank.exceptions.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainviewController {
    @FXML ListView<String> AccountListe;
    static PrivateBank privateBank;
    static String name;

    public String getname() {
        return privateBank.getName();
    }

    public void setName(String name) {
        this.name = privateBank.getName();
    }

    @FXML public void initialize() throws AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, InterestExceptions {
        privateBank = new PrivateBank("Bank1",0.05,0.1);
        AccountListe.getItems().addAll(privateBank.getAllAccounts());
    }

    private void update(){
        AccountListe.getItems().clear();
        AccountListe.getItems().addAll(privateBank.getAllAccounts());
    }

    /**
     * selects an account from the list for more details
     *
     * @param event
     * @throws IOException
     */
    @FXML public void auswaehlen(ActionEvent event) {
        try{

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Accountview.fxml"));
            name = AccountListe.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) AccountListe.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());

            stage.setScene(scene);

            AccountviewController accountviewController = fxmlLoader.getController();
            accountviewController.acc(AccountListe.getSelectionModel().getSelectedItem(), privateBank);
        }catch(RuntimeException | IOException e){
            e.printStackTrace();
            // System.out.println("Test");
        }
    }

    /**
     * deletes the selected account
     *
     * @param event
     * @throws AccountDoesNotExistException
     * @throws IOException
     */
    @FXML public void loeschen(ActionEvent event) throws AccountDoesNotExistException, IOException {
        String account = AccountListe.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Account "+ account + " löschen?", ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Delete account");
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES){
            privateBank.deleteAccount(account);
            update();
        }
    }

    /**
     * creates a new Account
     *
     * @param event
     * @throws AccountAlreadyExistsException
     * @throws IOException
     */
    public void newAccount(ActionEvent event) throws AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, InterestExceptions {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Account");
        dialog.setHeaderText("Name for the new account: ");
        Optional<String> accountname = dialog.showAndWait();

        if(accountname.isPresent()) {
            if (accountname.get().equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "AccountName must not be empty.", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
            } else {
                privateBank.createAccount(dialog.getResult());
            }
        }
        update();
    }
}