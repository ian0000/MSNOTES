package ejemplo.observer;

import java.math.BigDecimal;
import java.util.Objects;

/** Notificación inmutable enviada a los observadores. */
public final class EstadoSubasta {
    private final long numeroOferta;
    private final String ofertante;
    private final BigDecimal valorActual;

    public EstadoSubasta(long numeroOferta, String ofertante, BigDecimal valorActual) {
        if (numeroOferta <= 0) {
            throw new IllegalArgumentException("El número de oferta debe ser positivo");
        }
        if (ofertante == null || ofertante.isBlank()) {
            throw new IllegalArgumentException("El ofertante es obligatorio");
        }
        this.numeroOferta = numeroOferta;
        this.ofertante = ofertante.strip();
        this.valorActual = Objects.requireNonNull(valorActual, "valorActual");
        if (valorActual.signum() <= 0) {
            throw new IllegalArgumentException("El valor actual debe ser positivo");
        }
    }

    public long obtenerNumeroOferta() { return numeroOferta; }
    public String obtenerOfertante() { return ofertante; }
    public BigDecimal obtenerValorActual() { return valorActual; }
}
