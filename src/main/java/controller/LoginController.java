package controller;

import view.LoginView;
import view.RegisterView;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import view.ProductosView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        initializeListeners();
    }

    private void initializeListeners() {
        loginView.getLoginButton().setOnAction(e -> handleLogin());
        loginView.getRegisterButton().setOnAction(e -> openRegisterWindow());
    }

    private void handleLogin() {
        String username = loginView.getUsernameField().getText();
        String password = loginView.getPasswordField().getText();

        if (authenticate(username, password)) {
            showAlert("Éxito", "Inicio de sesión exitoso", Alert.AlertType.INFORMATION);
            openProductosWindow();
        } else {
            showAlert("Error", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
        }
    }

    private boolean authenticate(String username, String password) {
        boolean isAuthenticated = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://bc2hky8dpornvthdni1y-mysql.services.clever-cloud.com:3306/bc2hky8dpornvthdni1y",
                    "upgfp6ned3m77ha4",
                    "TdAsLKdnXx0XEHNwKFCB"
            );


            String sql = "SELECT * FROM clientes WHERE nombre = ? AND id_cliente = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isAuthenticated = true;
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

        return isAuthenticated;
    }

    private void openRegisterWindow() {
        Stage registerStage = new Stage();
        RegisterView registerView = new RegisterView();
        RegisterController registerController = new RegisterController(registerView, registerStage);
        registerView.show(registerStage);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openProductosWindow() {
        ProductosView productosView = new ProductosView();
        ProductosController productosController = new ProductosController(productosView);
        Stage productosStage = new Stage();
        productosStage.setTitle("Consulta de Productos");
        productosStage.setScene(new Scene(productosView, 700, 400));
        productosStage.show();

        ((Stage) loginView.getScene().getWindow()).close();
    }
}