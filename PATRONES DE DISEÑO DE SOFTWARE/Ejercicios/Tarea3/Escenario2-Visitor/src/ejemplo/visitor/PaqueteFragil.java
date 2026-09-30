package ejemplo.visitor;

import java.math.BigDecimal;
import java.util.Objects;

public final class PaqueteFragil implements Envio {
    private final BigDecimal pesoKg;
    private final String destino;
    private final BigDecimal valorDeclarado;
    private final boolean embalajeReforzado;

    public PaqueteFragil(BigDecimal pesoKg, String destino, BigDecimal valorDeclarado,
            boolean embalajeReforzado) {
        this.pesoKg = Validacion.positivo(pesoKg, "pesoKg");
        this.destino = Validacion.texto(destino, "destino");
        this.valorDeclarado = Validacion.noNegativo(valorDeclarado, "valorDeclarado");
        this.embalajeReforzado = embalajeReforzado;
    }

    public BigDecimal obtenerPesoKg() { return pesoKg; }
    public String obtenerDestino() { return destino; }
    public BigDecimal obtenerValorDeclarado() { return valorDeclarado; }
    public boolean tieneEmbalajeReforzado() { return embalajeReforzado; }

    @Override public void aceptar(VisitanteEnvio visitante) {
        Objects.requireNonNull(visitante, "visitante").visitar(this);
    }
}
