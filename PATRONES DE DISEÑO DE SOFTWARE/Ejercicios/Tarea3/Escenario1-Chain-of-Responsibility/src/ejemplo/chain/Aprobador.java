package ejemplo.chain;

import java.util.Objects;

/** Handler base: resuelve la solicitud o la delega al siguiente eslabón. */
public abstract class Aprobador {
    private Aprobador siguiente;

    public final void setSiguiente(Aprobador siguiente) {
        if (siguiente == this) {
            throw new IllegalArgumentException("Un aprobador no puede delegarse a sí mismo");
        }
        this.siguiente = siguiente;
    }

    public final ResultadoAprobacion manejar(SolicitudCompra solicitud) {
        Objects.requireNonNull(solicitud, "solicitud");
        if (puedeAprobar(solicitud)) {
            return resolver(solicitud);
        }
        if (siguiente != null) {
            return siguiente.manejar(solicitud);
        }
        return ResultadoAprobacion.sinAprobador();
    }

    protected abstract boolean puedeAprobar(SolicitudCompra solicitud);
    protected abstract ResultadoAprobacion resolver(SolicitudCompra solicitud);
}
