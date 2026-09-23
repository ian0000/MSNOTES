package ejemplo.adapter.proveedores;

import java.util.Objects;

/** Tipo del fabricante simulado: no depende de clases institucionales. */
public final class MarcacionProveedor2 {
    private final String documento;
    private final String instanteISO;
    private final String tipo;

    public MarcacionProveedor2(String documento, String instanteISO, String tipo) {
        this.documento = Objects.requireNonNull(documento, "documento");
        this.instanteISO = Objects.requireNonNull(instanteISO, "instanteISO");
        this.tipo = Objects.requireNonNull(tipo, "tipo");
    }

    public String getDocumento() { return documento; }
    public String getInstanteISO() { return instanteISO; }
    public String getTipo() { return tipo; }
}
