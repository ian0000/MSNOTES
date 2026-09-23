package ejemplo.adapter;

import java.util.Objects;

/** No conoce a Traductor1, Traductor2 ni sus metodos particulares. */
public final class Cliente {
    private final SolicitudTraduccion solicitud;

    public Cliente(SolicitudTraduccion solicitud) {
        this.solicitud = Objects.requireNonNull(solicitud, "solicitud");
    }

    public String solicitarTraduccion(String texto) {
        return solicitud.solicitarTraduccion(texto);
    }
}
