package ui;

import bank.*;
import bank.exceptions.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AccountviewController implements Initializable {

    private String account;
    private PrivateBank privateBank;

    @FXML ListView<Transaction> list;
    @FXML Label accountName;
    @FXML Label balance;

    @FXML public void acc(String name, PrivateBank pb){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Accountview.fxml"));
        MainviewController mvc = fxmlLoader.getController();




    }

    public void refresh() {
        list.getItems().clear();
        list.getItems().addAll(privateBank.getTransactions(account));
    }

    @FXML
    public void deleteTrans() throws TransactionDoesNotExistException, IOException, AccountDoesNotExistException {
        Transaction transaction = list.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Transaction "+ transaction + " löschen?", ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Delete Transaction");
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES){
            privateBank.removeTransaction(account, transaction);
            refresh();
        }
    }

    @FXML
    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Mainview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) list.getScene().getWindow();
        stage.setTitle("PrivateBank");
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void sortasc() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        list.getItems().clear();
        list.getItems().addAll(privateBank.getTransactionsSorted(account, true));
    }

    @FXML public void sortdec() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        list.getItems().clear();
        list.getItems().addAll(privateBank.getTransactionsSorted(account, false));
    }

    @FXML public void getpos() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        list.getItems().clear();
        list.getItems().addAll(privateBank.getTransactionsByType(account,true));
    }

    @FXML public void getneg() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        list.getItems().clear();
        list.getItems().addAll(privateBank.getTransactionsByType(account,false));
    }

    public void createTrans(ActionEvent actionEvent) throws TransactionAlreadyExistException, AccountDoesNotExistException, IOException, TransactionAttributeException, InterestExceptions {
        var dialog = new Dialog<>();
        dialog.setTitle("new Transaction");
        dialog.getDialogPane().getScene().getWindow().sizeToScene();

        var addT = new ButtonType("add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addT,ButtonType.CANCEL);

        var choices = new ArrayList<>();
        choices.add("Transfer");
        choices.add("Payment");

        var TType = new ChoiceBox<>();
        TType.getItems().addAll(choices);

        var general = new GridPane();

        general.add(new Label("Transaction Type: "),0,0);
        general.add(TType,1,0);

        var date = new TextField();
        var amount = new TextField();
        var description = new TextField();

        //extra fields needed for Transfer
        var sender = new TextField();
        var reciever = new TextField();

        TType.addEventFilter(ActionEvent.ACTION,event->{
            if(TType.getSelectionModel().getSelectedItem() == "Transfer"){
                var transfer = new GridPane();
                transfer.add(new Label("Transaction Type: "),0,0);
                transfer.add(TType,1,0);

                transfer.add(new Label("date: "),0,1);
                transfer.add(date,1,1);

                transfer.add(new Label("amount: "),0,2);
                transfer.add(amount,1,2);

                transfer.add(new Label("description: "),0,3);
                transfer.add(description,1,3);

                transfer.add(new Label("sender: "),0,4);
                transfer.add(sender,1,4);

                transfer.add(new Label("reciever"),0,5);
                transfer.add(reciever,1,5);

                dialog.getDialogPane().setContent(transfer);
                dialog.getDialogPane().getScene().getWindow().sizeToScene();
            }
            if(TType.getSelectionModel().getSelectedItem()== "Payment"){
                var payment = new GridPane();
                payment.add(new Label("Transaction Type: "),0,0);
                payment.add(TType,1,0);

                payment.add(new Label("date: "),0,1);
                payment.add(date,1,1);

                payment.add(new Label("amount: "),0,2);
                payment.add(amount,1,2);

                payment.add(new Label("description: "),0,3);
                payment.add(description,1,3);

                dialog.getDialogPane().setContent(payment);
                dialog.getDialogPane().getScene().getWindow().sizeToScene();
            }
        });
        // set the grid and show dialog
        dialog.getDialogPane().setContent(general);

        //get input
        var result =  dialog.showAndWait();

        if(result.isPresent() && result.get() == addT){
            if(date.getText().isEmpty()||amount.getText().isEmpty()||description.getText().isEmpty()){
                var alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Input cannot be empty.");
                alert.showAndWait();
                return;
            }
            else{
                System.out.println(date.getText()+amount.getText()+description.getText());
            }

            double damount = Double.parseDouble(amount.getText());

            if(TType.getSelectionModel().getSelectedItem() == "Payment"){
                var nPay = new Payment(date.getText(),damount,description.getText());
                privateBank.addTransaction(account,nPay);
            }

            if(TType.getSelectionModel().getSelectedItem() =="Transfer"){
                if(sender.getText().isEmpty()||reciever.getText().isEmpty()){
                    var alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Input cannot be empty.");
                    alert.showAndWait();
                    return;
                }

                var trans = new Transfer(date.getText(),damount,description.getText(),sender.getText(),reciever.getText());
                if(account.equals(sender.getText())){
                    var nTrans = new OutgoingTransfer(trans);
                    privateBank.addTransaction(account,nTrans);
                }
                if(account.equals(reciever.getText())){
                    var ninTrans = new IncomingTransfer(trans);
                    privateBank.addTransaction(account,ninTrans);
                }
            }
            refresh();
        } else {
            System.out.println(result.get());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        account = MainviewController.name;
        privateBank = MainviewController.privateBank;
        System.out.println(account);

        try{
            accountName.setText("Account: " + account);
            balance.setText("Account Balance: " + privateBank.getAccountBalance(account) + " €"); //eine Gleitkommazahl mit zwei Nachkommastellen
            list.getItems().clear();
            list.getItems().addAll(privateBank.getTransactions(account));}
        catch(IOException | TransactionAlreadyExistException | AccountAlreadyExistsException |
              AccountDoesNotExistException | TransactionAttributeException | InterestExceptions e){
            e.printStackTrace();
        }
    }
}