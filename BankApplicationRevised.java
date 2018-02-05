/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankapplicationrevised;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author shree
 */
public class BankApplicationRevised extends Application {

    /**
     * @param args the command line arguments
     */
    private Account account1;
    private Account account2;
    private Locale locale;
    private ResourceBundle messages;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private static HashMap<String, String> users = new HashMap();

    public static void main(String[] args) {
        // TODO code application logic here

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        readFromFile();
        Label username = new Label("Username: ");
        Label password = new Label("Password: ");
        TextField uname = new TextField("username");
        PasswordField pword = new PasswordField();
        Button submit = new Button("submit");
        submit.setOnAction(e -> {
            if (authenticate(uname.getText(), pword.getText())) {
                account1 = new CheckingsAccount(uname.getText(), 500);
                account2 = new SavingsAccount(uname.getText(), 1000);
                getPreferredLanguage(primaryStage);
            } else {
                //System.out.println("Reached Alert Area");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Invalid Username & Password");
                alert.setContentText("Please enter username and password");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    uname.setText("");
                    pword.setText("");
                } else {

                }
            }
        });
        Button clear = new Button("clear");
        clear.setOnAction(e -> {
            uname.setText("");
            pword.setText("");
        });

        Button create = new Button("New User");
        create.setOnAction(e -> {
            createNewUser(primaryStage);
        });
        GridPane gp1 = new GridPane();
        gp1.setVgap(5);
        gp1.setHgap(5);
        gp1.setPadding(new Insets(10, 10, 10, 10));
        gp1.add(username, 0, 0);
        gp1.add(uname, 1, 0);
        gp1.add(password, 0, 1);
        gp1.add(pword, 1, 1);
        gp1.add(submit, 0, 2);
        gp1.add(clear, 1, 2);
        gp1.add(create, 2, 2);
        Scene scene = new Scene(gp1);
        primaryStage.setTitle("Bank Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void getPreferredLanguage(Stage primaryStage) {
        GridPane gp2 = new GridPane();
        Label language = new Label("Choose a preferred language:");
        Button en = new Button("English");
        en.setOnAction(e1 -> {
            getOperationsScreen(primaryStage, "en", "US");
        });
        Button es = new Button("Espanol");
        es.setOnAction(e2 -> {
            getOperationsScreen(primaryStage, "es", "MX");
        });
        gp2.setVgap(5);
        gp2.setHgap(5);
        gp2.setPadding(new Insets(10, 10, 10, 10));
        gp2.add(language, 1, 0);
        gp2.add(en, 1, 1);
        gp2.add(es, 1, 2);
        Scene scene = new Scene(gp2);
        primaryStage.setTitle("Language");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void getOperationsScreen(Stage primaryStage, String language, String country) {
        GridPane gp3 = new GridPane();
        gp3.setVgap(5);
        gp3.setHgap(5);
        gp3.setPadding(new Insets(10, 10, 10, 10));
        locale = new Locale(language, country);
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
        Label operation = new Label(messages.getString("mssg1"));
        Button withdraw = new Button(messages.getString("withdraw"));
        Button deposit = new Button(messages.getString("deposit"));
        Button balance = new Button(messages.getString("balance"));
        Button quit = new Button(messages.getString("quit"));
        gp3.add(operation, 1, 0);
        gp3.add(withdraw, 1, 1);
        gp3.add(deposit, 1, 2);
        gp3.add(balance, 1, 3);
        gp3.add(quit, 1, 4);
        withdraw.setOnAction(e1 -> {
            withdrawAction(primaryStage, language, country);
        });
        deposit.setOnAction(e2 -> {
            depositAction(primaryStage, language, country);
        });
        balance.setOnAction(e3 -> {
            balanceAction(primaryStage, language, country);
        });
        quit.setOnAction(e5 -> {
            writeToFile();
            primaryStage.close();
        });
        Scene scene = new Scene(gp3);
        primaryStage.setTitle(messages.getString("welcome"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void withdrawAction(Stage primaryStage, String language, String country) {
        GridPane gp4 = new GridPane();
        gp4.setVgap(5);
        gp4.setHgap(5);
        gp4.setPadding(new Insets(10, 10, 10, 10));
        locale = new Locale(language, country);
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
        Label l1 = new Label(messages.getString("withdrawMssg"));
        TextField amount = new TextField(messages.getString("amount"));
        Label l2 = new Label(messages.getString("withdrawAccount"));
        ObservableList<String> options = FXCollections.observableArrayList(messages.getString("checkings"), messages.getString("savings"));
        ComboBox list = new ComboBox(options);
        Button back = new Button(messages.getString("back"));
        back.setOnAction(e1 -> {
            getOperationsScreen(primaryStage, language, country);
        });
        amount.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("^\\d*(\\.\\d\\d)?")) {
                amount.setText(newValue.replaceAll("[^\\d*(\\.\\d\\d)?]", ""));
            }
        });
        Button ok = new Button(messages.getString("submit"));
        ok.setOnAction(e2 -> {
            System.out.println("AccountType" + (String) list.getValue());
            if ((String) list.getValue() != null && amount.getText() != null) {
                try {
                    submitAction((String) list.getValue(), "withdraw", Double.parseDouble(amount.getText()));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if ((String) list.getValue() == null && amount.getText() == null) {
                System.out.println("enter the details");
                // throw alerts
            } else if ((String) list.getValue() == null) {
                System.out.println("Please slect account Type");
            } else {
                System.out.println("please enter an amount");
            }
        });
        Button close = new Button(messages.getString("close"));
        close.setOnAction(e3 -> {
            primaryStage.close();
        });

        gp4.add(l1, 0, 0);
        gp4.add(amount, 1, 0);
        gp4.add(l2, 0, 1);
        gp4.add(list, 1, 1);
        gp4.add(ok, 0, 2);
        gp4.add(close, 1, 2);
        gp4.add(back, 2, 2);
        Scene scene = new Scene(gp4);
        primaryStage.setTitle(messages.getString("withdraw"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void depositAction(Stage primaryStage, String language, String country) {
        GridPane gp = new GridPane();
        gp.setVgap(5);
        gp.setHgap(5);
        gp.setPadding(new Insets(10, 10, 10, 10));
        locale = new Locale(language, country);
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
        Label l1 = new Label(messages.getString("depositMssg"));
        TextField amount = new TextField(messages.getString("amount"));
        Label l2 = new Label(messages.getString("depositAccount"));
        ObservableList<String> options = FXCollections.observableArrayList(messages.getString("checkings"), messages.getString("savings"));
        ComboBox list = new ComboBox(options);
        amount.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("^\\d*(\\.\\d\\d)?")) {
                amount.setText(newValue.replaceAll("^\\d*(\\.\\d\\d)?", ""));
            }
        });
        Button back = new Button(messages.getString("back"));
        back.setOnAction(e1 -> {
            getOperationsScreen(primaryStage, language, country);
        });
        Button ok = new Button(messages.getString("submit"));
        ok.setOnAction(e2 -> {
            System.out.println("AccountType" + (String) list.getValue());
            try {
                submitAction((String) list.getValue(), "deposit", Double.parseDouble(amount.getText()));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Button close = new Button(messages.getString("close"));
        close.setOnAction(e3 -> {
            primaryStage.close();
        });
        gp.add(l1, 0, 0);
        gp.add(amount, 1, 0);
        gp.add(l2, 0, 1);
        gp.add(list, 1, 1);
        gp.add(ok, 0, 2);
        gp.add(close, 1, 2);
        gp.add(back, 2, 2);
        Scene scene = new Scene(gp);
        primaryStage.setTitle(messages.getString("deposit"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void balanceAction(Stage primaryStage, String language, String country) {
        GridPane gp = new GridPane();
        gp.setVgap(5);
        gp.setHgap(5);
        gp.setPadding(new Insets(10, 10, 10, 10));
        locale = new Locale(language, country);
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
        Label l1 = new Label(messages.getString("balanceAccount"));
        ObservableList<String> options = FXCollections.observableArrayList(messages.getString("checkings"), messages.getString("savings"));
        ComboBox list = new ComboBox(options);
        Button back = new Button(messages.getString("back"));
        back.setOnAction(e1 -> {
            getOperationsScreen(primaryStage, language, country);
        });
        Button ok = new Button(messages.getString("submit"));
        ok.setOnAction(e2 -> {
            System.out.println("AccountType" + (String) list.getValue());
            try {
                submitAction((String) list.getValue(), "balance", 0);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Button close = new Button(messages.getString("close"));
        close.setOnAction(e3 -> {
            primaryStage.close();
        });
        gp.add(l1, 0, 1);
        gp.add(list, 1, 1);
        gp.add(ok, 0, 2);
        gp.add(close, 1, 2);
        gp.add(back, 2, 2);
        Scene scene = new Scene(gp);
        primaryStage.setTitle(messages.getString("balance"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void submitAction(String accountType, String actionType, double amount) throws FileNotFoundException {
        switch (accountType) {
            case "Checkings":
            case "Verificaciones":
                switch (actionType) {
                    case "withdraw":
                    case "retirar":
                        if (!(account1.withdraw(amount))) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Withdrawl Unsucessful");
                            alert.setHeaderText("Invalid amount");
                            alert.setContentText("Please check your balance.");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {

                            } else {

                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Withdrawl Successful");
                            alert.setHeaderText(amount + " was successfully withdrawn.");
                            alert.setContentText("Transaction from account: Checking");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                               reciept(account2.name,"Checking");
                            } else {

                            }
                            ((CheckingsAccount) account1).writeToFile();
                        }
                        break;
                    case "deposit":
                    case "depositar":
                        if ((account1.deposit(amount))) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Deposit Successful");
                            alert.setHeaderText(amount + " was successfully deposited.");
                            alert.setContentText("Transaction from account: Checking");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                              reciept(account2.name,"Checking");
                            } else {

                            }
                            ((CheckingsAccount) account1).writeToFile();
                        }
                        break;
                    case "balance":
                    case "consulta de saldo":
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Balance Enquiry");
                        alert.setHeaderText("Your Balance" + account1.balance());
                        alert.setContentText("Balnce of account: Checking");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                               reciept(account2.name,"Checking");
                        } else {

                        }
                        ((CheckingsAccount) account1).writeToFile();
                        break;
                    default:
                        break;
                }
                break;
            case "Savings":
            case "ahorros":
                switch (actionType) {
                    case "withdraw":
                    case "retirar":
                        if (!(account2.withdraw(amount))) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Withdrawl Unsucessful");
                            alert.setHeaderText("Invalid amount");
                            alert.setContentText("Please check your balance.");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {

                            } else {

                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Withdrawl Successful");
                            alert.setHeaderText(amount + " was successfully withdrawn.");
                            alert.setContentText("Transaction from account: Savings");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                    reciept(account2.name,"Saving");
                            } else {

                            }
                            ((SavingsAccount) account2).writeToFile();
                        }
                        break;
                    case "deposit":
                    case "depositar":
                        if ((account2.deposit(amount))) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Deposit Successful");
                            alert.setHeaderText(amount + " was successfully deposited.");
                            alert.setContentText("Transaction from account: Svaings");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                 reciept(account2.name,"Saving");
                            } else {

                            }
                            ((SavingsAccount) account2).writeToFile();
                        }
                        break;
                    case "balance":
                    case "consulta de saldo":
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Balance Enquiry");
                        alert.setHeaderText("Your Balance" + account2.balance());
                        alert.setContentText("Balnce of account: Savings");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                             reciept(account2.name,"Saving");
                        } else {

                        }
                        ((SavingsAccount) account2).writeToFile();
                        break;
                    default:
                        System.out.println("Invalid bank account");
                        break;
                }
        }
    }

    public void writeToFile() {
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File("UserAuthentication.bin")));
            oos.writeObject((HashMap) users);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean authenticate(String username, String password) {
        Set set = users.entrySet();
        if (set != null) {
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                System.out.println(me.getKey() + " " + me.getValue());
                if (username.equals(me.getKey()) && password.equals(me.getValue())) {

                    System.out.println("Login Successful");
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public void readFromFile() {
        File file = new File("UserAuthentication.bin");
        if (file.exists()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                users = ((HashMap) ois.readObject());//.entrySet();
                ois.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createNewUser(Stage primaryStage) {
        Label username = new Label("Username");
        TextField name = new TextField("Username");
        Label password = new Label("Password");
        PasswordField pf = new PasswordField();
        Label reEnter = new Label("Confirm Password");
        PasswordField rpf = new PasswordField();
        Button submit = new Button("create");
        Button clear = new Button("clear");
        Button back = new Button("return");
        GridPane gp = new GridPane();
        gp.setVgap(5);
        gp.setHgap(5);
        gp.setPadding(new Insets(10, 10, 10, 10));
        gp.add(username, 0, 0);
        gp.add(name, 1, 0);
        gp.add(password, 0, 1);
        gp.add(pf, 1, 1);
        gp.add(reEnter, 0, 2);
        gp.add(rpf, 1, 2);
        gp.add(submit, 0, 3);
        gp.add(clear, 1, 3);
        gp.add(back, 2, 3);
        submit.setOnAction(e -> {
            if ((!name.getText().isEmpty()) && pf.getText().equals(rpf.getText())) {
                System.out.println(name.getText());
                if (!users.containsKey(name.getText())) {
                    users.put(name.getText(), pf.getText());
                    writeToFile();
                    account1 = new CheckingsAccount(name.getText(), 500);
                    account2 = new SavingsAccount(name.getText(), 1000);
                    authenticate(name.getText(), pf.getText());
                    try {
                        start(primaryStage);
                    } catch (Exception ex) {
                        Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Already existing");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("New User Error");
                    alert.setHeaderText("Already Existing");
                    alert.setContentText("try login in");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        try {
                            start(primaryStage);
                        } catch (Exception ex) {
                            Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        name.setText("");
                        pf.setText("");
                        rpf.setText("");
                    }
                }
            } else if (name.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("New user error");
                alert.setHeaderText("Please enter an username");
                alert.setContentText("invalid username");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                } else {
                    name.setText("");
                    pf.setText("");
                    rpf.setText("");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("New User Error");
                alert.setHeaderText("Passwords doesn't match");
                alert.setContentText("try renetring passwords");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    pf.setText("");
                    rpf.setText("");
                } else {
                    pf.setText("");
                    rpf.setText("");
                }
            }
        });
        back.setOnAction(e -> {
            try {
                start(primaryStage);
            } catch (Exception ex) {
                Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        clear.setOnAction(e -> {
            name.setText("");
            pf.setText("");
            rpf.setText("");
        });
        Scene scene = new Scene(gp);
        primaryStage.setTitle("Create New User");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void reciept(String name, String type) throws FileNotFoundException {

        final Stage popup = new Stage();
        popup.setTitle("Transaction Summary");

        Scanner in = new Scanner(new File(name+ "'s"+type+"TransactionSummary.txt"));
        String contents = "";

        while (in.hasNext()) {
            contents += in.nextLine() + "\n";
        }
        in.close();

        TextArea ta = new TextArea(contents);
        ta.setEditable(false);

        HBox dialogHbox = new HBox(20);
        dialogHbox.setAlignment(Pos.CENTER);

        dialogHbox.getChildren().add(ta);

        Scene dialogScene = new Scene(dialogHbox, 500, 40);
        popup.setScene(dialogScene);
        popup.show();
    }
}
