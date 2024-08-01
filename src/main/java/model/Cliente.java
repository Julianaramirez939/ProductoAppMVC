package model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cliente {
    private final IntegerProperty idCliente;
    private final StringProperty nombre;
    private final ObjectProperty<LocalDate> fechaNacimiento;


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Cliente(int idCliente, String nombre, LocalDate fechaNacimiento) {
        this.idCliente = new SimpleIntegerProperty(idCliente);
        this.nombre = new SimpleStringProperty(nombre);
        this.fechaNacimiento = new SimpleObjectProperty<>(fechaNacimiento);
    }


    public int getIdCliente() {
        return idCliente.get();
    }

    public void setIdCliente(int idCliente) {
        this.idCliente.set(idCliente);
    }

    public IntegerProperty idClienteProperty() {
        return idCliente;
    }


    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }


    public LocalDate getFechaNacimiento() {
        return fechaNacimiento.get();
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento.set(fechaNacimiento);
    }

    public ObjectProperty<LocalDate> fechaNacimientoProperty() {
        return fechaNacimiento;
    }


    public String getFechaNacimientoString() {
        return fechaNacimiento.get().format(DATE_FORMATTER);
    }
}