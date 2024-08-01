package controller;

import view.RegisterView;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class RegisterController {

    private RegisterView registerView;
    private Stage stage;

    public RegisterController(RegisterView registerView, Stage stage) {
        this.registerView = registerView;
        this.stage = stage;
        initializeListeners();
    }

    private void initializeListeners() {
        registerView.getRegisterButton().setOnAction(e -> handleRegister());
        registerView.getCancelButton().setOnAction(e -> stage.close());
    }

    private void handleRegister() {
        String username = registerView.getUsernameField().getText().trim();
        String password = registerView.getPasswordField().getText().trim();
        LocalDate birthdate = registerView.getBirthdatePicker().getValue();


        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "El nombre de usuario y la contraseña no pueden estar vacíos", Alert.AlertType.ERROR);
        } else if (username.contains(" ") || password.contains(" ")) {
            showAlert("Error", "El nombre de usuario y la contraseña no pueden contener espacios", Alert.AlertType.ERROR);
        } else if (isUserRegistered(username)) {
            showAlert("Error", "El usuario ya está registrado", Alert.AlertType.ERROR);
        } else if (!isOfLegalAge(birthdate)) {
            showAlert("Error", "Debe ser mayor de edad para registrarse", Alert.AlertType.ERROR);
        } else {
            registerUser(username, password, birthdate);
            showAlert("Éxito", "Usuario registrado exitosamente", Alert.AlertType.INFORMATION);
            stage.close();
        }
    }

    private boolean isUserRegistered(String username) {
        boolean userExists = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://bc2hky8dpornvthdni1y-mysql.services.clever-cloud.com:3306/bc2hky8dpornvthdni1y",
                    "upgfp6ned3m77ha4",
                    "TdAsLKdnXx0XEHNwKFCB"
            );

            String sql = "SELECT * FROM clientes WHERE nombre = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Error de conexión a la base de datos", Alert.AlertType.ERROR);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userExists;
    }

    private boolean isOfLegalAge(LocalDate birthdate) {
        Period age = Period.between(birthdate, LocalDate.now());
        return age.getYears() >= 18;
    }

    private void registerUser(String username, String password, LocalDate birthdate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://bc2hky8dpornvthdni1y-mysql.services.clever-cloud.com:3306/bc2hky8dpornvthdni1y",
                    "upgfp6ned3m77ha4",
                    "TdAsLKdnXx0XEHNwKFCB"
            );


            String sql = "INSERT INTO clientes (nombre, id_cliente, fecha_nacimiento) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);  // Almacenar la contraseña en 'id_cliente'
            preparedStatement.setDate(3, java.sql.Date.valueOf(birthdate));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Error al registrar el usuario", Alert.AlertType.ERROR);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}