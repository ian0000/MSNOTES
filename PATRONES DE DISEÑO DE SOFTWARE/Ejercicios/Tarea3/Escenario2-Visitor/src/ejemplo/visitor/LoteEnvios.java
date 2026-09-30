package ejemplo.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** ObjectStructure que aplica cualquier visitante a todos sus elementos. */
public final class LoteEnvios {
    private final List<Envio> envios = new ArrayList<>();

    public void agregar(Envio envio) {
        envios.add(Objects.requireNonNull(envio, "envio"));
    }

    public void ejecutar(VisitanteEnvio visitante) {
        Objects.requireNonNull(visitante, "visitante");
        for (Envio envio : envios) {
            envio.aceptar(visitante);
        }
    }
}
