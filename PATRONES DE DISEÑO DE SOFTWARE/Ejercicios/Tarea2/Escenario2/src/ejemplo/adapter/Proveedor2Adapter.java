package ejemplo.adapter;

import ejemplo.adapter.proveedores.MarcacionProveedor2;
import ejemplo.adapter.proveedores.Proveedor2;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/** Convierte la consulta y las marcas del fabricante al contrato institucional. */
public final class Proveedor2Adapter implements Contrato {
    private final Proveedor2 proveedor;

    public Proveedor2Adapter(Proveedor2 proveedor) {
        this.proveedor = Objects.requireNonNull(proveedor, "proveedor");
    }

    @Override public RegistroAsistencia consultarAsistenciaDiaria(String identificacion, LocalDate fecha) {
        RegistroAsistencia.validarConsulta(identificacion, fecha);
        // La biblioteca recibe la fecha como texto; el cliente utiliza LocalDate.
        List<MarcacionProveedor2> marcas = Objects.requireNonNull(
                proveedor.recuperarMarcaciones(identificacion, fecha.toString()), "respuesta del proveedor");
        LocalTime ingreso = null;
        LocalTime salida = null;
        for (MarcacionProveedor2 marca : marcas) {
            if (!identificacion.equals(marca.getDocumento())) { continue; }
            LocalDateTime instante = LocalDateTime.parse(marca.getInstanteISO());
            if (!instante.toLocalDate().equals(fecha)) { continue; }
            LocalTime hora = instante.toLocalTime();
            switch (marca.getTipo()) {
                case "ENTRADA":
                    if (ingreso == null || hora.isBefore(ingreso)) { ingreso = hora; }
                    break;
                case "SALIDA":
                    if (salida == null || hora.isAfter(salida)) { salida = hora; }
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de marcación desconocido: " + marca.getTipo());
            }
        }
        // Sin marcas se conservan identificación y fecha con horas ausentes.
        return new RegistroAsistencia(identificacion, fecha, ingreso, salida);
    }
}
