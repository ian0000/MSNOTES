package ejemplo.observer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/** ConcreteObserver: actualiza su información y puede decidir ofertar después. */
public final class Participante implements ValorObserver {
    private final String nombre;
    private EstadoSubasta ultimaActualizacion;
    private int notificacionesRecibidas;

    public Participante(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        this.nombre = nombre.strip();
    }

    @Override public void cambioOferta(EstadoSubasta estado) {
        ultimaActualizacion = Objects.requireNonNull(estado, "estado");
        notificacionesRecibidas++;
    }

    public boolean realizarNuevaOferta(Subasta subasta, BigDecimal valor) {
        Objects.requireNonNull(subasta, "subasta");
        return subasta.realizarOferta(nombre, valor);
    }

    public String obtenerNombre() { return nombre; }
    public int obtenerNotificacionesRecibidas() { return notificacionesRecibidas; }
    public Optional<EstadoSubasta> obtenerUltimaActualizacion() {
        return Optional.ofNullable(ultimaActualizacion);
    }
}
