package ejemplo.chain;

import java.util.Objects;

/** Cliente del patrón: solo conoce el primer manejador configurado. */
public final class ServicioCompras {
    private final Aprobador primerAprobador;

    public ServicioCompras(Aprobador primerAprobador) {
        this.primerAprobador = Objects.requireNonNull(primerAprobador, "primerAprobador");
    }

    public ResultadoAprobacion procesar(SolicitudCompra solicitud) {
        return primerAprobador.manejar(solicitud);
    }
}
