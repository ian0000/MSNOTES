package ejemplo.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Subject: concentra la suscripción y el mecanismo de notificación. */
public abstract class CambioDeValor {
    private final List<ValorObserver> participantes = new ArrayList<>();

    public final void attach(ValorObserver participante) {
        Objects.requireNonNull(participante, "participante");
        if (!participantes.contains(participante)) {
            participantes.add(participante);
        }
    }

    public final void detach(ValorObserver participante) {
        Objects.requireNonNull(participante, "participante");
        participantes.remove(participante);
    }

    public final int obtenerNumeroParticipantes() {
        return participantes.size();
    }

    /**
     * La copia permite que un observador se retire durante una notificación
     * sin alterar el recorrido que ya comenzó.
     */
    protected final void notifyObservers(EstadoSubasta estado) {
        Objects.requireNonNull(estado, "estado");
        for (ValorObserver participante : List.copyOf(participantes)) {
            participante.cambioOferta(estado);
        }
    }
}
