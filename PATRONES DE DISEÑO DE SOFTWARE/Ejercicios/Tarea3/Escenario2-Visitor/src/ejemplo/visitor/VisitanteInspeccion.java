package ejemplo.visitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** ConcreteVisitor que reúne las reglas de inspección. */
public final class VisitanteInspeccion implements VisitanteEnvio {
    private static final BigDecimal MINIMA = new BigDecimal("-20");
    private static final BigDecimal MAXIMA = new BigDecimal("8");
    private final List<String> resultados = new ArrayList<>();

    @Override public void visitar(Documento documento) {
        resultados.add(documento.esConfidencial()
                ? "Documento a " + documento.obtenerDestino() + ": requiere custodia especial"
                : "Documento a " + documento.obtenerDestino() + ": conforme");
    }

    @Override public void visitar(PaqueteFragil paquete) {
        resultados.add(paquete.tieneEmbalajeReforzado()
                ? "Paquete frágil a " + paquete.obtenerDestino() + ": conforme"
                : "Paquete frágil a " + paquete.obtenerDestino() + ": requiere reforzar embalaje");
    }

    @Override public void visitar(CargaRefrigerada carga) {
        BigDecimal temperatura = carga.obtenerTemperaturaRequerida();
        boolean fuera = temperatura.compareTo(MINIMA) < 0 || temperatura.compareTo(MAXIMA) > 0;
        resultados.add(fuera
                ? "Carga refrigerada a " + carga.obtenerDestino()
                        + ": temperatura fuera del intervalo operativo"
                : "Carga refrigerada a " + carga.obtenerDestino() + ": conforme");
    }

    public List<String> obtenerResultados() {
        return Collections.unmodifiableList(new ArrayList<>(resultados));
    }
}
