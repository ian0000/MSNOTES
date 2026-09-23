package ejemplo.adapter;

import ejemplo.adapter.proveedores.Proveedor1;
import java.time.LocalDate;
import java.util.Objects;

/** Envoltura conservada por decisión del grupo; no requiere conversión de formato. */
public final class Proveedor1Adapter implements Contrato {
    private final Proveedor1 proveedor;

    public Proveedor1Adapter(Proveedor1 proveedor) {
        this.proveedor = Objects.requireNonNull(proveedor, "proveedor");
    }

    @Override public RegistroAsistencia consultarAsistenciaDiaria(String identificacion, LocalDate fecha) {
        RegistroAsistencia.validarConsulta(identificacion, fecha);
        return proveedor.consultarAsistenciaDiaria(identificacion, fecha);
    }
}
