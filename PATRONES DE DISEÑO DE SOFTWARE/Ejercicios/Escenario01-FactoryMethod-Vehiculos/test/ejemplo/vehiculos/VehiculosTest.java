package ejemplo.vehiculos;

import java.math.BigDecimal;
import java.util.List;

public final class VehiculosTest {
    private static int aprobadas;

    public static void main(String[] args) {
        probar("cada creador produce su variante y conserva los datos", () -> {
            Vehiculo a = auto(2020, "20000").creadorVehiculo();
            Vehiculo b = camioneta(800).creadorVehiculo();
            Vehiculo c = camion(12, 8).creadorVehiculo();
            comprobar(a instanceof Auto && b instanceof Camioneta && c instanceof Camion, "tipos");
            comprobar("ABC-123".equals(a.getPlaca()) && "Marca".equals(a.getMarca())
                    && "Modelo".equals(a.getModelo()), "datos comunes");
            comprobar(a.getAnioFabricacion() == 2020 && a.getAnioCalculo() == 2026, "anios");
            comprobar(((Camioneta) b).getCapacidad() == 800, "capacidad");
            comprobar(((Camion) c).getTonelaje() == 12 && ((Camion) c).getCapacidadCarga() == 8, "carga");
        });
        probar("polimorfismo: costoMatricula sin parametros en todos", () -> {
            List<Vehiculo> vehiculos = List.of(auto(2020, "20000").creadorVehiculo(),
                    camioneta(800).creadorVehiculo(), camion(12, 8).creadorVehiculo());
            String[] esperados = {"140.00", "376.00", "910.00"};
            for (int i = 0; i < vehiculos.size(); i++) {
                importe(esperados[i], vehiculos.get(i).costoMatricula());
            }
        });
        probar("limites de antiguedad y redondeo del Auto", () -> {
            importe("200.00", auto(2026, "20000").creadorVehiculo().costoMatricula());
            importe("100.00", auto(2016, "20000").creadorVehiculo().costoMatricula());
            importe("100.00", auto(1990, "20000").creadorVehiculo().costoMatricula());
            importe("100.01", auto(2026, "10000.5").creadorVehiculo().costoMatricula());
            importe("0.00", auto(2026, "0").creadorVehiculo().costoMatricula());
        });
        probar("el creador reutilizado entrega productos distintos", () -> {
            CreadorVehiculo creador = auto(2020, "20000");
            comprobar(creador.creadorVehiculo() != creador.creadorVehiculo(), "nueva instancia");
        });
        probar("registro en memoria protegido y sin altas invalidas", () -> {
            RegistroVehiculos registro = new RegistroVehiculos();
            Vehiculo v = registro.registrar(auto(2020, "20000"));
            comprobar(registro.getRegistrados().get(0) == v, "registra el producto");
            List<Vehiculo> antes = registro.getRegistrados();
            esperar(UnsupportedOperationException.class, () -> antes.clear());
            esperar(IllegalArgumentException.class, () -> registro.registrar(auto(2030, "20000")));
            comprobar(registro.getRegistrados().size() == 1, "rechazo antes de registrar");
            registro.registrar(camioneta(800));
            comprobar(antes.size() == 1 && registro.getRegistrados().size() == 2, "consulta independiente");
        });
        probar("extiende productos y creadores sin cambiar RegistroVehiculos", () -> {
            CreadorVehiculo nuevoCreador = new CreadorVehiculo("BUS-001", "Marca", "Bus", 2020,
                    new BigDecimal("40000"), 2026) {
                @Override
                public Vehiculo creadorVehiculo() {
                    return new Vehiculo(placa, marca, modelo, anioFabricacion, avaluo, anioCalculo) {
                        @Override
                        public BigDecimal costoMatricula() { return new BigDecimal("99.00"); }
                    };
                }
            };
            Vehiculo busDePrueba = new RegistroVehiculos().registrar(nuevoCreador);
            importe("99.00", busDePrueba.costoMatricula());
        });
        probar("rechaza datos comunes invalidos", () -> {
            esperar(IllegalArgumentException.class, () -> auto(2030, "100").creadorVehiculo());
            esperar(IllegalArgumentException.class, () -> auto(0, "100").creadorVehiculo());
            esperar(IllegalArgumentException.class, () -> auto(2020, "-1").creadorVehiculo());
            esperar(IllegalArgumentException.class, () -> new CreadorVehiculoAuto(" ", "M", "X", 2020, BigDecimal.ONE, 2026).creadorVehiculo());
            esperar(IllegalArgumentException.class, () -> new CreadorVehiculoAuto("A", null, "X", 2020, BigDecimal.ONE, 2026).creadorVehiculo());
            esperar(IllegalArgumentException.class, () -> new CreadorVehiculoAuto("A", "M", "", 2020, BigDecimal.ONE, 2026).creadorVehiculo());
            esperar(IllegalArgumentException.class, () -> new CreadorVehiculoAuto("A", "M", "X", 2020, null, 2026).creadorVehiculo());
            esperar(IllegalArgumentException.class, () -> new CreadorVehiculoAuto("A", "M", "X", 2020, BigDecimal.ONE, 0).creadorVehiculo());
        });
        probar("rechaza capacidades no positivas o no finitas", () -> {
            for (double valor : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY}) {
                esperar(IllegalArgumentException.class, () -> camioneta(valor).creadorVehiculo());
                esperar(IllegalArgumentException.class, () -> camion(valor, 8).creadorVehiculo());
                esperar(IllegalArgumentException.class, () -> camion(12, valor).creadorVehiculo());
            }
        });
        probar("seleccion del creador a partir de entradas del cliente", () -> {
            comprobar(Main.seleccionarCreador(new String[]{"AUTO", "A", "M", "X", "2020", "20000", "2026"})
                    .creadorVehiculo() instanceof Auto, "auto");
            comprobar(Main.seleccionarCreador(new String[]{"camioneta", "A", "M", "X", "2020", "30000", "2026", "800"})
                    .creadorVehiculo() instanceof Camioneta, "camioneta");
            comprobar(Main.seleccionarCreador(new String[]{"camion", "A", "M", "X", "2020", "50000", "2026", "12", "8"})
                    .creadorVehiculo() instanceof Camion, "camion");
        });
        probar("errores de entrada explicitos", () -> {
            esperar(IllegalArgumentException.class, () -> Main.seleccionarCreador(new String[]{}));
            esperar(IllegalArgumentException.class, () -> Main.seleccionarCreador(new String[]{"moto"}));
            esperar(IllegalArgumentException.class, () -> Main.seleccionarCreador(new String[]{"auto"}));
            esperar(IllegalArgumentException.class, () -> Main.seleccionarCreador(new String[]{"auto", "A", "M", "X", "invalido", "20000", "2026"}));
        });
        System.out.println("Resultado: " + aprobadas + "/10 pruebas aprobadas.");
    }

    private static CreadorVehiculo auto(int anio, String avaluo) {
        return new CreadorVehiculoAuto("ABC-123", "Marca", "Modelo", anio, new BigDecimal(avaluo), 2026);
    }

    private static CreadorVehiculo camioneta(double capacidad) {
        return new CreadorVehiculoCamioneta("DEF-456", "Marca", "Modelo", 2021,
                new BigDecimal("30000"), 2026, capacidad);
    }

    private static CreadorVehiculo camion(double tonelaje, double carga) {
        return new CreadorVehiculoCamion("GHI-789", "Marca", "Modelo", 2018,
                new BigDecimal("50000"), 2026, tonelaje, carga);
    }

    private static void importe(String esperado, BigDecimal actual) {
        comprobar(actual.compareTo(new BigDecimal(esperado)) == 0 && actual.scale() == 2,
                "Esperado " + esperado + ", obtenido " + actual);
    }

    private static void probar(String nombre, Runnable caso) {
        caso.run(); aprobadas++; System.out.println("OK: " + nombre);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) { throw new AssertionError(mensaje); }
    }

    private static void esperar(Class<? extends Throwable> tipo, Runnable accion) {
        try { accion.run(); }
        catch (Throwable error) {
            if (tipo.isInstance(error)) { return; }
            throw new AssertionError("Se esperaba " + tipo.getSimpleName(), error);
        }
        throw new AssertionError("No se lanzo " + tipo.getSimpleName());
    }
}
