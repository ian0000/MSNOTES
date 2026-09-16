package ejemplo.vehiculos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Cliente del patron: no importa ni instancia productos o creadores concretos. */
public final class RegistroVehiculos {
    private final List<Vehiculo> registrados = new ArrayList<>();

    public Vehiculo registrar(CreadorVehiculo creador) {
        Vehiculo vehiculo = Objects.requireNonNull(
                Objects.requireNonNull(creador, "creador").creadorVehiculo(), "vehiculo");
        registrados.add(vehiculo);
        return vehiculo;
    }

    public List<Vehiculo> getRegistrados() {
        return List.copyOf(registrados);
    }
}
