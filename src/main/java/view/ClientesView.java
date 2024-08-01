package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Cliente;

public class ClientesView extends VBox {
    private TableView<Cliente> tablaClientes;
    private Button btnConsultar;
    private TextField txtAnioNacimiento;
    private ComboBox<String> querySelector;

    public ClientesView() {

        txtAnioNacimiento = new TextField();
        txtAnioNacimiento.setPromptText("Año de Nacimiento");

        querySelector = new ComboBox<>();
        querySelector.getItems().addAll(
                "Listar todos los clientes ordenados por fecha de nacimiento",
                "Clientes nacidos en el año 1990 o posterior"
        );
        querySelector.setValue("Listar todos los clientes ordenados por fecha de nacimiento");

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(new Label("Año de Nacimiento:"), txtAnioNacimiento, querySelector);

        btnConsultar = new Button("Consultar Clientes");

        tablaClientes = new TableView<>();

        TableColumn<Cliente, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty());

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Cliente, String> colFechaNacimiento = new TableColumn<>("Fecha de Nacimiento");
        colFechaNacimiento.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaNacimientoString())
        );

        tablaClientes.getColumns().addAll(colId, colNombre, colFechaNacimiento);

        this.setSpacing(10);
        this.getChildren().addAll(searchBox, btnConsultar, tablaClientes);
    }

    // Getters para los campos
    public TextField getTxtAnioNacimiento() { return txtAnioNacimiento; }
    public TableView<Cliente> getTablaClientes() { return tablaClientes; }
    public Button getBtnConsultar() { return btnConsultar; }
    public ComboBox<String> getQuerySelector() { return querySelector; }
}