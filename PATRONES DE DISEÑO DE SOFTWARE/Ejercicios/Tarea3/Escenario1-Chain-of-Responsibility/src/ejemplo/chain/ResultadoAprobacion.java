package ejemplo.chain;

import java.util.Objects;

/** Resultado común que evita que el cliente tenga que identificar al manejador concreto. */
public final class ResultadoAprobacion {
    private final boolean aprobada;
    private final String responsable;
    private final String mensaje;

    private ResultadoAprobacion(boolean aprobada, String responsable, String mensaje) {
        this.aprobada = aprobada;
        this.responsable = responsable;
        this.mensaje = Objects.requireNonNull(mensaje, "mensaje");
    }

    public static ResultadoAprobacion aprobadaPor(String responsable) {
        if (responsable == null || responsable.isBlank()) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }
        String nombre = responsable.trim();
        return new ResultadoAprobacion(true, nombre, "Aprobada por " + nombre);
    }

    public static ResultadoAprobacion sinAprobador() {
        return new ResultadoAprobacion(false, null,
                "Ningún nivel de la cadena tiene autoridad suficiente");
    }

    public boolean estaAprobada() { return aprobada; }
    public String obtenerResponsable() { return responsable; }
    public String obtenerMensaje() { return mensaje; }
}
