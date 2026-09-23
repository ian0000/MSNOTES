package ejemplo.adapter;

import java.util.Objects;

/** Adapter de objetos: convierte la llamada esperada en la disponible. */
public final class Traductor1Adapter implements SolicitudTraduccion {
    private final Traductor1 traductor;

    public Traductor1Adapter(Traductor1 traductor) {
        this.traductor = Objects.requireNonNull(traductor, "traductor");
    }

    @Override
    public String solicitarTraduccion(String texto) {
        return traductor.traducir(texto);
    }
}
