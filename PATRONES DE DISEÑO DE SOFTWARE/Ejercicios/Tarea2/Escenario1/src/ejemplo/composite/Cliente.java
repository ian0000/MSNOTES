package ejemplo.composite;

import java.util.Locale;
import java.util.Objects;

/** El mismo cliente procesa una hoja o un grupo sin comprobar su tipo concreto. */
public final class Cliente {
    public String consultar(PartidaPresupuestaria partida) {
        Objects.requireNonNull(partida, "partida");
        return partida.mostrarEstructura()
                + String.format(Locale.ROOT, "\nTotal consultado: %.2f", partida.calcularValorTotal());
    }
}
