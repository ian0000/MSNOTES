package ejemplo.vehiculos;

import java.math.BigDecimal;

public final class CreadorVehiculoAuto extends CreadorVehiculo {
    public CreadorVehiculoAuto(String placa, String marca, String modelo, int anioFabricacion,
                               BigDecimal avaluo, int anioCalculo) {
        super(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo);
    }

    @Override
    public Vehiculo creadorVehiculo() {
        return new Auto(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo);
    }
}
