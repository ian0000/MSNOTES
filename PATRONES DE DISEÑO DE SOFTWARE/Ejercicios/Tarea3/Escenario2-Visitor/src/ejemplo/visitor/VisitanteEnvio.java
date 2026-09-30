package ejemplo.visitor;

/** Visitor: una sobrecarga por cada tipo concreto de envío. */
public interface VisitanteEnvio {
    void visitar(Documento documento);
    void visitar(PaqueteFragil paquete);
    void visitar(CargaRefrigerada carga);
}
