package ejemplo.composite;

import java.util.Locale;

/** Componente común: no contiene un valor propio ni distingue hojas de grupos. */
public abstract class PartidaPresupuestaria {
    private final String codigo;
    private final String descripcion;

    protected PartidaPresupuestaria(String codigo, String descripcion) {
        this.codigo = textoObligatorio(codigo, "codigo");
        this.descripcion = textoObligatorio(descripcion, "descripcion");
    }

    public final String obtenerCodigo() { return codigo; }
    public final String obtenerDescripcion() { return descripcion; }
    public abstract float calcularValorTotal();
    public abstract String mostrarEstructura();

    // Permite detectar ciclos por polimorfismo sin instanceof.
    protected boolean contiene(PartidaPresupuestaria elemento) {
        return this == elemento;
    }

    protected final String linea(float total) {
        return String.format(Locale.ROOT, "%s - %s: %.2f", codigo, descripcion, total);
    }

    private static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        return valor.trim();
    }
}
