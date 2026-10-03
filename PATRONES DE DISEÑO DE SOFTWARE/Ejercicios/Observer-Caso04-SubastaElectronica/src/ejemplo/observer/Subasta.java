package ejemplo.observer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/** ConcreteSubject: acepta ofertas y notifica solo cuando cambia la mayor. */
public final class Subasta extends CambioDeValor {
    private BigDecimal valorActual = BigDecimal.ZERO;
    private EstadoSubasta estadoActual;
    private long numeroOferta;

    public boolean realizarOferta(String ofertante, BigDecimal valor) {
        if (ofertante == null || ofertante.isBlank()) {
            throw new IllegalArgumentException("El ofertante es obligatorio");
        }
        Objects.requireNonNull(valor, "valor");
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("La oferta debe ser positiva");
        }
        if (valor.compareTo(valorActual) <= 0) {
            return false;
        }

        valorActual = valor;
        estadoActual = new EstadoSubasta(++numeroOferta, ofertante, valorActual);
        notifyObservers(estadoActual);
        return true;
    }

    public BigDecimal obtenerValorActual() { return valorActual; }
    public Optional<EstadoSubasta> obtenerEstadoActual() { return Optional.ofNullable(estadoActual); }
}
