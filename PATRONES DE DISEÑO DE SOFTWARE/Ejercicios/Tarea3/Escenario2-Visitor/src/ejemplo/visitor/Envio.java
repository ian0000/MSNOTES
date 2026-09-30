package ejemplo.visitor;

/** Element del patrón Visitor. */
public interface Envio {
    void aceptar(VisitanteEnvio visitante);
}
