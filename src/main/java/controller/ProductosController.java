package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.Producto;
import util.DatabaseConnection;
import view.ClientesView;
import view.ProductosView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductosController {
    private ProductosView productosView;
    private ObservableList<Producto> productos = FXCollections.observableArrayList();

    public ProductosController(ProductosView productosView) {
        this.productosView = productosView;
        initializeListeners();
    }

    private void initializeListeners() {
        productosView.getBtnConsultar().setOnAction(e -> consultarProductos());
        productosView.getBtnClientes().setOnAction(e -> openClientesWindow());
    }

    private void consultarProductos() {
        productos.clear();
        String nombre = productosView.getTxtNombre().getText().trim();
        String precioStr = productosView.getTxtPrecio().getText().trim();
        String ubicacion = productosView.getTxtUbicacion().getText().trim();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM productos WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (!nombre.isEmpty()) {
            queryBuilder.append(" AND nombre LIKE ?");
            parameters.add("%" + nombre + "%");
        }
        if (!precioStr.isEmpty()) {
            queryBuilder.append(" AND precio > ?");
            try {
                double precio = Double.parseDouble(precioStr);
                parameters.add(precio);
            } catch (NumberFormatException e) {
                return;
            }
        }
        if (!ubicacion.isEmpty()) {
            queryBuilder.append(" AND ubicacion LIKE ?");
            parameters.add("%" + ubicacion + "%");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getString("tipo"),
                            rs.getString("ubicacion")
                    ));
                }

                Platform.runLater(() -> {
                    productosView.getTablaProductos().setItems(null);
                    productosView.getTablaProductos().setItems(productos);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openClientesWindow() {
        Stage clientesStage = new Stage();
        ClientesView clientesView = new ClientesView();
        ClientesController clientesController = new ClientesController(clientesView, clientesStage);
        clientesController.show(clientesStage);
    }
}