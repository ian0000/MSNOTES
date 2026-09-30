package ejemplo.visitor;

import java.util.Objects;

public final class Documento implements Envio {
    private final int pesoGramos;
    private final String destino;
    private final boolean confidencial;

    public Documento(int pesoGramos, String destino, boolean confidencial) {
        if (pesoGramos <= 0) {
            throw new IllegalArgumentException("pesoGramos debe ser positivo");
        }
        this.pesoGramos = pesoGramos;
        this.destino = Validacion.texto(destino, "destino");
        this.confidencial = confidencial;
    }

    public int obtenerPesoGramos() { return pesoGramos; }
    public String obtenerDestino() { return destino; }
    public boolean esConfidencial() { return confidencial; }

    @Override public void aceptar(VisitanteEnvio visitante) {
        Objects.requireNonNull(visitante, "visitante").visitar(this);
    }
}
