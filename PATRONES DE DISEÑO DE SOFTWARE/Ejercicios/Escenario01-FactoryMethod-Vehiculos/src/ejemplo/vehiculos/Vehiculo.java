package ejemplo.vehiculos;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Producto abstracto: todos los vehiculos ofrecen exactamente el mismo contrato. */
public abstract class Vehiculo {
    private final String placa;
    private final String marca;
    private final String modelo;
    private final int anioFabricacion;
    private final BigDecimal avaluo;
    private final int anioCalculo;

    protected Vehiculo(String placa, String marca, String modelo, int anioFabricacion,
                       BigDecimal avaluo, int anioCalculo) {
        this.placa = texto(placa, "placa");
        this.marca = texto(marca, "marca");
        this.modelo = texto(modelo, "modelo");
        if (anioCalculo < 1 || anioFabricacion < 1 || anioFabricacion > anioCalculo) {
            throw new IllegalArgumentException("El anio de fabricacion debe estar entre 1 y el anio de calculo");
        }
        if (avaluo == null || avaluo.signum() < 0) {
            throw new IllegalArgumentException("avaluo debe ser no nulo y no negativo");
        }
        this.anioFabricacion = anioFabricacion;
        this.avaluo = avaluo;
        this.anioCalculo = anioCalculo;
    }

    /** Importe academico ficticio; no representa una tarifa legal de matriculacion. */
    public abstract BigDecimal costoMatricula();

    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnioFabricacion() { return anioFabricacion; }
    public BigDecimal getAvaluo() { return avaluo; }
    public int getAnioCalculo() { return anioCalculo; }

    protected final int antiguedad() { return anioCalculo - anioFabricacion; }

    protected static BigDecimal redondear(BigDecimal importe) {
        return importe.setScale(2, RoundingMode.HALF_UP);
    }

    protected static double medidaPositiva(double valor, String campo) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(campo + " debe ser finito y mayor que cero");
        }
        return valor;
    }

    private static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede ser null ni estar en blanco");
        }
        return valor;
    }
}
