package ejemplo.vehiculos;

import java.math.BigDecimal;

public final class CreadorVehiculoCamioneta extends CreadorVehiculo {
    private final double capacidad;

    public CreadorVehiculoCamioneta(String placa, String marca, String modelo, int anioFabricacion,
                                    BigDecimal avaluo, int anioCalculo, double capacidad) {
        super(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo);
        this.capacidad = capacidad;
    }

    @Override
    public Vehiculo creadorVehiculo() {
        return new Camioneta(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo, capacidad);
    }
}
