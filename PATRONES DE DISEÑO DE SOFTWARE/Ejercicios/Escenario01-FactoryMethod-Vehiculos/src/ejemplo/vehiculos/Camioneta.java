package ejemplo.vehiculos;

import java.math.BigDecimal;

public final class Camioneta extends Vehiculo {
    private final double capacidad;

    /** capacidad se expresa en kilogramos de carga. */
    public Camioneta(String placa, String marca, String modelo, int anioFabricacion,
                     BigDecimal avaluo, int anioCalculo, double capacidad) {
        super(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo);
        this.capacidad = medidaPositiva(capacidad, "capacidad en kg");
    }

    public double getCapacidad() { return capacidad; }

    @Override
    public BigDecimal costoMatricula() {
        BigDecimal porAvaluo = getAvaluo().multiply(new BigDecimal("0.012"));
        BigDecimal porCapacidad = BigDecimal.valueOf(capacidad).multiply(new BigDecimal("0.02"));
        return redondear(porAvaluo.add(porCapacidad));
    }
}
