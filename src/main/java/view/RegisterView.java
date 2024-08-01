package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView extends GridPane {
    private TextField usernameField;
    private PasswordField passwordField;
    private DatePicker birthdatePicker;
    private Button registerButton;
    private Button cancelButton;

    public RegisterView() {

        this.setPadding(new Insets(20));
        this.setVgap(10);
        this.setHgap(10);

       
        Label usernameLabel = new Label("Usuario:");
        usernameField = new TextField();
        Label passwordLabel = new Label("Contraseña:");
        passwordField = new PasswordField();
        Label birthdateLabel = new Label("Fecha de Nacimiento:");
        birthdatePicker = new DatePicker();
        registerButton = new Button("Registrar");
        cancelButton = new Button("Cancelar");

        // Añadir los controles al GridPane
        this.add(usernameLabel, 0, 0);
        this.add(usernameField, 1, 0);
        this.add(passwordLabel, 0, 1);
        this.add(passwordField, 1, 1);
        this.add(birthdateLabel, 0, 2);
        this.add(birthdatePicker, 1, 2);
        this.add(registerButton, 1, 3);
        this.add(cancelButton, 1, 4);


        Image backgroundImage = new Image(getClass().getResource("/imagen/fondo.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        this.setBackground(new Background(background));
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public DatePicker getBirthdatePicker() {
        return birthdatePicker;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void show(Stage stage) {
        stage.setTitle("Registrar Usuario");
        stage.setScene(new Scene(this, 300, 250));
        stage.show();
    }
}