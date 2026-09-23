package ejemplo.adapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

/** Resultado inmutable de una consulta; no consulta dispositivos. */
public final class RegistroAsistencia {
    private final String identificacion;
    private final LocalDate fecha;
    private final LocalTime horaIngreso;
    private final LocalTime horaSalida;

    public RegistroAsistencia(String identificacion, LocalDate fecha,
                             LocalTime horaIngreso, LocalTime horaSalida) {
        validarConsulta(identificacion, fecha);
        this.identificacion = identificacion;
        this.fecha = fecha;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
    }

    public String obtenerIdentificacion() { return identificacion; }
    public LocalDate obtenerFecha() { return fecha; }
    public Optional<LocalTime> obtenerHoraIngreso() { return Optional.ofNullable(horaIngreso); }
    public Optional<LocalTime> obtenerHoraSalida() { return Optional.ofNullable(horaSalida); }

    static void validarConsulta(String identificacion, LocalDate fecha) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificación es obligatoria");
        }
        Objects.requireNonNull(fecha, "fecha");
    }
}
