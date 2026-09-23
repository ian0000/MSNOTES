package ejemplo.adapter;

import ejemplo.adapter.proveedores.MarcacionProveedor2;
import ejemplo.adapter.proveedores.Proveedor1;
import ejemplo.adapter.proveedores.Proveedor2;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public final class AdapterTest {
    private static final LocalDate FECHA = LocalDate.of(2026, 9, 23);
    private static int aprobadas;
    private AdapterTest() { }

    public static void main(String[] args) {
        probar("delegación al proveedor compatible", () -> {
            RegistroAsistencia original = new RegistroAsistencia("A", FECHA, LocalTime.of(8, 0), LocalTime.of(17, 0));
            Contrato servicio = new Proveedor1Adapter(new Proveedor1(List.of(original)));
            verificar(servicio.consultarAsistenciaDiaria("A", FECHA) == original);
            RegistroAsistencia ausente = servicio.consultarAsistenciaDiaria("B", FECHA);
            horas(ausente, null, null);
            verificar(ausente.obtenerIdentificacion().equals("B"));
        });
        probar("primera entrada y última salida sin ordenar", () -> horas(consultar(List.of(
                marca("17:30", "SALIDA"), marca("13:00", "ENTRADA"),
                marca("08:00", "ENTRADA"), marca("12:00", "SALIDA"))), "08:00", "17:30"));
        probar("solo entrada no inventa salida", () -> horas(consultar(List.of(marca("08:00", "ENTRADA"))), "08:00", null));
        probar("solo salida no inventa entrada", () -> horas(consultar(List.of(marca("17:00", "SALIDA"))), null, "17:00"));
        probar("sin marcas conserva persona y fecha", () -> {
            RegistroAsistencia registro = consultar(List.of());
            horas(registro, null, null);
            verificar(registro.obtenerIdentificacion().equals("A") && registro.obtenerFecha().equals(FECHA));
        });
        probar("convierte parámetros y filtra respuesta ajena", () -> {
            Proveedor2 biblioteca = new Proveedor2(List.of()) {
                @Override public List<MarcacionProveedor2> recuperarMarcaciones(String documento, String fechaISO) {
                    verificar(documento.equals("A") && fechaISO.equals("2026-09-23"));
                    return List.of(marca("08:00", "ENTRADA"),
                            new MarcacionProveedor2("B", "2026-09-23T23:00:00", "SALIDA"),
                            new MarcacionProveedor2("A", "2026-09-24T22:00:00", "SALIDA"));
                }
            };
            horas(new Proveedor2Adapter(biblioteca).consultarAsistenciaDiaria("A", FECHA), "08:00", null);
        });
        probar("cliente independiente del proveedor", () -> {
            Contrato antiguo = new Proveedor1Adapter(new Proveedor1(List.of()));
            Contrato nuevo = new Proveedor2Adapter(new Proveedor2(List.of()));
            String esperado = "A | 2026-09-23 | ingreso: sin registro | salida: sin registro";
            verificar(new Cliente(antiguo).consultar("A", FECHA).equals(esperado));
            verificar(new Cliente(nuevo).consultar("A", FECHA).equals(esperado));
        });
        probar("marcas repetidas no alteran extremos", () -> horas(consultar(List.of(
                marca("08:00", "ENTRADA"), marca("08:00", "ENTRADA"),
                marca("17:00", "SALIDA"), marca("17:00", "SALIDA"))), "08:00", "17:00"));
        probar("tipos desconocidos son errores", () -> falla(IllegalArgumentException.class,
                () -> consultar(List.of(marca("08:00", "DESCONOCIDO")))));
        probar("fecha malformada no se oculta como ausencia", () -> falla(DateTimeParseException.class,
                () -> consultar(List.of(new MarcacionProveedor2("A", "fecha-invalida", "ENTRADA")))));
        probar("fallo del dispositivo no se oculta como ausencia", () -> {
            Proveedor2 roto = new Proveedor2(List.of()) {
                @Override public List<MarcacionProveedor2> recuperarMarcaciones(String documento, String fechaISO) {
                    throw new IllegalStateException("Dispositivo no disponible");
                }
            };
            falla(IllegalStateException.class, () -> new Proveedor2Adapter(roto).consultarAsistenciaDiaria("A", FECHA));
        });
        probar("consulta inválida y dependencias nulas", () -> {
            for (Contrato servicio : List.of(new Proveedor1Adapter(new Proveedor1(List.of())),
                    new Proveedor2Adapter(new Proveedor2(List.of())))) {
                falla(IllegalArgumentException.class, () -> servicio.consultarAsistenciaDiaria(" ", FECHA));
                falla(NullPointerException.class, () -> servicio.consultarAsistenciaDiaria("A", null));
            }
            falla(NullPointerException.class, () -> new Cliente(null));
            falla(NullPointerException.class, () -> new Proveedor1Adapter(null));
            falla(NullPointerException.class, () -> new Proveedor2Adapter(null));
        });
        System.out.println("Resultado: " + aprobadas + "/12 pruebas aprobadas.");
    }

    private static MarcacionProveedor2 marca(String hora, String tipo) {
        return new MarcacionProveedor2("A", FECHA + "T" + hora + ":00", tipo);
    }
    private static RegistroAsistencia consultar(List<MarcacionProveedor2> marcas) {
        return new Proveedor2Adapter(new Proveedor2(marcas)).consultarAsistenciaDiaria("A", FECHA);
    }
    private static void horas(RegistroAsistencia registro, String ingreso, String salida) {
        verificar(registro.obtenerHoraIngreso().equals(hora(ingreso)));
        verificar(registro.obtenerHoraSalida().equals(hora(salida)));
    }
    private static Optional<LocalTime> hora(String texto) {
        return texto == null ? Optional.empty() : Optional.of(LocalTime.parse(texto));
    }
    private static void probar(String nombre, Runnable prueba) {
        prueba.run(); aprobadas++; System.out.println("OK: " + nombre);
    }
    private static void verificar(boolean condicion) { if (!condicion) { throw new AssertionError(); } }
    private static void falla(Class<? extends Throwable> tipo, Runnable accion) {
        try { accion.run(); } catch (Throwable error) {
            if (tipo.isInstance(error)) { return; }
            throw new AssertionError("Excepción inesperada", error);
        }
        throw new AssertionError("Faltó " + tipo.getSimpleName());
    }
}
