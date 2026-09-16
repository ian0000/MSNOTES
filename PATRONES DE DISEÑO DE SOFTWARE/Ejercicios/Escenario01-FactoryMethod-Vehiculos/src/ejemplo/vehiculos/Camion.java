package ejemplo.vehiculos;

import java.math.BigDecimal;

public final class Camion extends Vehiculo {
    private final double tonelaje;
    private final double capacidadCarga;

    /** Ambas medidas se expresan en toneladas; son datos independientes del ejercicio. */
    public Camion(String placa, String marca, String modelo, int anioFabricacion,
                  BigDecimal avaluo, int anioCalculo, double tonelaje, double capacidadCarga) {
        super(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo);
        this.tonelaje = medidaPositiva(tonelaje, "tonelaje");
        this.capacidadCarga = medidaPositiva(capacidadCarga, "capacidadCarga en toneladas");
    }

    public double getTonelaje() { return tonelaje; }
    public double getCapacidadCarga() { return capacidadCarga; }

    @Override
    public BigDecimal costoMatricula() {
        BigDecimal porAvaluo = getAvaluo().multiply(new BigDecimal("0.015"));
        BigDecimal porTonelaje = BigDecimal.valueOf(tonelaje).multiply(new BigDecimal("10"));
        BigDecimal porCarga = BigDecimal.valueOf(capacidadCarga).multiply(new BigDecimal("5"));
        return redondear(porAvaluo.add(porTonelaje).add(porCarga));
    }
}
