package ejemplo.vehiculos;

import java.math.BigDecimal;

/** Creator: recibe y conserva la configuracion; las subclases deciden el producto. */
public abstract class CreadorVehiculo {
    protected final String placa;
    protected final String marca;
    protected final String modelo;
    protected final int anioFabricacion;
    protected final BigDecimal avaluo;
    protected final int anioCalculo;

    protected CreadorVehiculo(String placa, String marca, String modelo, int anioFabricacion,
                              BigDecimal avaluo, int anioCalculo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anioFabricacion = anioFabricacion;
        this.avaluo = avaluo;
        this.anioCalculo = anioCalculo;
    }

    // Se conserva el nombre del metodo del UML del grupo.
    public abstract Vehiculo creadorVehiculo();
}
