package ejemplo.adapter;

import java.time.LocalDate;

/** Target: el servicio que espera el sistema institucional. */
public interface Contrato {
    RegistroAsistencia consultarAsistenciaDiaria(String identificacion, LocalDate fecha);
}
