package ejemplo.adapter;

import java.util.Objects;

/** Segundo Adapter, con el mismo contrato para el cliente. */
public final class Traductor2Adapter implements SolicitudTraduccion {
    private final Traductor2 traductor;

    public Traductor2Adapter(Traductor2 traductor) {
        this.traductor = Objects.requireNonNull(traductor, "traductor");
    }

    @Override
    public String solicitarTraduccion(String texto) {
        return traductor.traducir(texto);
    }
}
