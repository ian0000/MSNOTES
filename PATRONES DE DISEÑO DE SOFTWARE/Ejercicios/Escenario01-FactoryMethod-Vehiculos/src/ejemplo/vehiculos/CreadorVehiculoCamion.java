package ejemplo.vehiculos;

import java.math.BigDecimal;

public final class CreadorVehiculoCamion extends CreadorVehiculo {
    private final double tonelaje;
    private final double capacidadCarga;

    public CreadorVehiculoCamion(String placa, String marca, String modelo, int anioFabricacion,
                                 BigDecimal avaluo, int anioCalculo, double tonelaje, double capacidadCarga) {
        super(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo);
        this.tonelaje = tonelaje;
        this.capacidadCarga = capacidadCarga;
    }

    @Override
    public Vehiculo creadorVehiculo() {
        return new Camion(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo, tonelaje, capacidadCarga);
    }
}
