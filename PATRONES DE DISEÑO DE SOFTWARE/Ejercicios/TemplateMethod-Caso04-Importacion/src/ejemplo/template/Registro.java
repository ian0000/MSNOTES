package ejemplo.template;

/** Modelo común de salida; los campos son una elección didáctica del ejemplo. */
public final class Registro {
    private final String codigo;
    private final String nombre;

    public Registro(String codigo, String nombre) {
        this.codigo = obligatorio(codigo, "codigo");
        this.nombre = obligatorio(nombre, "nombre");
    }

    public String obtenerCodigo() { return codigo; }
    public String obtenerNombre() { return nombre; }

    private static String obligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Falta el campo " + campo);
        }
        return valor.strip();
    }
}
