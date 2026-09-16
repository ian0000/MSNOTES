package ejemplo.vehiculos;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/** Entrada por consola. La seleccion de creadores se concentra en esta configuracion. */
public final class Main {
    public static void main(String[] args) {
        try {
            CreadorVehiculo creador = seleccionarCreador(args);
            RegistroVehiculos registro = new RegistroVehiculos();
            Vehiculo vehiculo = registro.registrar(creador);
            System.out.println("Calculo academico ficticio; no es una tarifa oficial.");
            System.out.println("Vehiculo: " + vehiculo.getClass().getSimpleName());
            System.out.println("Placa: " + vehiculo.getPlaca());
            System.out.println("Anio de calculo: " + vehiculo.getAnioCalculo());
            System.out.println("Costo de matricula: " + vehiculo.costoMatricula().toPlainString());
            System.out.println("Registrados en memoria: " + registro.getRegistrados().size());
        } catch (IllegalArgumentException error) {
            System.err.println("Error: " + error.getMessage());
            System.err.println("Uso: Main tipo placa marca modelo anioFabricacion avaluo anioCalculo [capacidadKg | tonelaje capacidadCargaT]");
            System.exit(2);
        }
    }

    static CreadorVehiculo seleccionarCreador(String[] args) {
        Map<String, Function<String[], CreadorVehiculo>> creadores = configurarCreadores();
        if (args.length == 0) {
            throw new IllegalArgumentException("Falta el tipo: auto, camioneta o camion");
        }
        Function<String[], CreadorVehiculo> configurador = creadores.get(args[0].toLowerCase(Locale.ROOT));
        if (configurador == null) {
            throw new IllegalArgumentException("Tipo no soportado: " + args[0]);
        }
        return configurador.apply(args);
    }

    private static Map<String, Function<String[], CreadorVehiculo>> configurarCreadores() {
        Map<String, Function<String[], CreadorVehiculo>> creadores = new LinkedHashMap<>();
        creadores.put("auto", a -> {
            cantidad(a, 7);
            return new CreadorVehiculoAuto(a[1], a[2], a[3], Integer.parseInt(a[4]),
                    new BigDecimal(a[5]), Integer.parseInt(a[6]));
        });
        creadores.put("camioneta", a -> {
            cantidad(a, 8);
            return new CreadorVehiculoCamioneta(a[1], a[2], a[3], Integer.parseInt(a[4]),
                    new BigDecimal(a[5]), Integer.parseInt(a[6]), Double.parseDouble(a[7]));
        });
        creadores.put("camion", a -> {
            cantidad(a, 9);
            return new CreadorVehiculoCamion(a[1], a[2], a[3], Integer.parseInt(a[4]),
                    new BigDecimal(a[5]), Integer.parseInt(a[6]), Double.parseDouble(a[7]),
                    Double.parseDouble(a[8]));
        });
        return creadores;
    }

    private static void cantidad(String[] args, int esperada) {
        if (args.length != esperada) {
            throw new IllegalArgumentException("Cantidad incorrecta de argumentos para " + args[0]);
        }
    }
}
