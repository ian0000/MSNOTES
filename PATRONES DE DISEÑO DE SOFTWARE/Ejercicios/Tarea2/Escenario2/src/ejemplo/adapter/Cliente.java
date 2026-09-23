package ejemplo.adapter;

import java.time.LocalDate;
import java.util.Objects;

public final class Cliente {
    private final Contrato servicio;

    public Cliente(Contrato servicio) { this.servicio = Objects.requireNonNull(servicio, "servicio"); }

    public String consultar(String identificacion, LocalDate fecha) {
        RegistroAsistencia registro = servicio.consultarAsistenciaDiaria(identificacion, fecha);
        return registro.obtenerIdentificacion() + " | " + registro.obtenerFecha()
                + " | ingreso: " + registro.obtenerHoraIngreso().map(Object::toString).orElse("sin registro")
                + " | salida: " + registro.obtenerHoraSalida().map(Object::toString).orElse("sin registro");
    }
}
