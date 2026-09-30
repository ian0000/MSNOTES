package ejemplo.visitor;

import java.math.BigDecimal;
import java.util.Objects;

public final class CargaRefrigerada implements Envio {
    private final BigDecimal pesoKg;
    private final String destino;
    private final BigDecimal volumenLitros;
    private final BigDecimal distanciaKm;
    private final BigDecimal temperaturaRequerida;

    public CargaRefrigerada(BigDecimal pesoKg, String destino, BigDecimal volumenLitros,
            BigDecimal distanciaKm, BigDecimal temperaturaRequerida) {
        this.pesoKg = Validacion.positivo(pesoKg, "pesoKg");
        this.destino = Validacion.texto(destino, "destino");
        this.volumenLitros = Validacion.positivo(volumenLitros, "volumenLitros");
        this.distanciaKm = Validacion.noNegativo(distanciaKm, "distanciaKm");
        this.temperaturaRequerida = Objects.requireNonNull(temperaturaRequerida,
                "temperaturaRequerida");
    }

    public BigDecimal obtenerPesoKg() { return pesoKg; }
    public String obtenerDestino() { return destino; }
    public BigDecimal obtenerVolumenLitros() { return volumenLitros; }
    public BigDecimal obtenerDistanciaKm() { return distanciaKm; }
    public BigDecimal obtenerTemperaturaRequerida() { return temperaturaRequerida; }

    @Override public void aceptar(VisitanteEnvio visitante) {
        Objects.requireNonNull(visitante, "visitante").visitar(this);
    }
}
