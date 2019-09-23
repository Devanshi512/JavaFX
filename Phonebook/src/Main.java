import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

public class Main extends javafx.application.Application {
    Label lblName, lblNumberTitle, lblNumber, lblNoContact;
    Button btnAdd, btnEdit, btnCancelAdd, btnDoneAdd, btnDoneEdit, btnCancelEdit, btnDelete, btnAddFirstContact;
    TextField txtNameAdd, txtNumberAdd, txtNameEdit, txtNumberEdit;

    ObservableList<Contact> mainDb = FXCollections.observableArrayList();
    TextField search;
    ListView<Contact> contacts;

    File file = new File("src/phonebook.db");

    @Override
    public void start(Stage stage) throws Exception {
        // styling
        String btnStyle =
            "-fx-background-color: #475561;" +
            "-fx-border-color: white;" +
            "-fx-text-fill: white;" +
            "-fx-border-style: dashed;" +
            "-fx-border-radius: 3;" +
            "-fx-font-size: 10;";

        String msgStyle =
            "-fx-text-fill: white;" +
            "-fx-padding: 10;";

        String controlBoxStyle =
            "-fx-background-color: #475561;" +
            "-fx-padding: 10;";

        String txtStyle =
            "-fx-background-color: #5b6e7e;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);";

        String lblNameStyle =
            "-fx-font-size: 20;" +
            "-fx-text-fill: white;";

        String lblStyle =
            "-fx-text-fill: white;";

        // left pane
        search = new TextField();
            search.setPromptText("Search");
            search.setStyle(txtStyle);
        contacts = new ListView<>();
            contacts.setStyle("-fx-control-inner-background: #475561;");
        VBox leftBox = new VBox(search, contacts);
            leftBox.setStyle("-fx-background-color: #475561;");
            leftBox.setSpacing(10);
            leftBox.setPadding(new Insets(10));

        // contact view
        lblName = new Label("Name");
            lblName.setStyle(lblNameStyle);
        lblNumberTitle = new Label("Number: ");
        lblNumber = new Label("");
            lblNumber.setStyle(lblStyle);
        HBox numberBox = new HBox(lblNumberTitle, lblNumber);
            numberBox.setAlignment(Pos.CENTER);
            numberBox.setPadding(new Insets(20, 0, 0, 0));
        btnAdd = new Button("+");
            btnAdd.setStyle(btnStyle);
        btnEdit = new Button("Edit");
            btnEdit.setStyle(btnStyle);
        btnDelete = new Button("Delete");
            btnDelete.setStyle(btnStyle);
        VBox contactViewBox = new VBox(lblName, numberBox);
            contactViewBox.setStyle("-fx-background-color: #475561");
            contactViewBox.setAlignment(Pos.CENTER);
        HBox controlBoxOne = new HBox(btnAdd, btnEdit, btnDelete);
            controlBoxOne.setStyle(controlBoxStyle);
            controlBoxOne.setSpacing(5);

        // add view
        txtNameAdd = new TextField();
            txtNameAdd.setPromptText("Name");
            txtNameAdd.setStyle(txtStyle);
        txtNumberAdd = new TextField();
            txtNumberAdd.setPromptText("Phone");
            txtNumberAdd.setStyle(txtStyle);
//        HBox numberBoxAdd = new HBox(lblNumberTitle, txtNumberAdd);
//            numberBoxAdd.setAlignment(Pos.CENTER);
//            numberBoxAdd.setPadding(new Insets(20, 0, 0, 0));
        VBox contactAddBox = new VBox(txtNameAdd, txtNumberAdd);
            contactAddBox.setStyle("-fx-background-color: #475561");
            contactAddBox.setPadding(new Insets(10));
            contactAddBox.setAlignment(Pos.CENTER);
            contactAddBox.setSpacing(10);
        btnCancelAdd = new Button("Cancel");
            btnCancelAdd.setStyle(btnStyle);
        btnDoneAdd = new Button("Done");
            btnDoneAdd.setStyle(btnStyle);
            btnDoneAdd.setDisable(true);
            btnDoneAdd.setDefaultButton(true);
        HBox controlBoxTwo = new HBox(btnCancelAdd, btnDoneAdd);
            controlBoxTwo.setStyle(controlBoxStyle);
            controlBoxTwo.setSpacing(5);

        // edit view
        txtNameEdit = new TextField();
            txtNameEdit.setStyle(txtStyle);
        txtNumberEdit = new TextField();
            txtNumberEdit.setStyle(txtStyle);
        HBox numberBoxEdit = new HBox(lblNumberTitle, txtNumberEdit);
            numberBoxEdit.setAlignment(Pos.CENTER);
            numberBoxEdit.setPadding(new Insets(20, 0, 0, 0));
        VBox contactEditBox = new VBox(txtNameEdit, txtNumberEdit);
            contactEditBox.setStyle("-fx-background-color: #475561");
            contactEditBox.setPadding(new Insets(10));
            contactEditBox.setAlignment(Pos.CENTER);
            contactEditBox.setSpacing(10);
        btnCancelEdit = new Button("Cancel");
            btnCancelEdit.setStyle(btnStyle);
        btnDoneEdit = new Button("Done");
            btnDoneEdit.setStyle(btnStyle);
            btnDoneEdit.setDefaultButton(true);
        HBox controlBoxThree = new HBox(btnCancelEdit, btnDoneEdit);
            controlBoxThree.setStyle(controlBoxStyle);
            controlBoxThree.setSpacing(5);

        // no contact view
        lblNoContact = new Label("No contacts added yet");
            lblNoContact.setStyle(msgStyle);
        btnAddFirstContact = new Button("Add Contact");
            btnAddFirstContact.setStyle(btnStyle);
        VBox noContactPane = new VBox(lblNoContact, btnAddFirstContact);
            noContactPane.setAlignment(Pos.CENTER);
            noContactPane.setStyle("-fx-background-color: #475561");

        BorderPane rightBox = new BorderPane();
            rightBox.setCenter(contactViewBox);
            rightBox.setBottom(controlBoxOne);

        BorderPane pane = new BorderPane();
            pane.setLeft(leftBox);
            pane.setCenter(rightBox);

        populateDb();
        contacts.getSelectionModel().selectFirst();
        Contact c = contacts.getSelectionModel().getSelectedItem();
        if (c != null) {
            lblName.setText(c.getName());
            lblNumber.setText(c.getNumber());
        } else {
            pane.setCenter(noContactPane);
        }

        Scene scene = new Scene(pane, 600, 300);
        stage.setScene(scene);
        stage.setTitle("Phonebook");
        stage.show();

//        * * * events * * *
//        contacts.setOnMouseClicked(e -> {
//            Contact contact = contacts.getSelectionModel().getSelectedItem();
//            System.out.println(contact.getName() + ": " + contact.getNumber());
//        });

        contacts.getSelectionModel().selectedItemProperty().addListener(e -> {
            try {
                Contact contact = contacts.getSelectionModel().getSelectedItem();
                lblName.setText(contact.getName());
                lblNumber.setText(contact.getNumber());
            } catch (NullPointerException ex) {
                // avoid NullPointerException
            }
        });

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            search(newValue);
        });

        btnAdd.setOnAction(e -> {
            rightBox.setCenter(contactAddBox);
            rightBox.setBottom(controlBoxTwo);

            leftBox.setDisable(true);

            txtNameAdd.requestFocus();
        });

        btnAddFirstContact.setOnAction(e -> {
            pane.setCenter(rightBox);
            btnAdd.fire();
        });

        btnCancelAdd.setOnAction(e -> {
            if (mainDb.size() == 0) {
                pane.setCenter(noContactPane);
            } else {
                txtNameAdd.setText("");
                txtNumberAdd.setText("");

                rightBox.setCenter(contactViewBox);
                rightBox.setBottom(controlBoxOne);

                leftBox.setDisable(false);
            }
        });

        btnEdit.setOnAction(e -> {
            rightBox.setCenter(contactEditBox);
            rightBox.setBottom(controlBoxThree);

            leftBox.setDisable(true);

            Contact contact = contacts.getSelectionModel().getSelectedItem();
            txtNameEdit.setText(contact.getName());
            txtNumberEdit.setText(contact.getNumber());
        });

        btnCancelEdit.setOnAction(e -> {
            rightBox.setCenter(contactViewBox);
            rightBox.setBottom(controlBoxOne);

            leftBox.setDisable(false);
        });

        txtNameAdd.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                btnDoneAdd.setDisable(true);
            } else {
                if (txtNumberAdd.getText().equals("")) {
                    btnDoneAdd.setDisable(true);
                } else {
                    btnDoneAdd.setDisable(false);
                }
            }
        });

        txtNumberAdd.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                btnDoneAdd.setDisable(true);
            } else {
                if (txtNameAdd.getText().equals("")) {
                    btnDoneAdd.setDisable(true);
                } else {
                    btnDoneAdd.setDisable(false);
                }
            }
        });

        txtNameEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                btnDoneEdit.setDisable(true);
            } else {
                if (txtNumberEdit.getText().equals("")) {
                    btnDoneEdit.setDisable(true);
                } else {
                    btnDoneEdit.setDisable(false);
                }
            }
        });

        txtNumberEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                btnDoneAdd.setDisable(true);
            } else {
                if (txtNameEdit.getText().equals("")) {
                    btnDoneEdit.setDisable(true);
                } else {
                    btnDoneEdit.setDisable(false);
                }
            }
        });

        btnDoneAdd.setOnAction(e -> {
            if (!isNumber(txtNumberAdd.getText())) {
                showAlert("Phone number can only contain digits!");
                txtNumberAdd.requestFocus();
            } else {
                // save to db
                Contact contact = new Contact(txtNameAdd.getText(), txtNumberAdd.getText());

                boolean exists = false;
                for (int i = 0; i < mainDb.size(); i++) {
                    if (mainDb.get(i).equals(contact)) {
//                    System.out.println("Contact with this name already exists!");
                        showAlert("Contact with this name already exists!");
                        exists = true;
                        btnDoneAdd.setDisable(true);
                    }
                }

                if (!exists) {
                    mainDb.add(contact);

                    syncFile(); // update file

                    // switch to contact view
                    txtNameAdd.setText("");
                    txtNumberAdd.setText("");

                    rightBox.setCenter(contactViewBox);
                    rightBox.setBottom(controlBoxOne);

                    contacts.setItems(mainDb.sorted()); // add contact to listview

                    contacts.getSelectionModel().select(contact); // select added contact

                    leftBox.setDisable(false);
                }

                btnDelete.setDisable(false);
            }
        });

        btnDoneEdit.setOnAction(e -> {
            if (!isNumber(txtNumberEdit.getText())) {
                showAlert("Phone number can only contain digits!");
                txtNumberEdit.requestFocus();
            } else {
                // save to db
                Contact contact = contacts.getSelectionModel().getSelectedItem();
                mainDb.remove(contact);

                Contact new_contact = new Contact(txtNameEdit.getText(), txtNumberEdit.getText());
                mainDb.add(new_contact);

                syncFile(); // update file

                // switch to contact view
                rightBox.setCenter(contactViewBox);
                rightBox.setBottom(controlBoxOne);

                contacts.setItems(mainDb.sorted()); // add contact to listview

                contacts.getSelectionModel().select(new_contact); // select added contact

                leftBox.setDisable(false);
            }
        });

        btnDelete.setOnAction(e -> {
            Contact contact = contacts.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Contact");
            alert.setHeaderText("You are about to delete the entry for " + contact.getName());
            alert.setContentText("Do you want to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
//                Contact contact = contacts.getSelectionModel().getSelectedItem();
                mainDb.remove(contact);
                syncFile();

                contacts.setItems(mainDb.sorted());

                if (mainDb.size() == 0) {
                    pane.setCenter(noContactPane);
                }
            }
        });
    }

    private void populateDb() {
        try {
            Scanner input = new Scanner(file);

            while (input.hasNext()) {
                String line = input.nextLine();
                String[] args = line.split(":\\s*");
                Contact contact = new Contact(args[0], args[1]);
                mainDb.add(contact);
            }

            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error on populateDb(): " + e.getMessage());
        }

        contacts.setItems(mainDb.sorted());
    }

    private void search(String query) {
        ObservableList<Contact> tempDb = FXCollections.observableArrayList();
        for (int i = 0; i < mainDb.size(); i++) {
            Contact contact = mainDb.get(i);
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                tempDb.add(contact);
            }
        }

        contacts.setItems(tempDb.sorted());
        contacts.getSelectionModel().selectFirst();
    }

    private void syncFile() {
        try {
            PrintWriter output = new PrintWriter(file);
            for (Contact contact :
                    mainDb) {
                output.printf("%s:%s\n", contact.getName(), contact.getNumber());
            }
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showAlert(String message, String contactName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
//        alert.setHeaderText("Delete Contact");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean isNumber(String number) {
        for (int i = 0; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
