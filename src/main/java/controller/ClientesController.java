package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cliente;
import util.DatabaseConnection;
import view.ClientesView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientesController {
    private ClientesView clientesView;
    private Stage stage;
    private ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    public ClientesController(ClientesView clientesView, Stage stage) {
        this.clientesView = clientesView;
        this.stage = stage;
        initializeListeners();
    }

    private void initializeListeners() {
        clientesView.getBtnConsultar().setOnAction(e -> consultarClientes());
    }

    private void consultarClientes() {
        clientes.clear();
        String selectedQuery = clientesView.getQuerySelector().getValue();
        String anioStr = clientesView.getTxtAnioNacimiento().getText().trim();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM clientes WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (selectedQuery.equals("Clientes nacidos en el año 1990 o posterior")) {
            queryBuilder.append(" AND YEAR(fecha_nacimiento) >= ?");
            parameters.add(1990);
        } else {
            queryBuilder.append(" ORDER BY fecha_nacimiento ASC");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_nacimiento").toLocalDate()
                    ));
                }

                Platform.runLater(() -> {
                    clientesView.getTablaClientes().setItems(null);
                    clientesView.getTablaClientes().setItems(clientes);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(Stage primaryStage) {
        primaryStage.setTitle("Clientes");
        primaryStage.setScene(new Scene(clientesView, 600, 400));
        primaryStage.show();
    }
}