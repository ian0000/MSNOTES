package ejemplo.adapter.proveedores;

import ejemplo.adapter.RegistroAsistencia;
import java.time.LocalDate;
import java.util.List;

/** Proveedor antiguo simulado: ya entrega datos en el formato institucional. */
public final class Proveedor1 {
    private final List<RegistroAsistencia> registros;

    public Proveedor1(List<RegistroAsistencia> registros) {
        this.registros = List.copyOf(registros);
    }

    public RegistroAsistencia consultarAsistenciaDiaria(String identificacion, LocalDate fecha) {
        return registros.stream()
                .filter(registro -> registro.obtenerIdentificacion().equals(identificacion))
                .filter(registro -> registro.obtenerFecha().equals(fecha))
                .findFirst()
                .orElseGet(() -> new RegistroAsistencia(identificacion, fecha, null, null));
    }
}
