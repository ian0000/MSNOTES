package ejemplo.chain;

import java.math.BigDecimal;
import java.util.Objects;

/** Petición que recorre la cadena sin conocer a sus posibles aprobadores. */
public final class SolicitudCompra {
    private final String codigo;
    private final String areaSolicitante;
    private final String descripcion;
    private final BigDecimal valorEstimado;

    public SolicitudCompra(String codigo, String areaSolicitante, String descripcion,
            BigDecimal valorEstimado) {
        this.codigo = textoObligatorio(codigo, "codigo");
        this.areaSolicitante = textoObligatorio(areaSolicitante, "areaSolicitante");
        this.descripcion = textoObligatorio(descripcion, "descripcion");
        this.valorEstimado = Objects.requireNonNull(valorEstimado, "valorEstimado");
        if (valorEstimado.signum() <= 0) {
            throw new IllegalArgumentException("El valor estimado debe ser positivo");
        }
    }

    public String obtenerCodigo() { return codigo; }
    public String obtenerAreaSolicitante() { return areaSolicitante; }
    public String obtenerDescripcion() { return descripcion; }
    public BigDecimal obtenerValorEstimado() { return valorEstimado; }

    private static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        return valor.trim();
    }
}
