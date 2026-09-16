package ejemplo.vehiculos;

import java.math.BigDecimal;

public final class Auto extends Vehiculo {
    public Auto(String placa, String marca, String modelo, int anioFabricacion,
                BigDecimal avaluo, int anioCalculo) {
        super(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo);
    }

    @Override
    public BigDecimal costoMatricula() {
        // Regla didactica: descuento del 5% por anio, con factor minimo de 0.50.
        BigDecimal factor = BigDecimal.ONE
                .subtract(new BigDecimal("0.05").multiply(BigDecimal.valueOf(antiguedad())))
                .max(new BigDecimal("0.50"));
        return redondear(getAvaluo().multiply(new BigDecimal("0.01")).multiply(factor));
    }
}
